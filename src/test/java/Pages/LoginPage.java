package Pages;

import Actions.ActionEngine;
import Locators.LoginPageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;
import java.util.Set;

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

    /**
     * Auto-detect login type and perform login (legacy method)
     * @deprecated Use performFormLogin() or performSSOLogin() directly
     */
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
                performSSOLogin(username, password);
                return;
            }

            // Branch 2: Internal login form
            info("Detected internal login form");
            performFormLogin(username, password);

        } catch (TimeoutException te) {
            error("Login Timeout: " + te.getMessage());
            throw te;
        }
    }
    
    /**
     * Performs Form-based login with username and password
     * @param username Username for form login
     * @param password Password for form login
     */
    public void performFormLogin(String username, String password) {
        try {
            info("🔐 Performing FORM login with username: " + username);
            
            // Ensure page is ready
            wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.readyState").equals("complete"));
            waitForSeconds(1);

            // Username
            wait.until(ExpectedConditions.visibilityOfElementLocated(loc.getinputUserName()));
            type(loc.getinputUserName(), username, "Enter User Name");

            // Move focus to the next field using TAB
            try {
                WebElement unameEl = driver.findElement(loc.getinputUserName());
                unameEl.sendKeys(Keys.TAB);
                waitForSeconds(1);
            } catch (Exception ignored) {}

            // Password (internal form): try multiple strategies but DO NOT click Sign In before password is filled
            if (!tryTypePassword(password)) {
                error("Password field not found or not typeable after retries. Aborting without clicking Sign In.");
                throw new TimeoutException("Unable to locate/type into password field after multiple strategies.");
            }

            // Sign In (only after password typed)
            wait.until(ExpectedConditions.elementToBeClickable(loc.getbuttonSignIn()));
            click(loc.getbuttonSignIn(), "Sign In Button");

            if (test != null) test.pass("✅ FORM login completed for user: " + username);

        } catch (TimeoutException te) {
            error("FORM Login Timeout: " + te.getMessage());
            throw te;
        }
    }
    
    /**
     * Performs SSO login (Microsoft) with email and password
     * @param email Email address for SSO login
     * @param password Password for SSO login
     */
    public void performSSOLogin(String email, String password) {
        try {
            info("🔐 Performing SSO login with email: " + email);
            
            // Ensure page is ready
            wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.readyState").equals("complete"));
            waitForSeconds(1);


            String originalWindow = driver.getWindowHandle();
            String currentUrl = driver.getCurrentUrl();
            info("Current URL before Microsoft click: " + currentUrl);

            // STEP 1: Click Microsoft SSO button if present on landing page
            boolean microsoftButtonClicked = false;
            try {
                if (isPresent(loc.getMicrosoftLoginBtn(), 5)) {
                    info("Microsoft SSO button found - clicking it");
                    wait.until(ExpectedConditions.elementToBeClickable(loc.getMicrosoftLoginBtn()));
                    click(loc.getMicrosoftLoginBtn(), "Microsoft SSO Button");
                    microsoftButtonClicked = true;
                    info("Microsoft SSO button clicked successfully");
                    waitForSeconds(3);
                } else {
                    info("Microsoft SSO button not found on page");
                }
            } catch (Exception e) {
                info("Exception while clicking Microsoft button: " + e.getMessage());
            }

            // STEP 2: Check current state after button click
            info("Number of windows open: " + driver.getWindowHandles().size());
            currentUrl = driver.getCurrentUrl();
            info("Current URL after Microsoft click: " + currentUrl);

            // STEP 3: Handle popup/new window or URL redirect
            // Check for new window/popup
            if (driver.getWindowHandles().size() > 1) {
                info("Detected multiple windows - attempting to switch to popup");
                for (String windowHandle : driver.getWindowHandles()) {
                    if (!windowHandle.equals(originalWindow)) {
                        driver.switchTo().window(windowHandle);
                        info("Switched to new window. URL: " + driver.getCurrentUrl());
                        break;
                    }
                }
            } else if (microsoftButtonClicked) {
                // URL might have changed in same window
                info("Same window - checking for URL change or redirect");
                try {
                    // Wait for URL to change to Microsoft domain
                    wait.until(d -> {
                        String url = d.getCurrentUrl().toLowerCase();
                        return url.contains("microsoft") || url.contains("login.microsoftonline");
                    });
                    info("URL redirected to Microsoft SSO: " + driver.getCurrentUrl());
                } catch (TimeoutException te) {
                    info("URL did not redirect to Microsoft domain");
                }
            }

            // Wait for page to fully load
            wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.readyState").equals("complete"));
            waitForSeconds(2);

            // STEP 4: Comprehensive search for email field
            info("Starting comprehensive search for email field...");
            boolean emailFieldFound = false;
            
            // Strategy 1: Check main page with multiple locators
            info("Strategy 1: Checking main page context");
            By[] emailLocators = {
                loc.getEmailField(),
                By.id("i0116"),
                By.name("loginfmt"),
                By.xpath("//input[@id='i0116']"),
                By.xpath("//input[@name='loginfmt']"),
                By.cssSelector("input[type='email']")
            };
            
            for (By locator : emailLocators) {
                try {
                    if (isPresent(locator, 3)) {
                        info("✅ Email field found with locator: " + locator.toString());
                        emailFieldFound = true;
                        // Update to use the working locator temporarily
                        break;
                    }
                } catch (Exception e) {
                    // Continue to next locator
                }
            }

            // Strategy 2: Check iframes if not found in main context
            if (!emailFieldFound) {
                info("Strategy 2: Searching in iframes...");
                driver.switchTo().defaultContent();
                java.util.List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                info("Found " + iframes.size() + " iframes on page");
                
                for (int i = 0; i < iframes.size(); i++) {
                    try {
                        driver.switchTo().frame(i);
                        info("Checking iframe " + i);
                        
                        for (By locator : emailLocators) {
                            if (isPresent(locator, 2)) {
                                info("✅ Email field found in iframe " + i + " with locator: " + locator.toString());
                                emailFieldFound = true;
                                break;
                            }
                        }
                        
                        if (emailFieldFound) break;
                        driver.switchTo().defaultContent();
                    } catch (Exception e) {
                        info("Error checking iframe " + i + ": " + e.getMessage());
                        driver.switchTo().defaultContent();
                    }
                }
            }

            // Strategy 3: Wait longer with explicit wait
            if (!emailFieldFound) {
                info("Strategy 3: Extended wait for email field...");
                driver.switchTo().defaultContent();
                try {
                    WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
                    extendedWait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.id("i0116")),
                        ExpectedConditions.presenceOfElementLocated(By.name("loginfmt")),
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='email']"))
                    ));
                    emailFieldFound = true;
                    info("✅ Email field appeared after extended wait");
                } catch (TimeoutException e) {
                    info("Email field not found even after extended wait");
                }
            }

            if (!emailFieldFound) {
                // Print diagnostic info
                error("❌ EMAIL FIELD NOT FOUND - Diagnostic Info:");
                error("Current URL: " + driver.getCurrentUrl());
                error("Page Title: " + driver.getTitle());
                error("Number of windows: " + driver.getWindowHandles().size());
                
                // Take screenshot for debugging
                try {
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains("email") || pageSource.contains("login")) {
                        error("Page contains 'email' or 'login' keywords - check locators");
                    }
                } catch (Exception ignored) {}
                
                throw new TimeoutException("Email field not located after trying all strategies");
            }

            // STEP 5: Handle "Use another account" option
            try {
                if (isPresent(loc.useAnotherAccountOption(), 3)) {
                    info("Found 'Use another account' option - clicking it");
                    WebElement another = driver.findElement(loc.useAnotherAccountOption());
                    if (another.isDisplayed()) {
                        another.click();
                        waitForSeconds(2);
                        info("Clicked 'Use another account'");
                    }
                }
            } catch (Exception e) {
                info("No 'Use another account' option found or error: " + e.getMessage());
            }

            // STEP 6: Enter Email - try multiple locators
            info("Entering email: " + email);
            WebElement emailField = null;
            
            // Try the working locators in order of reliability
            By[] workingEmailLocators = {
                By.name("loginfmt"),
                By.id("i0116"),
                loc.getEmailField()
            };
            
            for (By locator : workingEmailLocators) {
                try {
                    emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    info("Email field found with: " + locator);
                    break;
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (emailField == null) {
                throw new TimeoutException("Could not find email field with any locator");
            }
            
            emailField.clear();
            emailField.sendKeys(email);
            info("Email entered successfully");
            
            waitForSeconds(1);
            
            // Click Next button - try multiple locators
            By[] nextButtonLocators = {
                By.id("idSIButton9"),
                By.xpath("//input[@type='submit' and @value='Next']"),
                loc.getConfirmbutton()
            };
            
            WebElement nextButton = null;
            for (By locator : nextButtonLocators) {
                try {
                    nextButton = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    info("Next button found with: " + locator);
                    nextButton.click();
                    info("Clicked Next button");
                    break;
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (nextButton == null) {
                throw new TimeoutException("Could not find Next button");
            }

            // STEP 7: Enter Password
            info("Waiting for password field...");
            
            By[] passwordLocators = {
                By.name("passwd"),
                By.id("i0118"),
                loc.getPasswordField()
            };
            
            WebElement passwordField = null;
            for (By locator : passwordLocators) {
                try {
                    passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    info("Password field found with: " + locator);
                    break;
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (passwordField == null) {
                throw new TimeoutException("Could not find password field");
            }
            
            passwordField.clear();
            passwordField.sendKeys(password);
            info("Password entered successfully");
            
            waitForSeconds(1);
            
            // Click Sign in button
            By[] signInButtonLocators = {
                By.id("idSIButton9"),
                By.xpath("//input[@type='submit' and @value='Sign in']"),
                loc.getConfirmbutton()
            };
            
            WebElement signInButton = null;
            for (By locator : signInButtonLocators) {
                try {
                    signInButton = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    info("Sign in button found with: " + locator);
                    signInButton.click();
                    info("Clicked Sign in button");
                    break;
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (signInButton == null) {
                throw new TimeoutException("Could not find Sign in button");
            }

            // STEP 8: Handle "Stay signed in?" prompt
            try {
                waitForSeconds(2);
                if (isPresent(loc.getConfirmbutton(), 5)) {
                    info("Found 'Stay signed in?' prompt - clicking Yes");
                    click(loc.getConfirmbutton(), "Stay signed in - Yes");
                }
            } catch (Exception e) {
                info("No 'Stay signed in?' prompt or already handled");
            }

            // STEP 9: Switch back to original window if needed
            try {
                waitForSeconds(3);
                info("Checking window handles...");
                Set<String> allWindows = driver.getWindowHandles();
                info("Total windows available: " + allWindows.size());
                
                if (allWindows.size() > 1) {
                    // Multiple windows - need to switch to correct one
                    if (allWindows.contains(originalWindow)) {
                        driver.switchTo().window(originalWindow);
                        info("Switched back to original window");
                    } else {
                        // Original window closed, switch to first available
                        String firstAvailable = allWindows.iterator().next();
                        driver.switchTo().window(firstAvailable);
                        info("Original window not found, switched to first available window");
                    }
                } else if (allWindows.size() == 1) {
                    // Only one window, make sure we're on it
                    String onlyWindow = allWindows.iterator().next();
                    try {
                        driver.getCurrentUrl(); // Test if we're on a valid window
                    } catch (Exception windowEx) {
                        // Not on valid window, switch to the only one available
                        driver.switchTo().window(onlyWindow);
                        info("Switched to the only available window");
                    }
                } else {
                    info("Warning: No windows available!");
                }
            } catch (Exception e) {
                info("Window switch error: " + e.getMessage());
                // Try to recover by switching to any available window
                try {
                    if (!driver.getWindowHandles().isEmpty()) {
                        driver.switchTo().window(driver.getWindowHandles().iterator().next());
                        info("Recovered by switching to first available window");
                    }
                } catch (Exception recoverEx) {
                    info("Could not recover window: " + recoverEx.getMessage());
                }
            }

            // Wait for final page load
            wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.readyState").equals("complete"));
            
            info("✅ SSO login completed successfully for: " + email);
            if (test != null) test.pass("✅ SSO login completed for email: " + email);

        } catch (Exception e) {
            error("❌ SSO Login Failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            
            // Try to get current URL, but handle if window is lost
            try {
                // Check if any windows are available
                if (driver.getWindowHandles() != null && !driver.getWindowHandles().isEmpty()) {
                    // Try to switch to a valid window if current is lost
                    try {
                        driver.getCurrentUrl(); // Test if current window is valid
                        error("Current URL at failure: " + driver.getCurrentUrl());
                    } catch (Exception urlEx) {
                        // Current window lost, try to switch to first available
                        String firstWindow = driver.getWindowHandles().iterator().next();
                        driver.switchTo().window(firstWindow);
                        error("Current URL at failure (after window switch): " + driver.getCurrentUrl());
                    }
                } else {
                    error("No browser windows available - session may have been closed");
                }
            } catch (Exception urlException) {
                error("Could not retrieve URL - window session lost: " + urlException.getMessage());
            }
            
            throw e;
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

