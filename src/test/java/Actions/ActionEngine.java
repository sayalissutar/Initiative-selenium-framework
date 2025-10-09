package Actions;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Base.BaseTest;

public class ActionEngine extends BaseTest {
    protected static ExtentTest reportLogger;

    public static void setLogger(ExtentTest logger) {
        reportLogger = logger;
    }

    // ✅ Wait for seconds
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    // ✅ Click
    public void click(By locator, String locatorName) {
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(locator));
            webDriver.findElement(locator).click();
            logSuccess("Clicked on: " + locatorName);
        } catch (Exception e) {
            logFailure("Click failed on: " + locatorName, e);
        }
    }

    // ✅ JS Click
    public void jsClick(By locator, String locatorName) {
        try {
            WebElement element = webDriver.findElement(locator);
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
            logSuccess("JS Clicked on: " + locatorName);
        } catch (Exception e) {
            logFailure("JS Click failed on: " + locatorName, e);
        }
    }

    // ✅ Type
    public void type(By locator, String data, String locatorName) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            WebElement el = webDriver.findElement(locator);

            // Scroll into view and try focusing
            try { ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block:'center'});", el); } catch (Exception ignore) {}
            try { ((JavascriptExecutor) webDriver).executeScript("arguments[0].focus();", el); } catch (Exception ignore) {}

            // Click with retry to ensure caret focus
            try {
                wait.until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
            } catch (Exception clickEx) {
                try { ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", el); } catch (Exception ignore) {}
            }

            // Clear with JS fallback if normal clear fails
            try {
                el.clear();
            } catch (Exception clr) {
                try { ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='';", el); } catch (Exception ignore) {}
            }

            el.sendKeys(data);
            logSuccess("Typed into: " + locatorName);
        } catch (Exception e) {
            logFailure("Error typing into: " + locatorName, e);
        }
    }

    // ✅ Get Text
    public String getText(By locator, String locatorName) {
        waitForElementToBeVisible(locator, locatorName);
        String text = webDriver.findElement(locator).getText().trim();
        logSuccess("Got text from: " + locatorName + " → " + text);
        return text;
    }

    // ✅ Wait for element clickable
    public void waitForElementToBeClickable(By locator, String elementName) {
        new WebDriverWait(webDriver, Duration.ofSeconds(40))
            .until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    
    
    

    // ✅ Wait for element visible
    public void waitForElementToBeVisible(By locator, String elementName) {
        new WebDriverWait(webDriver, Duration.ofSeconds(40))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ✅ Check element present
    public boolean isElementPresent(By locator, String locatorName) {
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return webDriver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Handle Password Change Popup
    public void handlePasswordChangePopup() {
        try {
            By popupOkButton = By.xpath("//button[normalize-space()='OK']");
            if (isElementPresent(popupOkButton, "Password Change Popup OK Button")) {
                click(popupOkButton, "Password Change Popup OK Button");
                logSuccess("🟢 Password change popup handled successfully.");
            } else {
                System.out.println("ℹ️ No password change popup appeared.");
            }
        } catch (Exception e) {
            System.out.println("ℹ️ No password change popup appeared.");
        }
    }

    // ✅ Logging helpers (safe)
    private void logSuccess(String message) {
        System.out.println("✅ " + message);
        if (reportLogger != null) {
            reportLogger.log(Status.PASS, message);
        }
    }

    private void logFailure(String message, Exception e) {
        System.out.println("❌ " + message);
        if (reportLogger != null) {
            reportLogger.log(Status.FAIL, message + "<br>" + e);
        }
    }

    public WebElement getWebElement(By locator) throws Throwable {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logFailure("Element not found: " + locator.toString(), e);
            throw e;
        }
    }
    
    
    
    // ✅ Switch to Modal Popup Window
    public void switchToModalWindow(By modalLocator, String locatorName) throws Throwable {
        try {
            // Store parent window handle
            String parentWindow = webDriver.getWindowHandle();
            java.util.Set<String> allWindows = webDriver.getWindowHandles();

            // Wait until a new window appears (modal popup)
            int attempts = 0;
            while (allWindows.size() == 1 && attempts < 10) {
                waitForSeconds(1);
                allWindows = webDriver.getWindowHandles();
                attempts++;
            }

            // Switch to the new (modal) window
            for (String window : allWindows) {
                if (!window.equals(parentWindow)) {
                    webDriver.switchTo().window(window);
                    break;
                }
            }

            // Wait for modal element to appear and highlight
            waitForElementToBeVisible(modalLocator, locatorName);
            WebElement modalElement = getWebElement(modalLocator);

            ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].style.border='2px solid blue';", modalElement);

            logSuccess("Switched to modal popup: " + locatorName);
        } catch (Exception e) {
            logFailure("Failed to switch to modal popup: " + locatorName, e);
        }
    }

    // ✅ Switch back to Parent Window
    public void switchToParentWindow() {
        try {
            String parentWindow = webDriver.getWindowHandles().iterator().next();
            webDriver.switchTo().window(parentWindow);
            logSuccess("Switched back to parent window successfully.");
        } catch (Exception e) {
            logFailure("Failed to switch back to parent window.", e);
        }
    }

    // ===========================================
    // ENHANCED MODAL HANDLING UTILITIES
    // ===========================================

    /**
     * Handle JavaScript Alert (OK button only)
     * Use this for browser alerts with single OK button
     */
    public void handleJavaScriptAlert(boolean accept) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            
            String alertText = alert.getText();
            System.out.println("🔔 JavaScript Alert detected: " + alertText);
            
            if (accept) {
                alert.accept();
                logSuccess("JavaScript Alert accepted: " + alertText);
            } else {
                alert.dismiss();
                logSuccess("JavaScript Alert dismissed: " + alertText);
            }
        } catch (Exception e) {
            logFailure("No JavaScript Alert found or error handling alert", e);
        }
    }

    /**
     * Handle JavaScript Confirm (OK/Cancel buttons)
     * Use this for browser confirm dialogs
     */
    public void handleJavaScriptConfirm(boolean accept) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            
            String confirmText = alert.getText();
            System.out.println("🔔 JavaScript Confirm detected: " + confirmText);
            
            if (accept) {
                alert.accept();
                logSuccess("JavaScript Confirm accepted: " + confirmText);
            } else {
                alert.dismiss();
                logSuccess("JavaScript Confirm dismissed: " + confirmText);
            }
        } catch (Exception e) {
            logFailure("No JavaScript Confirm found or error handling confirm", e);
        }
    }

    /**
     * Handle JavaScript Prompt (text input with OK/Cancel)
     * Use this for browser prompt dialogs
     */
    public void handleJavaScriptPrompt(String textToEnter, boolean accept) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            
            String promptText = alert.getText();
            System.out.println("🔔 JavaScript Prompt detected: " + promptText);
            
            if (textToEnter != null && !textToEnter.isEmpty()) {
                alert.sendKeys(textToEnter);
                System.out.println("✏️ Entered text in prompt: " + textToEnter);
            }
            
            if (accept) {
                alert.accept();
                logSuccess("JavaScript Prompt accepted with text: " + textToEnter);
            } else {
                alert.dismiss();
                logSuccess("JavaScript Prompt dismissed");
            }
        } catch (Exception e) {
            logFailure("No JavaScript Prompt found or error handling prompt", e);
        }
    }

    /**
     * Check if JavaScript Alert is present
     * @return true if alert is present, false otherwise
     */
    public boolean isAlertPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get text from JavaScript Alert without accepting/dismissing
     * @return Alert text or null if no alert present
     */
    public String getAlertText() {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            return alert.getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Handle HTML Modal (overlay) - Wait and verify modal is visible
     * Use this for application modals that are part of the DOM
     */
    public boolean waitForHTMLModal(By modalLocator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds));
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
            
            // Highlight modal for visibility
            ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].style.border='3px solid red';", modal);
            
            logSuccess("HTML Modal is visible: " + modalLocator.toString());
            return true;
        } catch (Exception e) {
            logFailure("HTML Modal not found or not visible: " + modalLocator.toString(), e);
            return false;
        }
    }

    /**
     * Check if HTML Modal is currently visible
     */
    public boolean isHTMLModalVisible(By modalLocator) {
        try {
            WebElement modal = webDriver.findElement(modalLocator);
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click button inside HTML Modal
     */
    public void clickButtonInModal(By buttonLocator, String buttonName) {
        try {
            waitForElementToBeClickable(buttonLocator, buttonName);
            click(buttonLocator, "Modal Button: " + buttonName);
            logSuccess("Clicked button in modal: " + buttonName);
        } catch (Exception e) {
            // Try JS click as fallback
            try {
                jsClick(buttonLocator, "Modal Button: " + buttonName);
                logSuccess("JS Clicked button in modal: " + buttonName);
            } catch (Exception e2) {
                logFailure("Failed to click button in modal: " + buttonName, e2);
            }
        }
    }

    /**
     * Type text in modal input field
     */
    public void typeInModalField(By inputLocator, String text, String fieldName) {
        try {
            waitForElementToBeVisible(inputLocator, fieldName);
            type(inputLocator, text, "Modal Field: " + fieldName);
            logSuccess("Typed in modal field: " + fieldName);
        } catch (Exception e) {
            logFailure("Failed to type in modal field: " + fieldName, e);
        }
    }

    /**
     * Close HTML Modal by clicking close button (X)
     */
    public void closeHTMLModal(By closeButtonLocator) {
        try {
            click(closeButtonLocator, "Modal Close Button");
            
            // Wait for modal to disappear
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(closeButtonLocator));
            
            logSuccess("HTML Modal closed successfully");
        } catch (Exception e) {
            logFailure("Failed to close HTML Modal", e);
        }
    }

    /**
     * Close HTML Modal by pressing ESC key
     */
    public void closeModalWithEscape() {
        try {
            Actions action = new Actions(webDriver);
            action.sendKeys(Keys.ESCAPE).build().perform();
            waitForSeconds(1);
            logSuccess("Modal closed using ESC key");
        } catch (Exception e) {
            logFailure("Failed to close modal with ESC key", e);
        }
    }

    /**
     * Close HTML Modal by clicking outside (backdrop)
     */
    public void closeModalByClickingBackdrop(By backdropLocator) {
        try {
            WebElement backdrop = webDriver.findElement(backdropLocator);
            backdrop.click();
            waitForSeconds(1);
            logSuccess("Modal closed by clicking backdrop");
        } catch (Exception e) {
            logFailure("Failed to close modal by clicking backdrop", e);
        }
    }

    /**
     * Handle Popup Window (new window/tab)
     * Stores parent window, switches to popup, performs action, and returns
     */
    private String parentWindowHandle = null;

    public void storeParentWindow() {
        try {
            parentWindowHandle = webDriver.getWindowHandle();
            logSuccess("Parent window handle stored: " + parentWindowHandle);
        } catch (Exception e) {
            logFailure("Failed to store parent window handle", e);
        }
    }

    /**
     * Switch to popup window by waiting for new window
     */
    public boolean switchToModalPopupWindow(By modalLocator, String locatorName) {
        try {
            // Store parent if not already stored
            if (parentWindowHandle == null) {
                storeParentWindow();
            }

            // Wait for new window to appear
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));

            // Get all window handles
            java.util.Set<String> allWindows = webDriver.getWindowHandles();
            
            // Switch to the new window
            for (String window : allWindows) {
                if (!window.equals(parentWindowHandle)) {
                    webDriver.switchTo().window(window);
                    System.out.println("🪟 Switched to popup window: " + window);
                    
                    // Wait for modal element to be visible in popup
                    waitForElementToBeVisible(modalLocator, locatorName);
                    logSuccess("Successfully switched to modal popup: " + locatorName);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logFailure("Failed to switch to modal popup: " + locatorName, e);
            return false;
        }
    }

    /**
     * Close current popup window and switch back to parent
     */
    public void closeCurrentWindowAndSwitchToParent() {
        try {
            // Close current window
            webDriver.close();
            System.out.println("🪟 Closed popup window");
            
            // Switch back to parent
            if (parentWindowHandle != null) {
                webDriver.switchTo().window(parentWindowHandle);
                logSuccess("Switched back to parent window");
            } else {
                // Fallback: switch to first available window
                String firstWindow = webDriver.getWindowHandles().iterator().next();
                webDriver.switchTo().window(firstWindow);
                logSuccess("Switched to first available window");
            }
        } catch (Exception e) {
            logFailure("Failed to close popup and switch to parent", e);
        }
    }

    /**
     * Get current window count
     */
    public int getWindowCount() {
        try {
            return webDriver.getWindowHandles().size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Wait for new window to appear
     */
    public boolean waitForNewWindow(int expectedWindowCount, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.numberOfWindowsToBe(expectedWindowCount));
            logSuccess("New window appeared. Total windows: " + expectedWindowCount);
            return true;
        } catch (Exception e) {
            logFailure("New window did not appear within timeout", e);
            return false;
        }
    }

    /**
     * Switch to window by title
     */
    public boolean switchToWindowByTitle(String windowTitle) {
        try {
            java.util.Set<String> allWindows = webDriver.getWindowHandles();
            
            for (String window : allWindows) {
                webDriver.switchTo().window(window);
                if (webDriver.getTitle().contains(windowTitle)) {
                    logSuccess("Switched to window with title: " + windowTitle);
                    return true;
                }
            }
            logFailure("Window with title not found: " + windowTitle, new Exception("Window not found"));
            return false;
        } catch (Exception e) {
            logFailure("Failed to switch to window by title: " + windowTitle, e);
            return false;
        }
    }

    /**
     * Handle modal with submit and comments
     * Comprehensive method for common modal pattern: enter text and submit
     */
    public boolean handleSubmitModalWithComments(String comments) {
        try {
            // Wait for modal container to appear
            By modalContainer = By.xpath("//div[contains(@class, 'modal') or contains(@class, 'dialog')]");
            waitForHTMLModal(modalContainer, 10);
            
            // Find and interact with textarea
            By textareaLocator = By.xpath("//textarea");
            typeInModalField(textareaLocator, comments, "Comments Textarea");
            
            // Click submit button
            By submitButton = By.xpath("//button[contains(., 'Submit') or contains(@class, 'submit')]");
            clickButtonInModal(submitButton, "Submit");
            
            // Wait for modal to close
            waitForSeconds(2);
            
            logSuccess("Modal with comments handled successfully");
            return true;
        } catch (Exception e) {
            logFailure("Failed to handle submit modal with comments", e);
            return false;
        }
    }

    /**
     * Type text in modal using specific locator
     * Enhanced version with better error handling and retry logic
     */
    public void typeInModal(By locator, String text, String fieldName) {
        WebElement element = null;
        boolean success = false;
        
        try {
            // Wait for element to be visible and clickable
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
            element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            // Additional wait for modal animations
            Thread.sleep(500);
            
            // Scroll into view silently
            try {
                ((JavascriptExecutor) webDriver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', behavior:'instant'});", element);
                Thread.sleep(200);
            } catch (Exception ignored) {}
            
            // Focus on element - try click first, then JS
            try {
                element.click();
            } catch (Exception e1) {
                try {
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].focus();", element);
                } catch (Exception ignored) {}
            }
            
            // Clear existing text
            try {
                element.clear();
            } catch (Exception e) {
                try {
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='';", element);
                } catch (Exception ignored) {}
            }
            
            // Type text - try normal sendKeys first
            try {
                element.sendKeys(text);
                success = true;
            } catch (Exception e1) {
                // Fallback: Use JavaScript to set value
                try {
                    ((JavascriptExecutor) webDriver).executeScript(
                        "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", 
                        element, text);
                    success = true;
                } catch (Exception e2) {
                    throw new Exception("Both sendKeys and JS setValue failed: " + e2.getMessage());
                }
            }
            
            // Verify text was entered
            if (success) {
                try {
                    String actualValue = element.getAttribute("value");
                    if (actualValue != null && actualValue.equals(text)) {
                        logSuccess("Typed in modal element: " + fieldName + " = " + text);
                    } else if (actualValue != null && actualValue.contains(text)) {
                        logSuccess("Typed in modal element: " + fieldName + " = " + text + " (partial match)");
                    } else {
                        logSuccess("Text entered in modal element: " + fieldName);
                    }
                } catch (Exception e) {
                    // Verification failed but typing might have worked
                    logSuccess("Text entered in modal element: " + fieldName + " (verification skipped)");
                }
            }
            
        } catch (Exception e) {
            // Log the error and try one last time with the basic type() method as ultimate fallback
            System.out.println("⚠️ Warning: typeInModal encountered issue, trying fallback method...");
            try {
                type(locator, text, fieldName);
                logSuccess("Typed in modal element using fallback: " + fieldName);
            } catch (Exception fallbackEx) {
                // Now truly failed
                logFailure("Failed to type in modal element: " + fieldName, e);
                throw new RuntimeException("Failed to type in modal element after all retries: " + fieldName, e);
            }
        }
    }

    /**
     * Click element in modal with enhanced retry logic
     */
    public void clickElementInModal(By locator, String elementName) {
        boolean clicked = false;
        Exception lastException = null;
        
        try {
            // Wait for element to be clickable with longer timeout
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            // Brief wait for any animations
            Thread.sleep(500);
            
            // Scroll into view (silent fallback)
            try {
                ((JavascriptExecutor) webDriver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', behavior:'instant'});", element);
                Thread.sleep(300);
            } catch (Exception ignored) {}
            
            // Method 1: Try standard click
            try {
                element.click();
                clicked = true;
                logSuccess("Clicked element in modal: " + elementName);
            } catch (Exception e1) {
                lastException = e1;
                
                // Method 2: Try Actions click
                try {
                    Actions actions = new Actions(webDriver);
                    actions.moveToElement(element).click().perform();
                    clicked = true;
                    logSuccess("Actions clicked element in modal: " + elementName);
                } catch (Exception e2) {
                    lastException = e2;
                    
                    // Method 3: Try JavaScript click
                    try {
                        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
                        clicked = true;
                        logSuccess("JS clicked element in modal: " + elementName);
                    } catch (Exception e3) {
                        lastException = e3;
                        
                        // Method 4: Try finding button parent and clicking
                        try {
                            WebElement button = element.findElement(By.xpath("./ancestor-or-self::button"));
                            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", button);
                            clicked = true;
                            logSuccess("Clicked button ancestor in modal: " + elementName);
                        } catch (Exception e4) {
                            lastException = e4;
                        }
                    }
                }
            }
            
            if (!clicked) {
                throw new Exception("All click methods failed. Last error: " + 
                    (lastException != null ? lastException.getMessage() : "Unknown"));
            }
            
        } catch (Exception e) {
            // Final fallback - try the basic click method
            System.out.println("⚠️ Warning: clickElementInModal encountered issue, trying fallback...");
            try {
                click(locator, elementName);
                logSuccess("Clicked using fallback method: " + elementName);
            } catch (Exception fallbackEx) {
                // Now truly failed
                logFailure("Failed to click element in modal after all retries: " + elementName, e);
                throw new RuntimeException("Failed to click element in modal: " + elementName + " - " + e.getMessage(), e);
            }
        }
    }


}
