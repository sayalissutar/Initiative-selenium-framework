package Pages;

import Actions.ActionEngine;
import Locators.LoginPageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;

public class LoginPage extends ActionEngine {
    private final LoginPageLocators loc = new LoginPageLocators();
    private final WebDriver driver;
    private final ExtentTest test;
    private final WebDriverWait wait;

    // ✅ IMPORTANT: pass driver/test up to ActionEngine
    public LoginPage(WebDriver driver, ExtentTest test) {
        
        this.driver = driver;
        this.test = test;
        this.wait  = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void login(String username, String password) {
        // Ensure page is ready
        wait.until(d -> ((JavascriptExecutor)d)
            .executeScript("return document.readyState").equals("complete"));

        try {
            waitForSeconds(1);

            // Branch 1: Microsoft SSO (login.microsoftonline.com or Microsoft fields present)
            boolean onMsDomain = driver.getCurrentUrl().toLowerCase().contains("login.microsoftonline.com");
            boolean msEmailPresent = isPresent(loc.getEmailField(), 5);

            if (onMsDomain || msEmailPresent) {
                info("Detected Microsoft SSO flow");

                // Sometimes the page shows account tiles; click "Use another account" if present
                try {
                    WebElement another = driver.findElement(loc.useAnotherAccountOption());
                    if (another.isDisplayed()) {
                        another.click();
                    }
                } catch (NoSuchElementException ignored) {}

                // Email
                wait.until(ExpectedConditions.visibilityOfElementLocated(loc.getEmailField()));
                type(loc.getEmailField(), username, "Enter Email");
                wait.until(ExpectedConditions.elementToBeClickable(loc.getConfirmbutton()));
                click(loc.getConfirmbutton(), "Next/Submit");

                // Password
                wait.until(ExpectedConditions.visibilityOfElementLocated(loc.getPasswordField()));
                type(loc.getPasswordField(), password, "Enter Password");
                wait.until(ExpectedConditions.elementToBeClickable(loc.getConfirmbutton()));
                click(loc.getConfirmbutton(), "Sign in");

                // Optional: Stay signed in? Confirm if shown
                try {
                    if (isPresent(loc.getConfirmbutton(), 5)) {
                        click(loc.getConfirmbutton(), "Confirm stay signed in");
                    }
                } catch (Exception ignored) {}

                if (test != null) test.pass("Microsoft SSO login attempted for user: " + username);
                return;
            }

            // Branch 2: Internal login form
            info("Detected internal login form");

            // Username
            wait.until(ExpectedConditions.visibilityOfElementLocated(loc.getinputUserName()));
            type(loc.getinputUserName(), username, "Enter User Name");

            // Move focus to the next field using TAB
            try {
                WebElement unameEl = driver.findElement(loc.getinputUserName());
                unameEl.sendKeys(Keys.TAB);
                waitForSeconds(1);
            } catch (Exception ignored) {}

            // Do NOT submit username first. Only proceed when password becomes typeable.

            // Password (internal form): try multiple strategies but DO NOT click Sign In before password is filled
            if (!tryTypePassword(password)) {
                error("Password field not found or not typeable after retries. Aborting without clicking Sign In.");
                throw new TimeoutException("Unable to locate/type into password field after multiple strategies.");
            }

            // Sign In (only after password typed)
            wait.until(ExpectedConditions.elementToBeClickable(loc.getbuttonSignIn()));
            click(loc.getbuttonSignIn(), "Sign In Button");

            if (test != null) test.pass("Internal login attempted for user: " + username);

        } catch (TimeoutException te) {
            error("Login Timeout: " + te.getMessage());
            throw te;
        }
    }

    // Optional helpers you may already have in ActionEngine:
    private void info(String msg) { if (test != null) test.info(msg); }
    private void error(String msg) { if (test != null) test.fail(msg); }

    private boolean isPresent(By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean tryTypePassword(String password) {
        // Strategy 1: direct wait and type in current context
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loc.getinputPassword()));
            type(loc.getinputPassword(), password, "Enter Password");
            return true;
        } catch (TimeoutException ignore) {}

        // Strategy 2: scan iframes and switch into the one containing password field
        try {
            if (switchToFrameContaining(loc.getinputPassword(), 5)) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(loc.getinputPassword()));
                type(loc.getinputPassword(), password, "Enter Password (framed)");
                return true;
            }
        } catch (Exception ignore) {}

        // Strategy 3: JS fallback - set value on first password-like input in the page
        try {
            driver.switchTo().defaultContent();
            Boolean set = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var doc = document;\n" +
                "function setPwd(d){\n" +
                "  var el = d.querySelector(\"input[type='password'], input[name='password'], input#password, input[placeholder*='password' i]\");\n" +
                "  if(el){ el.focus(); el.value=arguments[0]; el.dispatchEvent(new Event('input',{bubbles:true})); return true;}\n" +
                "  var frames = d.querySelectorAll('iframe,frame');\n" +
                "  for(var i=0;i<frames.length;i++){\n" +
                "    try{ var fd = frames[i].contentDocument || frames[i].contentWindow.document; if(setPwd(fd)) return true; }catch(e){}\n" +
                "  }\n" +
                "  return false;\n" +
                "}\n" +
                "return setPwd(doc);",
                password
            );
            return Boolean.TRUE.equals(set);
        } catch (Exception ignore) {}

        return false;
    }

    private boolean switchToFrameContaining(By locator, int timeoutSeconds) {
        try {
            driver.switchTo().defaultContent();
            if (isPresent(locator, timeoutSeconds)) return true;

            java.util.List<WebElement> frames = driver.findElements(By.cssSelector("iframe,frame"));
            for (WebElement frame : frames) {
                try {
                    driver.switchTo().frame(frame);
                    if (isPresent(locator, timeoutSeconds)) {
                        return true;
                    }
                    // Recurse one level deeper for nested frames
                    java.util.List<WebElement> inner = driver.findElements(By.cssSelector("iframe,frame"));
                    for (WebElement in : inner) {
                        try {
                            driver.switchTo().frame(in);
                            if (isPresent(locator, timeoutSeconds)) {
                                return true;
                            }
                        } catch (Exception ignored) {
                        } finally {
                            try { driver.switchTo().parentFrame(); } catch (Exception ignored2) {}
                        }
                    }
                } catch (Exception ignored) {
                } finally {
                    try { driver.switchTo().defaultContent(); } catch (Exception ignored2) {}
                }
            }
        } catch (Exception ignored) {}
        return false;
    }
}
