package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import Base.BaseTest;
import java.util.List;
import java.util.Set;

/**
 * Diagnostic test to understand SSO popup behavior
 * Run this test and share the console output
 */
public class SSODiagnosticTest extends BaseTest {

    @Test
    public void diagnoseSSOFlow() {
        // Disable automatic login
        this.useLoginHelper = false;
        
        reportLogger.info("===== SSO DIAGNOSTIC TEST STARTED =====");
        
        try {
            // STEP 1: Initial page state
            System.out.println("\n========================================");
            System.out.println("STEP 1: INITIAL PAGE STATE");
            System.out.println("========================================");
            System.out.println("URL: " + webDriver.getCurrentUrl());
            System.out.println("Title: " + webDriver.getTitle());
            System.out.println("Number of windows: " + webDriver.getWindowHandles().size());
            
            String originalWindow = webDriver.getWindowHandle();
            
            // STEP 2: Find Microsoft button
            System.out.println("\n========================================");
            System.out.println("STEP 2: LOOKING FOR MICROSOFT BUTTON");
            System.out.println("========================================");
            
            By microsoftBtnLocator = By.cssSelector("img.ms-2");
            List<WebElement> microsoftButtons = webDriver.findElements(microsoftBtnLocator);
            System.out.println("Microsoft buttons found with 'img.ms-2': " + microsoftButtons.size());
            
            if (microsoftButtons.isEmpty()) {
                // Try alternative locators
                System.out.println("\nTrying alternative Microsoft button locators:");
                
                By[] altLocators = {
                    By.xpath("//button[contains(text(),'Microsoft')]"),
                    By.xpath("//a[contains(text(),'Microsoft')]"),
                    By.xpath("//*[contains(@class,'microsoft')]"),
                    By.xpath("//*[contains(@id,'microsoft')]"),
                    By.cssSelector("button[class*='microsoft']"),
                    By.cssSelector("a[class*='microsoft']")
                };
                
                for (By locator : altLocators) {
                    List<WebElement> elements = webDriver.findElements(locator);
                    System.out.println("  " + locator + " found: " + elements.size());
                    if (!elements.isEmpty()) {
                        microsoftBtnLocator = locator;
                        microsoftButtons = elements;
                        break;
                    }
                }
            }
            
            if (microsoftButtons.isEmpty()) {
                System.out.println("\n❌ NO MICROSOFT BUTTON FOUND!");
                System.out.println("Here are all buttons on the page:");
                List<WebElement> allButtons = webDriver.findElements(By.tagName("button"));
                for (int i = 0; i < Math.min(allButtons.size(), 10); i++) {
                    WebElement btn = allButtons.get(i);
                    System.out.println("  Button " + i + ": " + btn.getText() + " | class=" + btn.getAttribute("class"));
                }
                return;
            }
            
            // STEP 3: Click Microsoft button
            System.out.println("\n========================================");
            System.out.println("STEP 3: CLICKING MICROSOFT BUTTON");
            System.out.println("========================================");
            
            WebElement microsoftBtn = microsoftButtons.get(0);
            System.out.println("Button text: " + microsoftBtn.getText());
            System.out.println("Button class: " + microsoftBtn.getAttribute("class"));
            
            microsoftBtn.click();
            System.out.println("✅ Microsoft button clicked");
            
            Thread.sleep(3000); // Wait for popup/redirect
            
            // STEP 4: Check page state after click
            System.out.println("\n========================================");
            System.out.println("STEP 4: PAGE STATE AFTER CLICK");
            System.out.println("========================================");
            
            Set<String> allWindows = webDriver.getWindowHandles();
            System.out.println("Number of windows now: " + allWindows.size());
            System.out.println("Current URL: " + webDriver.getCurrentUrl());
            System.out.println("Current Title: " + webDriver.getTitle());
            
            // STEP 5: Check for popup/new window
            System.out.println("\n========================================");
            System.out.println("STEP 5: CHECKING FOR POPUP/NEW WINDOW");
            System.out.println("========================================");
            
            if (allWindows.size() > 1) {
                System.out.println("✅ NEW WINDOW DETECTED - Switching to it");
                for (String windowHandle : allWindows) {
                    if (!windowHandle.equals(originalWindow)) {
                        webDriver.switchTo().window(windowHandle);
                        System.out.println("Switched to new window");
                        break;
                    }
                }
                Thread.sleep(2000);
            } else {
                System.out.println("ℹ️ No new window - stayed in same window");
            }
            
            System.out.println("\nAfter switching:");
            System.out.println("Current URL: " + webDriver.getCurrentUrl());
            System.out.println("Current Title: " + webDriver.getTitle());
            
            // STEP 6: Search for email field with ALL possible locators
            System.out.println("\n========================================");
            System.out.println("STEP 6: SEARCHING FOR EMAIL FIELD");
            System.out.println("========================================");
            
            By[] emailLocators = {
                By.id("i0116"),
                By.name("loginfmt"),
                By.xpath("//input[@type='email']"),
                By.xpath("//input[@name='loginfmt']"),
                By.cssSelector("input[type='email']"),
                By.cssSelector("input[name='loginfmt']"),
                By.xpath("//input[contains(@placeholder,'email')]"),
                By.xpath("//input[contains(@placeholder,'Email')]"),
                By.className("form-control"),
                By.xpath("//input[@id='i0116']")
            };
            
            System.out.println("Checking main page:");
            boolean foundInMain = false;
            By workingLocator = null;
            
            for (By locator : emailLocators) {
                List<WebElement> elements = webDriver.findElements(locator);
                System.out.println("  " + locator + " found: " + elements.size());
                if (!elements.isEmpty() && !foundInMain) {
                    foundInMain = true;
                    workingLocator = locator;
                    System.out.println("  ✅ THIS ONE WORKS!");
                }
            }
            
            // STEP 7: Check iframes
            System.out.println("\n========================================");
            System.out.println("STEP 7: CHECKING IFRAMES");
            System.out.println("========================================");
            
            List<WebElement> iframes = webDriver.findElements(By.tagName("iframe"));
            System.out.println("Number of iframes: " + iframes.size());
            
            if (!foundInMain && iframes.size() > 0) {
                for (int i = 0; i < iframes.size(); i++) {
                    try {
                        webDriver.switchTo().frame(i);
                        System.out.println("\nChecking iframe " + i + ":");
                        
                        for (By locator : emailLocators) {
                            List<WebElement> elements = webDriver.findElements(locator);
                            if (!elements.isEmpty()) {
                                System.out.println("  ✅ " + locator + " found: " + elements.size());
                                workingLocator = locator;
                            }
                        }
                        
                        webDriver.switchTo().defaultContent();
                    } catch (Exception e) {
                        System.out.println("  Error checking iframe " + i);
                        webDriver.switchTo().defaultContent();
                    }
                }
            }
            
            // STEP 8: Summary
            System.out.println("\n========================================");
            System.out.println("DIAGNOSTIC SUMMARY");
            System.out.println("========================================");
            
            if (workingLocator != null) {
                System.out.println("✅ EMAIL FIELD FOUND!");
                System.out.println("Working locator: " + workingLocator);
                System.out.println("\nAdd this to LoginPageLocators.java:");
                System.out.println("private By emailField = " + formatLocator(workingLocator) + ";");
            } else {
                System.out.println("❌ EMAIL FIELD NOT FOUND");
                System.out.println("\nPage source contains 'email': " + webDriver.getPageSource().contains("email"));
                System.out.println("Page source contains 'login': " + webDriver.getPageSource().contains("login"));
                System.out.println("Page source contains 'i0116': " + webDriver.getPageSource().contains("i0116"));
                
                System.out.println("\nAll input fields on page:");
                List<WebElement> allInputs = webDriver.findElements(By.tagName("input"));
                for (int i = 0; i < Math.min(allInputs.size(), 15); i++) {
                    WebElement input = allInputs.get(i);
                    System.out.println("  Input " + i + ":");
                    System.out.println("    type=" + input.getAttribute("type"));
                    System.out.println("    name=" + input.getAttribute("name"));
                    System.out.println("    id=" + input.getAttribute("id"));
                    System.out.println("    class=" + input.getAttribute("class"));
                    System.out.println("    placeholder=" + input.getAttribute("placeholder"));
                }
            }
            
            System.out.println("\n========================================");
            System.out.println("DIAGNOSTIC TEST COMPLETED");
            System.out.println("========================================");
            
            reportLogger.pass("Diagnostic test completed - check console output");
            
        } catch (Exception e) {
            System.out.println("\n❌ ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            reportLogger.fail("Diagnostic test failed: " + e.getMessage());
        }
    }
    
    private String formatLocator(By locator) {
        String locStr = locator.toString();
        if (locStr.startsWith("By.id: ")) {
            return "By.id(\"" + locStr.substring(7) + "\")";
        } else if (locStr.startsWith("By.name: ")) {
            return "By.name(\"" + locStr.substring(9) + "\")";
        } else if (locStr.startsWith("By.xpath: ")) {
            return "By.xpath(\"" + locStr.substring(10) + "\")";
        } else if (locStr.startsWith("By.cssSelector: ")) {
            return "By.cssSelector(\"" + locStr.substring(16) + "\")";
        }
        return locator.toString();
    }
    
    @Override
    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        // Override to disable automatic login
        reportLogger = extent.createTest(method.getName());
        log.info("===== Starting Test: " + method.getName() + " =====");

        String browser = config.getProperty("browser", "edge").toLowerCase();
        log.info("Launching browser: " + browser);

        switch (browser) {
            case "chrome":
                io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
                org.openqa.selenium.chrome.ChromeOptions chromeOptions = new org.openqa.selenium.chrome.ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                webDriver = new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);
                break;

            case "firefox":
                io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
                org.openqa.selenium.firefox.FirefoxOptions firefoxOptions = new org.openqa.selenium.firefox.FirefoxOptions();
                webDriver = new org.openqa.selenium.firefox.FirefoxDriver(firefoxOptions);
                break;

            case "edge":
            default:
                try {
                    io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
                } catch (Exception e) {
                    System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
                }
                
                org.openqa.selenium.edge.EdgeOptions edgeOptions = new org.openqa.selenium.edge.EdgeOptions();
                edgeOptions.addArguments("--disable-save-password-bubble");
                edgeOptions.addArguments("--disable-notifications");
                webDriver = new org.openqa.selenium.edge.EdgeDriver(edgeOptions);
                break;
        }

        webDriver.manage().window().maximize();
        String appUrl = config.getProperty("url", "URLInconfig");
        webDriver.get(appUrl);
        
        // Do NOT perform login - this is a diagnostic test
    }
}







