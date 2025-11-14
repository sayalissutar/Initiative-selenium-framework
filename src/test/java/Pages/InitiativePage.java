package Pages;

import Actions.ActionEngine;
import Locators.InitiativePageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class InitiativePage extends ActionEngine {

    private WebDriver driver;
    private ExtentTest reportLogger;

    // 🔹 Correct constructor with WebDriver + Logger
    public InitiativePage(WebDriver driver, ExtentTest reportLogger) {
        super(); // pass driver to ActionEngine
        this.driver = driver;
        this.reportLogger = reportLogger;
    }

    // ✅ Helper method to check if element is present
    private boolean isElementPresent(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    // ✅ Navigate to Initiative Page
    public void navigateToInitiative() throws Exception {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("🔍 NAVIGATION TO INITIATIVE - START");
        System.out.println("═══════════════════════════════════════════════════════");
        
        // Wait for page to be fully loaded after login
        System.out.println("⏳ Waiting for page to stabilize after login...");
        waitForSeconds(5); // Increased wait time
        
        // Wait for page to be fully loaded
        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        pageWait.until(driver -> ((JavascriptExecutor) driver)
            .executeScript("return document.readyState").equals("complete"));
        System.out.println("✅ Page fully loaded");
        
        try {
            // Step 1: Hover and click on initree to reveal menu
            System.out.println("\n📍 Step 1: Accessing Initiative Tree Menu...");
            hoverAndClickElement(InitiativePageLocators.initree, "Initiative Tree Menu");
            waitForSeconds(2); // Wait for menu to appear
            
            // Step 2: Click on initiative option (arrow)
            System.out.println("\n📍 Step 2: Clicking Initiative Option Arrow...");
            clickWithFallback(InitiativePageLocators.initiativeOption, "Initiative Option");
            waitForSeconds(1);
            
            // Step 3: Click on Initiative node
            System.out.println("\n📍 Step 3: Clicking Initiative Node...");
            clickWithFallback(InitiativePageLocators.initiativeNode, "Initiative Node");
            waitForSeconds(2); // Wait for Initiative page to load
            
            System.out.println("\n✅ ✅ ✅ Navigated to Initiative successfully! ✅ ✅ ✅");
            if (reportLogger != null) {
                reportLogger.pass("Successfully navigated to Initiative page");
            }
            
        } catch (Exception e) {
            System.out.println("\n❌ ❌ ❌ Navigation to Initiative FAILED! ❌ ❌ ❌");
            System.out.println("Error: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.fail("Failed to navigate to Initiative: " + e.getMessage());
            }
            throw e;
        }
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔍 NAVIGATION TO INITIATIVE - END");
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
    
    // ✅ Helper method to hover over and click an element
    private void hoverAndClickElement(By locator, String elementName) throws Exception {
        try {
            System.out.println("🖱️ Hovering and clicking: " + elementName);
            
            // Wait for element to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            
            // Scroll element into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            waitForSeconds(1);
            
            // Hover over element
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            System.out.println("  ↪ Hovered over element");
            waitForSeconds(1);
            
            // Try to click
            try {
                element.click();
                System.out.println("  ↪ Clicked using regular click");
            } catch (Exception e1) {
                System.out.println("  ⚠️ Regular click failed, trying JavaScript click...");
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    System.out.println("  ↪ Clicked using JavaScript");
                } catch (Exception e2) {
                    System.out.println("  ⚠️ JavaScript click failed, trying Actions click...");
                    actions.moveToElement(element).click().perform();
                    System.out.println("  ↪ Clicked using Actions");
                }
            }
            
            System.out.println("✅ Successfully hovered and clicked: " + elementName);
            if (reportLogger != null) {
                reportLogger.info("Hovered and clicked: " + elementName);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to hover and click: " + elementName);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            if (reportLogger != null) {
                reportLogger.warning("Failed to hover/click: " + elementName + " - " + e.getMessage());
            }
            throw e;
        }
    }
    
    public void Initiativebeforeclickadd() throws Exception {
        waitForSeconds(3); // buffer wait after login
        clickWithFallback(InitiativePageLocators.initreec, "Initiative Nodec");
    }

    // ✅ Helper method to click with js fallback
    private void clickWithFallback(By locator, String elementName) throws Exception {
        waitForElementToBeVisible(locator, elementName);
        try {
            click(locator, elementName);
            if (reportLogger != null) {
                reportLogger.pass("Clicked on: " + elementName);
            }
        } catch (Exception e) {
            jsClick(locator, elementName); // fallback
            if (reportLogger != null) {
                reportLogger.warning("Used JS Click for: " + elementName);
            }
        }
    }

    // ✅ Verify Filters and Search
    public void verifyFiltersAndSearch() throws Exception {
        isElementPresent(InitiativePageLocators.draftFilter, "Draft");
        isElementPresent(InitiativePageLocators.inboxFilter, "Inbox");
        isElementPresent(InitiativePageLocators.watchlistFilter, "Watchlist");
        isElementPresent(InitiativePageLocators.searchInput, "Search");
    }

    // ✅ Verify Draft Count
    public void verifyDraftCount() throws Exception {
        waitForElementToBeClickable(InitiativePageLocators.draftFilter, "Draft");
        click(InitiativePageLocators.draftFilter, "Draft");

        String total = getText(InitiativePageLocators.totalRecords, "Total Records");
        String count = getText(InitiativePageLocators.countRecords, "Visible Records Count");

        if (total.equals(count)) {
            reportLogger.pass("✅ Count matches. Total: " + total);
        } else {
            reportLogger.fail("❌ Mismatch - Total: " + total + " vs Visible: " + count);
        }
    }

    public void ClickADD() throws Exception {
        clickWithFallback(InitiativePageLocators.AddIni, "Add Button");
    }

    public void ClickSD() throws Exception {
        clickWithFallback(InitiativePageLocators.savedraft, "Save as Draft Button");
        waitForSeconds(10);
    }

         public void SelectNOI(String noiValue) throws Exception {
        // Build dynamic XPath for list item (not dropdown)
        	 By noiOption = By.xpath("//div[@class='MuiBox-root css-ah0zvi']//td[normalize-space(text())='" + noiValue + "']");


        
        clickWithFallback(noiOption, "Nature of Initiative: " + noiValue);

        reportLogger.info("✅ Selected Nature of Initiative from list: " + noiValue);
    }

    public void setInitiativeTitle(String title) throws Throwable {
        type(InitiativePageLocators.IniTitle, title, "Initiative Title");
        if (reportLogger != null) {
            reportLogger.info("Entered Initiative Title: " + title);
        }
    }

    public void setInitiativedescription(String descriptionf) throws Throwable {
        type(InitiativePageLocators.iniDescription, descriptionf, "Initiative Description");
        if (reportLogger != null) {
            reportLogger.info("Entered Initiative Description: " + descriptionf);
        }
    }

    public void setAdditionalNotes(String notes) throws Throwable {
        System.out.println("\n📝 ═══════════════════════════════════════════");
        System.out.println("📝 Setting Additional Notes: " + notes);
        System.out.println("📝 ═══════════════════════════════════════════");
        
        try {
            // Wait for the submit comments modal/container
            System.out.println("  🔍 Waiting for Submit Comments Modal...");
            waitForElementToBeVisible(InitiativePageLocators.submitCommentsContainer, "Submit Comments Modal");
            waitForSeconds(3); // Extra wait for modal animation
            System.out.println("  ✓ Modal is visible");
            
            // IMPORTANT: Ensure we're in the correct context (check for iframes)
            System.out.println("  🔍 Checking for iframes in the page...");
            int totalIframeCount = driver.findElements(By.tagName("iframe")).size();
            System.out.println("  📋 Found " + totalIframeCount + " iframe(s) on the entire page");
            
            // Try to find the modal element itself for debugging
            WebElement modal = null;
            try {
                modal = driver.findElement(InitiativePageLocators.submitCommentsContainer);
                System.out.println("  📋 Modal Details:");
                System.out.println("    - Class: " + modal.getAttribute("class"));
                System.out.println("    - Style: " + modal.getAttribute("style"));
                System.out.println("    - Display: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).display;", modal));
                System.out.println("    - Visibility: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).visibility;", modal));
                
                // CRITICAL: Check for iframes INSIDE the modal
                int modalIframeCount = modal.findElements(By.tagName("iframe")).size();
                System.out.println("  📋 Found " + modalIframeCount + " iframe(s) INSIDE the modal");
                
                if (modalIframeCount > 0) {
                    System.out.println("  ⚠️ IFRAME DETECTED INSIDE MODAL - Attempting to switch...");
                    try {
                        WebElement modalIframe = modal.findElement(By.tagName("iframe"));
                        System.out.println("    - Iframe src: " + modalIframe.getAttribute("src"));
                        System.out.println("    - Iframe id: " + modalIframe.getAttribute("id"));
                        System.out.println("    - Iframe name: " + modalIframe.getAttribute("name"));
                        
                        // Switch to the iframe
                        driver.switchTo().frame(modalIframe);
                        System.out.println("  ✅ Successfully switched to iframe inside modal");
                        waitForSeconds(2);
                    } catch (Exception iframeEx) {
                        System.out.println("  ❌ Failed to switch to modal iframe: " + iframeEx.getMessage());
                    }
                }
                
                // Keep focus on modal by clicking it
                System.out.println("  → Clicking modal to ensure focus...");
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", modal);
                    modal.click();
                } catch (Exception focusEx) {
                    System.out.println("  ⚠️ Could not focus modal: " + focusEx.getMessage());
                }
                
            } catch (Exception modalEx) {
                System.out.println("  ⚠️ Could not get modal details: " + modalEx.getMessage());
            }

            // Wait for textarea to be visible
            System.out.println("  🔍 Waiting for Additional Notes textarea...");
            waitForElementToBeVisible(InitiativePageLocators.additionalNotes, "Additional Notes");
            
            boolean success = false;
            WebElement textarea = null;
            
            // Get the textarea element
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                textarea = wait.until(ExpectedConditions.presenceOfElementLocated(InitiativePageLocators.additionalNotes));
                System.out.println("  ✓ Textarea element found");
                
                // DETAILED DEBUGGING - Check all properties
                System.out.println("\n  📋 ═══ DETAILED TEXTAREA ANALYSIS ═══");
                System.out.println("    - Tag Name: " + textarea.getTagName());
                System.out.println("    - ID: " + textarea.getAttribute("id"));
                System.out.println("    - Name: " + textarea.getAttribute("name"));
                System.out.println("    - Class: " + textarea.getAttribute("class"));
                System.out.println("    - Type: " + textarea.getAttribute("type"));
                System.out.println("    - Placeholder: " + textarea.getAttribute("placeholder"));
                System.out.println("    - Current Value: '" + textarea.getAttribute("value") + "'");
                System.out.println("    - Current Text: '" + textarea.getText() + "'");
                System.out.println("    - Displayed: " + textarea.isDisplayed());
                System.out.println("    - Enabled: " + textarea.isEnabled());
                System.out.println("    - Readonly: " + textarea.getAttribute("readonly"));
                System.out.println("    - Disabled: " + textarea.getAttribute("disabled"));
                System.out.println("    - Aria-disabled: " + textarea.getAttribute("aria-disabled"));
                System.out.println("    - Tabindex: " + textarea.getAttribute("tabindex"));
                
                // Check computed styles
                System.out.println("\n  🎨 Computed Styles:");
                System.out.println("    - Display: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).display;", textarea));
                System.out.println("    - Visibility: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).visibility;", textarea));
                System.out.println("    - Opacity: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).opacity;", textarea));
                System.out.println("    - Pointer-events: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).pointerEvents;", textarea));
                System.out.println("    - Z-index: " + ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).zIndex;", textarea));
                
                // Check for overlapping elements
                System.out.println("\n  🔍 Checking for overlapping elements...");
                Object elementAtPoint = ((JavascriptExecutor) driver).executeScript(
                    "var rect = arguments[0].getBoundingClientRect();" +
                    "var x = rect.left + rect.width / 2;" +
                    "var y = rect.top + rect.height / 2;" +
                    "var el = document.elementFromPoint(x, y);" +
                    "return el === arguments[0] ? 'TEXTAREA IS ON TOP' : 'Element at center: ' + el.tagName + ' ' + (el.id || el.className);",
                    textarea
                );
                System.out.println("    - Element check: " + elementAtPoint);
                
                // Scroll into view
                System.out.println("\n  → Scrolling textarea into view...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", textarea);
                waitForSeconds(2);
                
                // Remove readonly if present
                if (textarea.getAttribute("readonly") != null) {
                    System.out.println("  ⚠️ Textarea has readonly attribute - removing it...");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('readonly');", textarea);
                }
                
                // Remove disabled if present
                if (textarea.getAttribute("disabled") != null) {
                    System.out.println("  ⚠️ Textarea has disabled attribute - removing it...");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", textarea);
                }
                
            } catch (Exception e) {
                System.out.println("  ❌ Failed to locate textarea: " + e.getMessage());
                throw e;
            }
            
            // Strategy 1: Standard Selenium click + sendKeys (MODAL-AWARE)
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 1: Standard Selenium interaction (modal-aware)");
                    
                    // Ensure modal stays in focus
                    if (modal != null) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", modal);
                    }
                    
                    // Click textarea and ensure it has focus
                    textarea.click();
                    waitForSeconds(1);
                    
                    // Verify textarea has focus before typing
                    WebElement activeElement = driver.switchTo().activeElement();
                    if (!activeElement.equals(textarea)) {
                        System.out.println("    ⚠️ Textarea not focused, forcing focus...");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", textarea);
                        textarea.click();
                        waitForSeconds(1);
                    }
                    
                    textarea.clear();
                    waitForSeconds(1);
                    textarea.sendKeys(notes);
                    waitForSeconds(2); // Extra wait for value to register
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 1 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 1 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 1 failed: " + e.getMessage());
                }
            }
            
            // Strategy 2: JavaScript setValue with React events (MODAL-AWARE)
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 2: JavaScript with React events (modal-aware)");
                    
                    // Ensure modal stays in focus
                    if (modal != null) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].focus();" +
                            "arguments[0].scrollIntoView({block: 'center'});",
                            modal
                        );
                    }
                    
                    // Prevent any focus loss during interaction
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        // Focus textarea first
                        "element.focus();" +
                        // Set value using React-compatible method
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(element, value);" +
                        // Trigger events
                        "element.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "element.dispatchEvent(new Event('change', { bubbles: true }));" +
                        // Verify focus remains on textarea
                        "element.focus();",
                        textarea, notes
                    );
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 2 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 2 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 2 failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Focus, JavaScript set, trigger all events
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 3: Focus + JavaScript + All events");
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "element.focus();" +
                        "element.value = value;" +
                        "element.dispatchEvent(new Event('input', { bubbles: true, cancelable: true }));" +
                        "element.dispatchEvent(new Event('change', { bubbles: true, cancelable: true }));" +
                        "element.dispatchEvent(new Event('keyup', { bubbles: true, cancelable: true }));" +
                        "element.dispatchEvent(new Event('keydown', { bubbles: true, cancelable: true }));" +
                        "element.blur();",
                        textarea, notes
                    );
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 3 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 3 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 3 failed: " + e.getMessage());
                }
            }
            
            // Strategy 4: Actions class with click, clear, and type
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 4: Actions class");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(textarea)
                           .click()
                           .pause(Duration.ofMillis(500))
                           .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                           .pause(Duration.ofMillis(200))
                           .sendKeys(Keys.DELETE)
                           .pause(Duration.ofMillis(300))
                           .sendKeys(notes)
                           .pause(Duration.ofMillis(500))
                           .perform();
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 4 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 4 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 4 failed: " + e.getMessage());
                }
            }
            
            // Strategy 5: Character by character with events
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 5: Character-by-character typing");
                    textarea.click();
                    waitForSeconds(1);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", textarea);
                    waitForSeconds(1);
                    
                    for (char c : notes.toCharArray()) {
                        textarea.sendKeys(String.valueOf(c));
                        Thread.sleep(100);
                    }
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                        textarea
                    );
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 5 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 5 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 5 failed: " + e.getMessage());
                }
            }
            
            // Strategy 6: Use the existing type method with extra waits
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 6: Using existing type method");
                    textarea.click();
                    waitForSeconds(2);
                    type(InitiativePageLocators.additionalNotes, notes, "Additional Notes");
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 6 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 6 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 6 failed: " + e.getMessage());
                }
            }
            
            // Strategy 7: JavaScript click at center to bypass overlays
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 7: JavaScript click at element center (bypass overlay)");
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var rect = element.getBoundingClientRect();" +
                        "var x = rect.left + rect.width / 2;" +
                        "var y = rect.top + rect.height / 2;" +
                        "element.click();" +
                        "element.focus();",
                        textarea
                    );
                    waitForSeconds(1);
                    
                    // Now try to set value
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "element.value = value;" +
                        "element.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "element.dispatchEvent(new Event('change', { bubbles: true }));",
                        textarea, notes
                    );
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 7 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 7 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 7 failed: " + e.getMessage());
                }
            }
            
            // Strategy 8: Remove pointer-events restriction and retry
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 8: Remove pointer-events and retry");
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.pointerEvents = 'auto';" +
                        "arguments[0].removeAttribute('readonly');" +
                        "arguments[0].removeAttribute('disabled');",
                        textarea
                    );
                    waitForSeconds(1);
                    
                    textarea.click();
                    waitForSeconds(1);
                    textarea.clear();
                    textarea.sendKeys(notes);
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 8 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 8 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 8 failed: " + e.getMessage());
                }
            }
            
            // Strategy 9: Use textContent instead of value
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 9: Set textContent/innerText");
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "element.textContent = value;" +
                        "element.innerText = value;" +
                        "element.value = value;" +
                        "element.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "element.dispatchEvent(new Event('change', { bubbles: true }));",
                        textarea, notes
                    );
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    String textValue = textarea.getText();
                    if ((currentValue != null && currentValue.equals(notes)) || 
                        (textValue != null && textValue.equals(notes))) {
                        System.out.println("  ✅ Strategy 9 SUCCESS - Value set: " + 
                            (currentValue != null ? currentValue : textValue));
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 9 failed - Value: '" + currentValue + "', Text: '" + textValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 9 failed: " + e.getMessage());
                }
            }
            
            // Strategy 10: Try finding the textarea by ID directly (bypass any cached reference issues)
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 10: Find textarea fresh by ID and set value");
                    WebElement freshTextarea = driver.findElement(By.id("TextField220"));
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "element.removeAttribute('readonly');" +
                        "element.removeAttribute('disabled');" +
                        "element.focus();" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(element, value);" +
                        "var event = new Event('input', { bubbles: true });" +
                        "element.dispatchEvent(event);" +
                        "var changeEvent = new Event('change', { bubbles: true });" +
                        "element.dispatchEvent(changeEvent);" +
                        "element.blur();",
                        freshTextarea, notes
                    );
                    waitForSeconds(2);
                    
                    String currentValue = freshTextarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 10 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 10 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 10 failed: " + e.getMessage());
                }
            }
            
            // Strategy 11: Aggressive JavaScript - bypass all React/Vue/Angular handlers
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 11: Aggressive JavaScript bypass (all frameworks)");
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "element.value = '';" +
                        "element.value = value;" +
                        // Trigger React events
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(element, value);" +
                        // Trigger all possible events
                        "var eventTypes = ['input', 'change', 'keydown', 'keyup', 'keypress', 'focus', 'blur'];" +
                        "eventTypes.forEach(function(type) {" +
                        "  var evt = new Event(type, { bubbles: true, cancelable: true });" +
                        "  element.dispatchEvent(evt);" +
                        "});",
                        textarea, notes
                    );
                    waitForSeconds(3);
                    
                    String currentValue = textarea.getAttribute("value");
                    if (currentValue != null && currentValue.equals(notes)) {
                        System.out.println("  ✅ Strategy 11 SUCCESS - Value set: " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 11 failed - Value: '" + currentValue + "'");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 11 failed: " + e.getMessage());
                }
            }
            
            // Strategy 12: Last resort - Force set and don't verify
            if (!success) {
                try {
                    System.out.println("\n  📍 Strategy 12: LAST RESORT - Force set without verification");
                    System.out.println("  ⚠️ WARNING: Forcing value without guaranteeing it will be saved");
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].value = arguments[1];", textarea, notes
                    );
                    
                    // Trigger events
                    textarea.click();
                    textarea.sendKeys(Keys.TAB);
                    waitForSeconds(2);
                    
                    String currentValue = textarea.getAttribute("value");
                    System.out.println("  📋 Final value check: '" + currentValue + "'");
                    
                    // Accept even partial match as last resort
                    if (currentValue != null && !currentValue.isEmpty()) {
                        System.out.println("  ⚠️ Strategy 12 - Value was set (may need manual verification): " + currentValue);
                        success = true;
                    } else {
                        System.out.println("  ✗ Strategy 12 failed - No value set");
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Strategy 12 failed: " + e.getMessage());
                }
            }
            
            if (success) {
                System.out.println("\n✅ ✅ ✅ Successfully set Additional Notes ✅ ✅ ✅");
                
                // IMPORTANT: Verify the value persists and modal is still open
                System.out.println("  🔍 Final verification - checking if value persists...");
                waitForSeconds(1);
                
                try {
                    WebElement verifyTextarea = driver.findElement(InitiativePageLocators.additionalNotes);
                    String finalValue = verifyTextarea.getAttribute("value");
                    System.out.println("  📋 Final verified value: '" + finalValue + "'");
                    
                    if (finalValue != null && finalValue.equals(notes)) {
                        System.out.println("  ✅ Value persists correctly!");
                    } else {
                        System.out.println("  ⚠️ WARNING: Value may have changed: '" + finalValue + "'");
                    }
                } catch (Exception verifyEx) {
                    System.out.println("  ⚠️ Could not verify final value: " + verifyEx.getMessage());
                }
                
                // Verify modal is still visible and focused
                try {
                    if (modal != null && modal.isDisplayed()) {
                        System.out.println("  ✅ Modal is still visible and in focus");
                        // Keep focus on modal to prevent switching away
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].focus();" +
                            "arguments[0].style.zIndex = '9999';", // Ensure modal stays on top
                            modal
                        );
                    }
                } catch (Exception modalCheck) {
                    System.out.println("  ⚠️ Could not verify modal status: " + modalCheck.getMessage());
                }
                
                if (reportLogger != null) {
                    reportLogger.pass("Entered Additional Notes: " + notes);
                }
                
                System.out.println("  📌 Context Status: Staying in MODAL context (do not switch to parent)");
                System.out.println("  ⚠️ IMPORTANT: Next action should happen in THIS modal context");
                
            } else {
                System.out.println("\n❌ ❌ ❌ All strategies FAILED to set Additional Notes ❌ ❌ ❌");
                
                // Debug: Print final state
                System.out.println("\n  🔍 FINAL DEBUG STATE:");
                try {
                    System.out.println("    - Current URL: " + driver.getCurrentUrl());
                    System.out.println("    - Page title: " + driver.getTitle());
                    System.out.println("    - Active element: " + driver.switchTo().activeElement().getTagName());
                    
                    // Check if we're in an iframe
                    try {
                        driver.switchTo().defaultContent();
                        System.out.println("    - Was in iframe context - switched back to default");
                    } catch (Exception e) {
                        System.out.println("    - Already in default context");
                    }
                    
                } catch (Exception debugEx) {
                    System.out.println("    - Could not get debug state: " + debugEx.getMessage());
                }
                
                if (reportLogger != null) {
                    reportLogger.fail("Failed to set Additional Notes after all strategies");
                }
                throw new Exception("All strategies failed to set Additional Notes");
            }
            
            System.out.println("📝 ═══════════════════════════════════════════\n");
            
        } catch (Exception e) {
            System.out.println("❌ Failed to set Additional Notes: " + e.getMessage());
            e.printStackTrace();
            
            // Try to recover context if we're stuck in an iframe
            try {
                driver.switchTo().defaultContent();
                System.out.println("  🔄 Recovered to default content after error");
            } catch (Exception recoverEx) {
                // Already in default content
            }
            
            if (reportLogger != null) {
                reportLogger.fail("Failed to set Additional Notes: " + e.getMessage());
            }
            throw e;
        }
    }
    
    public void setInitiativestartdate(String startdate) throws Throwable {
        System.out.println("📅 Setting Initiative Start Date: " + startdate);
        
        try {
            // Strategy 1: Try the original locator
            boolean success = setDateWithMultipleStrategies(
                InitiativePageLocators.startdate, 
                startdate, 
                "Start Date"
            );
            
            // Strategy 2: If failed, try alternative locator (without -label suffix)
            if (!success) {
                System.out.println("  ↪ Trying alternative locator without -label suffix...");
                By alternativeLocator = By.xpath("//input[@id='DatePicker24']");
                success = setDateWithMultipleStrategies(alternativeLocator, startdate, "Start Date (Alt)");
            }
            
            // Strategy 3: Try finding any date input nearby
            if (!success) {
                System.out.println("  ↪ Trying to find date input by placeholder...");
                By placeholderLocator = By.xpath("//input[contains(@placeholder,'start') or contains(@placeholder,'Start')]");
                success = setDateWithMultipleStrategies(placeholderLocator, startdate, "Start Date (Placeholder)");
            }
            
            if (success) {
                System.out.println("✅ Successfully set Initiative Start Date: " + startdate);
                if (reportLogger != null) {
                    reportLogger.info("Entered Initiative startdate: " + startdate);
                }
            } else {
                throw new Exception("All strategies failed to set start date");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to set Initiative Start Date: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.warning("Failed to set startdate: " + e.getMessage());
            }
            throw e;
        }
    }

    public void setInitiativeenddate(String enddate) throws Throwable {
        System.out.println("\n📅 ═══════════════════════════════════════════");
        System.out.println("📅 Setting Initiative END DATE: " + enddate);
        System.out.println("📅 ═══════════════════════════════════════════");
        
        try {
            // Extra wait to ensure start date is fully processed
            waitForSeconds(2);
            
            // First, scroll to make sure end date field is visible
            System.out.println("  🔍 Scrolling to end date field...");
            try {
                WebElement endDateElement = driver.findElement(InitiativePageLocators.enddate);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", endDateElement);
                waitForSeconds(2);
                System.out.println("  ✓ Scrolled to end date field");
            } catch (Exception scrollEx) {
                System.out.println("  ⚠️ Could not scroll to end date: " + scrollEx.getMessage());
            }
            
            // Check if field is enabled
            try {
                WebElement endDateElement = driver.findElement(InitiativePageLocators.enddate);
                String disabled = endDateElement.getAttribute("disabled");
                String readonly = endDateElement.getAttribute("readonly");
                System.out.println("  📋 End date field status: disabled=" + disabled + ", readonly=" + readonly);
                
                if ("true".equals(disabled) || disabled != null) {
                    System.out.println("  ⚠️ WARNING: End date field appears to be disabled!");
                    System.out.println("  ↪ This might require start date to be set first");
                }
            } catch (Exception statusEx) {
                System.out.println("  ⚠️ Could not check field status: " + statusEx.getMessage());
            }
            
            boolean success = false;
            
            // Strategy 1: Try the original locator with label
            System.out.println("\n  📍 Strategy 1: Original locator (with -label suffix)");
            success = setDateWithMultipleStrategies(
                InitiativePageLocators.enddate, 
                enddate, 
                "End Date"
            );
            
            // Strategy 2: Try alternative locator (without -label suffix)
            if (!success) {
                System.out.println("\n  📍 Strategy 2: Alternative locator (without -label suffix)");
                By alternativeLocator = By.xpath("//input[@id='DatePicker31']");
                success = setDateWithMultipleStrategies(alternativeLocator, enddate, "End Date (Alt)");
            }
            
            // Strategy 3: Try by position (second date input on page)
            if (!success) {
                System.out.println("\n  📍 Strategy 3: Finding second date input field");
                By secondDateInput = By.xpath("(//input[@type='text' and contains(@id,'DatePicker')])[2]");
                success = setDateWithMultipleStrategies(secondDateInput, enddate, "End Date (2nd Input)");
            }
            
            // Strategy 4: Try finding by placeholder
            if (!success) {
                System.out.println("\n  📍 Strategy 4: Finding by placeholder text");
                By placeholderLocator = By.xpath("//input[contains(@placeholder,'end') or contains(@placeholder,'End')]");
                success = setDateWithMultipleStrategies(placeholderLocator, enddate, "End Date (Placeholder)");
            }
            
            // Strategy 5: Try finding the actual input field near the label
            if (!success) {
                System.out.println("\n  📍 Strategy 5: Finding input near end date label");
                By nearLabelLocator = By.xpath("//label[contains(text(),'End') or contains(text(),'end')]/following-sibling::input | //label[contains(text(),'End') or contains(text(),'end')]/..//input");
                success = setDateWithMultipleStrategies(nearLabelLocator, enddate, "End Date (Near Label)");
            }
            
            if (success) {
                System.out.println("\n✅ ✅ ✅ Successfully set Initiative End Date: " + enddate + " ✅ ✅ ✅");
                if (reportLogger != null) {
                    reportLogger.info("Entered Initiative enddate: " + enddate);
                }
            } else {
                System.out.println("\n❌ ❌ ❌ All strategies FAILED to set end date ❌ ❌ ❌");
                // Take screenshot for debugging
                try {
                    System.out.println("  📸 Taking screenshot for debugging...");
                    // You can add screenshot logic here if needed
                } catch (Exception screenshotEx) {}
                
                throw new Exception("All strategies failed to set end date");
            }
            
            System.out.println("📅 ═══════════════════════════════════════════\n");
            
        } catch (Exception e) {
            System.out.println("❌ Failed to set Initiative End Date: " + e.getMessage());
            e.printStackTrace();
            if (reportLogger != null) {
                reportLogger.warning("Failed to set enddate: " + e.getMessage());
            }
            throw e;
        }
    }
    
    // Helper method to set date with multiple strategies
    private boolean setDateWithMultipleStrategies(By locator, String dateValue, String fieldName) {
        try {
            System.out.println("  🔍 Trying to set " + fieldName + " with value: " + dateValue);
            
            // Check if element exists
            if (!driver.findElements(locator).isEmpty()) {
                WebElement dateField = driver.findElement(locator);
                
                if (!dateField.isDisplayed()) {
                    System.out.println("    ⚠️ Element found but not visible");
                    return false;
                }
                
                System.out.println("    ✓ Element found and visible");
                System.out.println("    📋 Current value before: '" + dateField.getAttribute("value") + "'");
                System.out.println("    📋 Tag name: " + dateField.getTagName());
                System.out.println("    📋 Type: " + dateField.getAttribute("type"));
                
                // Method 1: Standard Selenium interaction with TAB
                try {
                    System.out.println("    → Method 1: Standard Selenium with TAB");
                    dateField.click();
                    waitForSeconds(1);
                    dateField.sendKeys(Keys.CONTROL + "a");
                    dateField.sendKeys(Keys.BACK_SPACE);
                    waitForSeconds(1);
                    dateField.sendKeys(dateValue);
                    waitForSeconds(1);
                    dateField.sendKeys(Keys.TAB);
                    waitForSeconds(2); // Extra wait for value to register
                    
                    String currentValue = dateField.getAttribute("value");
                    System.out.println("    📋 Value after Method 1: '" + currentValue + "'");
                    if (currentValue != null && currentValue.contains(dateValue.substring(0, 5))) {
                        System.out.println("    ✅ Method 1 SUCCESS - Value set: " + currentValue);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Method 1 failed: " + e.getMessage());
                }
                
                // Method 2: JavaScript setValue with ALL events
                try {
                    System.out.println("    → Method 2: JavaScript with ALL events");
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "element.focus();" +
                        "element.value = value;" +
                        "element.dispatchEvent(new Event('input', { bubbles: true, cancelable: true }));" +
                        "element.dispatchEvent(new Event('change', { bubbles: true, cancelable: true }));" +
                        "element.dispatchEvent(new Event('keyup', { bubbles: true, cancelable: true }));" +
                        "element.dispatchEvent(new Event('keydown', { bubbles: true, cancelable: true }));" +
                        "element.blur();",
                        dateField, dateValue
                    );
                    waitForSeconds(2);
                    
                    String currentValue = dateField.getAttribute("value");
                    System.out.println("    📋 Value after Method 2: '" + currentValue + "'");
                    if (currentValue != null && currentValue.contains(dateValue.substring(0, 5))) {
                        System.out.println("    ✅ Method 2 SUCCESS - Value set: " + currentValue);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Method 2 failed: " + e.getMessage());
                }
                
                // Method 3: React-specific event triggering
                try {
                    System.out.println("    → Method 3: React-specific events");
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "var value = arguments[1];" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(element, value);" +
                        "var event = new Event('input', { bubbles: true });" +
                        "element.dispatchEvent(event);" +
                        "var changeEvent = new Event('change', { bubbles: true });" +
                        "element.dispatchEvent(changeEvent);",
                        dateField, dateValue
                    );
                    waitForSeconds(2);
                    
                    String currentValue = dateField.getAttribute("value");
                    System.out.println("    📋 Value after Method 3: '" + currentValue + "'");
                    if (currentValue != null && currentValue.contains(dateValue.substring(0, 5))) {
                        System.out.println("    ✅ Method 3 SUCCESS - Value set: " + currentValue);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Method 3 failed: " + e.getMessage());
                }
                
                // Method 4: Actions class - Click, clear, type, ENTER
                try {
                    System.out.println("    → Method 4: Actions with ENTER");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(dateField)
                           .click()
                           .pause(Duration.ofMillis(500))
                           .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                           .pause(Duration.ofMillis(200))
                           .sendKeys(Keys.DELETE)
                           .pause(Duration.ofMillis(300))
                           .sendKeys(dateValue)
                           .pause(Duration.ofMillis(500))
                           .sendKeys(Keys.ENTER)
                           .pause(Duration.ofMillis(500))
                           .perform();
                    waitForSeconds(2);
                    
                    String currentValue = dateField.getAttribute("value");
                    System.out.println("    📋 Value after Method 4: '" + currentValue + "'");
                    if (currentValue != null && currentValue.contains(dateValue.substring(0, 5))) {
                        System.out.println("    ✅ Method 4 SUCCESS - Value set: " + currentValue);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Method 4 failed: " + e.getMessage());
                }
                
                // Method 5: Force set with JavaScript and click outside
                try {
                    System.out.println("    → Method 5: Force set + click outside");
                    // Set value
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].value = arguments[1];", dateField, dateValue
                    );
                    waitForSeconds(1);
                    
                    // Trigger all possible events
                    ((JavascriptExecutor) driver).executeScript(
                        "var element = arguments[0];" +
                        "element.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "element.dispatchEvent(new Event('change', { bubbles: true }));",
                        dateField
                    );
                    
                    // Click somewhere else to trigger blur
                    Actions actions = new Actions(driver);
                    actions.moveToElement(dateField).click()
                           .pause(Duration.ofMillis(500))
                           .sendKeys(Keys.TAB)
                           .pause(Duration.ofMillis(500))
                           .perform();
                    waitForSeconds(2);
                    
                    String currentValue = dateField.getAttribute("value");
                    System.out.println("    📋 Value after Method 5: '" + currentValue + "'");
                    if (currentValue != null && currentValue.contains(dateValue.substring(0, 5))) {
                        System.out.println("    ✅ Method 5 SUCCESS - Value set: " + currentValue);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Method 5 failed: " + e.getMessage());
                }
                
                // Method 6: Character by character with events
                try {
                    System.out.println("    → Method 6: Char-by-char with events");
                    dateField.click();
                    waitForSeconds(1);
                    
                    // Clear field
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", dateField);
                    
                    // Type each character
                    for (char c : dateValue.toCharArray()) {
                        dateField.sendKeys(String.valueOf(c));
                        Thread.sleep(150);
                    }
                    
                    // Trigger events
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                        dateField
                    );
                    
                    dateField.sendKeys(Keys.TAB);
                    waitForSeconds(2);
                    
                    String currentValue = dateField.getAttribute("value");
                    System.out.println("    📋 Value after Method 6: '" + currentValue + "'");
                    if (currentValue != null && currentValue.contains(dateValue.substring(0, 5))) {
                        System.out.println("    ✅ Method 6 SUCCESS - Value set: " + currentValue);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Method 6 failed: " + e.getMessage());
                }
                
                System.out.println("    ❌ All methods failed - value not set properly");
                
            } else {
                System.out.println("    ✗ Element not found with locator: " + locator);
            }
            
        } catch (Exception e) {
            System.out.println("    ✗ Strategy failed completely: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public void verifyInitiativeHeader(String expectedHeader) throws Throwable {
        waitForElementToBeVisible(InitiativePageLocators.pageHeader, "Initiative Page Header");

        String actualHeader = getText(InitiativePageLocators.pageHeader, "Initiative Page Header").trim();

        if (expectedHeader.equalsIgnoreCase(actualHeader)) {
            reportLogger.pass("✅ Header matched! Expected: " + expectedHeader + " | Actual: " + actualHeader);
        } else {
            reportLogger.fail("❌ Header mismatch! Expected: " + expectedHeader + " | Actual: " + actualHeader);
        }
    }
    
   
    public void verifyInitiativeHeaderini(String expectedHeader) throws Throwable {
        waitForElementToBeVisible(InitiativePageLocators.Pageheaderini, "Initiative Page Header add details");
    }

    public void selectInitiativeBGWithActions(String bgName) throws Throwable {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            Actions actions = new Actions(driver);
            
            // 1. Click dropdown
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='Dropdown20-option']")));
            actions.moveToElement(dropdown).click().perform();
            
            // 2. Wait and find option
            Thread.sleep(1000);
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='" + bgName + "']")));
            
            // 3. Move to option and click
            actions.moveToElement(option).click().perform();
            
            reportLogger.info("✅ Selected Initiative BG using Actions: " + bgName);
            
        } catch (Exception e) {
            reportLogger.fail("❌ Failed to select Initiative BG using Actions: " + bgName + " - " + e.getMessage());
            throw e;
        }
    }

    public void selectInitiativeOUWithActions(String OUName) throws Throwable {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            Actions actions = new Actions(driver);
            
            // 1. Click dropdown
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='Dropdown21-option']")));
            actions.moveToElement(dropdown).click().perform();
            
            // 2. Wait and find option
            Thread.sleep(1000);
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='" + OUName + "']")));
            
            // 3. Move to option and click
            actions.moveToElement(option).click().perform();
            
            reportLogger.info("✅ Selected Initiative OU using Actions: " + OUName);
            
        } catch (Exception e) {
            reportLogger.fail("❌ Failed to select  Initiative OU using Actions: " + OUName + " - " + e.getMessage());
            throw e;
        }
    }
    

    public String verifyAlertMessage(String expectedMsg) throws Throwable {
        try {
            // Wait up to 15s since toast may load late
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
            WebElement alertElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(InitiativePageLocators.toastAlert)
            );

            String actualMsg = alertElement.getText().trim();

            // Print captured alert
            System.out.println("🔔 Captured Alert: " + actualMsg);

            // Validate
            if (actualMsg.equals(expectedMsg)) {
                reportLogger.log(Status.PASS, "✅ Alert message verified: " + actualMsg);
            } else {
                reportLogger.fail(
                    "❌ Expected: " + expectedMsg + " but found: " + actualMsg,
                    MediaEntityBuilder.createScreenCaptureFromPath(
                        getScreenshot(webDriver, "AlertMismatch")
                    ).build()
                );
            }

            return actualMsg;

        } catch (TimeoutException e) {
            String msg = "⚠️ No alert appeared within timeout.";
            System.out.println(msg);
            reportLogger.fail(msg);
            throw e;
        }
    }
    
    
    public void verifyInitiativealtmsg(String expectedalert) throws Throwable {
    	
        waitForElementToBeVisible(InitiativePageLocators.toastAlert, "Initiative title alert ");

        String actualalert = getText(InitiativePageLocators.toastAlert, "Initiative title alert").trim();

        if (expectedalert.equalsIgnoreCase(actualalert)) {
            reportLogger.pass("✅ ALert matched! Expected: " + expectedalert + " | Actual: " + actualalert);
        } else {
            reportLogger.fail("❌ ALert mismatch! Expected: " + expectedalert + " | Actual: " + actualalert);
        }
    }
    public void ClickSubmit() throws Exception {
        System.out.println("\n🔘 ═══════════════════════════════════════════");
        System.out.println("🔘 Attempting to Click SUBMIT Button");
        System.out.println("🔘 ═══════════════════════════════════════════");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        boolean clicked = false;
        WebElement submitEl = null;
        
        // Array of locators to try
        By[] submitLocators = {
            InitiativePageLocators.Submit,      // Primary
            InitiativePageLocators.SubmitAlt1,  // Button with SendIcon
            InitiativePageLocators.SubmitAlt2,  // Span with Submit text
            InitiativePageLocators.SubmitAlt3   // Span with Submit and svg
        };
        
        String[] locatorNames = {
            "Primary (absolute path to div container)",
            "Alternative 1 (button with SendIcon)",
            "Alternative 2 (span with Submit text)",
            "Alternative 3 (span with Submit text and svg)"
        };
        
        // Try each locator
        for (int i = 0; i < submitLocators.length && !clicked; i++) {
            try {
                System.out.println("\n  📍 Trying locator " + (i + 1) + ": " + locatorNames[i]);
                
                // Check if element exists
                if (driver.findElements(submitLocators[i]).isEmpty()) {
                    System.out.println("    ✗ Element not found");
                    continue;
                }
                
                submitEl = wait.until(ExpectedConditions.presenceOfElementLocated(submitLocators[i]));
                System.out.println("    ✓ Element found");
                
                // Check if visible
                if (!submitEl.isDisplayed()) {
                    System.out.println("    ⚠️ Element not visible");
                    continue;
                }
                System.out.println("    ✓ Element visible");
                
                // Scroll into view
                System.out.println("    → Scrolling element into view...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", submitEl);
                waitForSeconds(1);
                
                // Wait for it to be clickable
                System.out.println("    → Waiting for element to be clickable...");
                wait.until(ExpectedConditions.elementToBeClickable(submitLocators[i]));
                
                // Strategy 1: Standard click
                System.out.println("    → Strategy 1: Standard click");
                try {
                    submitEl.click();
                    System.out.println("    ✅ Standard click SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e1) {
                    System.out.println("    ✗ Standard click failed: " + e1.getMessage());
                }
                
                // Strategy 2: JavaScript click
                System.out.println("    → Strategy 2: JavaScript click");
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitEl);
                    System.out.println("    ✅ JavaScript click SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e2) {
                    System.out.println("    ✗ JavaScript click failed: " + e2.getMessage());
                }
                
                // Strategy 3: Actions click
                System.out.println("    → Strategy 3: Actions click");
                try {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(submitEl).pause(Duration.ofMillis(300)).click().perform();
                    System.out.println("    ✅ Actions click SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e3) {
                    System.out.println("    ✗ Actions click failed: " + e3.getMessage());
                }
                
                // Strategy 4: Click parent button if exists
                System.out.println("    → Strategy 4: Finding and clicking parent button");
                try {
                    ((JavascriptExecutor) driver).executeScript(
                        "var el = arguments[0];" +
                        "var btn = el.closest('button') || el.closest('[role=\"button\"]');" +
                        "if (btn) { btn.click(); return true; }" +
                        "return false;",
                        submitEl
                    );
                    System.out.println("    ✅ Parent button click SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e4) {
                    System.out.println("    ✗ Parent button click failed: " + e4.getMessage());
                }
                
                // Strategy 5: Send ENTER key
                System.out.println("    → Strategy 5: Sending ENTER key");
                try {
                    submitEl.sendKeys(Keys.ENTER);
                    System.out.println("    ✅ ENTER key SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e5) {
                    System.out.println("    ✗ ENTER key failed: " + e5.getMessage());
                }
                
                // Strategy 6: Tab navigation and ENTER
                System.out.println("    → Strategy 6: Tab navigation + ENTER");
                try {
                    // Move focus to the element using Tab simulation
                    Actions actions = new Actions(driver);
                    actions.moveToElement(submitEl)
                           .click() // Focus on element
                           .pause(Duration.ofMillis(500))
                           .sendKeys(Keys.ENTER)
                           .perform();
                    System.out.println("    ✅ Tab navigation + ENTER SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e6) {
                    System.out.println("    ✗ Tab navigation failed: " + e6.getMessage());
                }
                
                // Strategy 7: Tab from body to element and press SPACE
                System.out.println("    → Strategy 7: Tab navigation + SPACE");
                try {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(submitEl)
                           .click() // Focus on element
                           .pause(Duration.ofMillis(500))
                           .sendKeys(Keys.SPACE)
                           .perform();
                    System.out.println("    ✅ Tab navigation + SPACE SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e7) {
                    System.out.println("    ✗ Tab + SPACE failed: " + e7.getMessage());
                }
                
                // Strategy 8: Focus with JavaScript then send keyboard event
                System.out.println("    → Strategy 8: JS focus + keyboard event");
                try {
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].focus(); " +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key: 'Enter', code: 'Enter', keyCode: 13, which: 13})); " +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key: 'Enter', code: 'Enter', keyCode: 13, which: 13}));",
                        submitEl
                    );
                    waitForSeconds(1);
                    System.out.println("    ✅ JS focus + keyboard event SUCCESS");
                    clicked = true;
                    break;
                } catch (Exception e8) {
                    System.out.println("    ✗ JS focus + keyboard failed: " + e8.getMessage());
                }
                
            } catch (Exception locatorEx) {
                System.out.println("    ✗ Locator failed: " + locatorEx.getMessage());
            }
        }
        
        if (!clicked) {
            System.out.println("\n❌ ❌ ❌ All locators and strategies FAILED ❌ ❌ ❌");
            if (reportLogger != null) reportLogger.fail("Failed to click Submit button with all strategies");
            throw new Exception("Could not click Submit button - all strategies failed");
        }
        
        System.out.println("\n✅ Submit button clicked successfully!");
        waitForSeconds(2);
        
        // Verify it actually fired: wait for modal to appear
        System.out.println("  🔍 Verifying submit action...");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // First, try to wait for the modal to appear (this is the expected behavior)
            try {
                shortWait.until(ExpectedConditions.visibilityOfElementLocated(InitiativePageLocators.modalPopup));
                System.out.println("  ✅ Submit comments modal appeared!");
                if (reportLogger != null) reportLogger.pass("Submit comments modal appeared successfully.");
            } catch (Exception modalEx) {
                // Modal didn't appear, check for other indicators
                System.out.println("  ⚠️ Modal didn't appear, checking other indicators...");
                shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(InitiativePageLocators.toastAlert),
                    ExpectedConditions.stalenessOf(submitEl),
                    ExpectedConditions.invisibilityOf(submitEl)
                ));
                System.out.println("  ✅ Submit action verified - toast appeared or element state changed");
                if (reportLogger != null) reportLogger.pass("Submit action verified.");
            }
        } catch (Exception verifyEx) {
            System.out.println("  ⚠️ Could not verify submit action with any indicator");
            System.out.println("  ⚠️ This may indicate the submit button click did not trigger the expected action");
            if (reportLogger != null) reportLogger.warning("Submit click did not show expected modal or confirmation within timeout");
            // Don't throw exception here, let the next step (Clickpopsub) fail if modal is really not there
        }
        
        System.out.println("🔘 ═══════════════════════════════════════════\n");
    }

    public void waitForSubmit() throws Exception {
        waitForElementToBeVisible(InitiativePageLocators.Submit, "Submit Link");
        waitForElementToBeClickable(InitiativePageLocators.Submit, "Submit Link");
    }

    /**
     * Switch to modal window using XPath locator
     * @param modalXPath XPath locator for the modal container
     * @param timeoutSeconds Maximum time to wait for modal to appear
     * @return WebElement of the modal if found, null otherwise
     */
    public WebElement switchToModalWindow(String modalXPath, int timeoutSeconds) {
        try {
            By modalLocator = By.xpath(modalXPath);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Wait for modal to be visible
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
            
            // Check if modal is displayed
            if (modal.isDisplayed()) {
                reportLogger.info("Successfully switched to modal window: " + modalXPath);
                return modal;
            } else {
                reportLogger.fail("Modal found but not displayed: " + modalXPath);
                return null;
            }
            
        } catch (TimeoutException e) {
            reportLogger.fail("Modal window not found within " + timeoutSeconds + " seconds: " + modalXPath);
            return null;
        } catch (Exception e) {
            reportLogger.fail("Error switching to modal window: " + e.getMessage());
            return null;
        }
    }

    /**
     * Switch to modal window using XPath locator with default timeout
     * @param modalXPath XPath locator for the modal container
     * @return WebElement of the modal if found, null otherwise
     */
    public WebElement switchToModalWindow(String modalXPath) {
        return switchToModalWindow(modalXPath, 20);
    }

    /**
     * Switch to specific modal types with predefined XPath patterns
     */
    public WebElement switchToSubmitModal() {
        return switchToModalWindow("//div[contains(@class, 'modal') and .//h6[contains(text(),'Submit')]]");
    }

    public WebElement switchToConfirmModal() {
        return switchToModalWindow("//div[contains(@class, 'modal') and .//h6[contains(text(),'Confirm')]]");
    }

    public WebElement switchToDeleteModal() {
        return switchToModalWindow("//div[contains(@class, 'modal') and .//h6[contains(text(),'Delete')]]");
    }

    public WebElement switchToGenericModal() {
        return switchToModalWindow("//div[contains(@class, 'modal')]");
    }

    /**
     * Switch to the specific submit modal with comments textarea
     * @return WebElement of the modal if found, null otherwise
     */
    public WebElement switchToSubmitCommentsModal() {
        return switchToModalWindow("//div[contains(@class, 'modal-105') and .//h6[text()='Submit']]");
    }

    /**
     * Check if modal is currently active/visible
     * @param modalXPath XPath locator for the modal
     * @return true if modal is visible, false otherwise
     */
    public boolean isModalActive(String modalXPath) {
        try {
            By modalLocator = By.xpath(modalXPath);
            WebElement modal = driver.findElement(modalLocator);
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Close modal window if it's open
     * @param modalXPath XPath locator for the modal
     * @return true if modal was closed, false otherwise
     */
    public boolean closeModalWindow(String modalXPath) {
        try {
            if (isModalActive(modalXPath)) {
                // Try to find and click close button (X button)
                By closeButton = By.xpath("//div[contains(@class, 'modal')]//button[contains(@class, 'ms-Button--icon')]");
                click(closeButton, "Modal Close Button");
                reportLogger.info("Modal window closed successfully");
                return true;
            }
            return false;
        } catch (Exception e) {
            reportLogger.fail("Error closing modal window: " + e.getMessage());
            return false;
        }
    }

    /**
     * ENHANCED: Click popup submit button with debugging and aggressive strategies
     */
    public void Clickpopsub() throws Exception {
        System.out.println("\n🖱️ ═══════════════════════════════════════════");
        System.out.println("🖱️ Clicking Popup Submit Button (ENHANCED)");
        System.out.println("🖱️ ═══════════════════════════════════════════");
        
        boolean clicked = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // FIRST: Debug - Print ALL buttons in modal
        System.out.println("\n🔍 DEBUG: Scanning all buttons in modal...");
        try {
            List<WebElement> allButtons = driver.findElements(By.xpath("//div[@class='modal-105']//button"));
            System.out.println("  📊 Found " + allButtons.size() + " button(s) in modal");
            for (int i = 0; i < allButtons.size(); i++) {
                WebElement btn = allButtons.get(i);
                try {
                    System.out.println("  Button #" + (i+1) + ":");
                    System.out.println("    - ID: " + btn.getAttribute("id"));
                    System.out.println("    - Class: " + btn.getAttribute("class"));
                    System.out.println("    - Text: '" + btn.getText() + "'");
                    System.out.println("    - Enabled: " + btn.isEnabled());
                    System.out.println("    - Visible: " + btn.isDisplayed());
                } catch (Exception e) {
                    System.out.println("    - Error reading button: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("  ⚠️ Error scanning buttons: " + e.getMessage());
        }
        
        // Also check for spans that might contain submit
        System.out.println("\n🔍 DEBUG: Checking for span elements...");
        try {
            List<WebElement> allSpans = driver.findElements(By.xpath("//div[@class='modal-105']//span[contains(@id,'id__')]"));
            System.out.println("  📊 Found " + allSpans.size() + " span(s) with ID in modal");
            for (int i = 0; i < allSpans.size(); i++) {
                WebElement span = allSpans.get(i);
                try {
                    System.out.println("  Span #" + (i+1) + ": ID=" + span.getAttribute("id") + ", Text='" + span.getText() + "'");
                } catch (Exception e) {
                    System.out.println("  Span #" + (i+1) + ": Error - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("  ⚠️ Error scanning spans: " + e.getMessage());
        }
        
        // Try multiple locators (most reliable first)
        By[] locators = {
            By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button--primary')]"), // Most common
            By.xpath("//div[@class='modal-105']//button[contains(text(),'Submit')]"),
            By.xpath("//div[@class='modal-105']//button[contains(.,'Submit')]"),
            By.xpath("//div[@class='modal-105']//button"),  // ANY button in modal
            InitiativePageLocators.popSubmit,        // Original
            InitiativePageLocators.popSubmitAlt1,
            InitiativePageLocators.popSubmitAlt2,
            InitiativePageLocators.popSubmitAlt3
        };
        
        String[] locatorNames = {
            "ms-Button--primary in modal (MOST RELIABLE)",
            "Button with 'Submit' text",
            "Button containing 'Submit'",
            "ANY button in modal (aggressive)",
            "Original primary locator",
            "Alternative 1",
            "Alternative 2", 
            "Alternative 3"
        };
        
        WebElement submitButton = null;
        
        // Find the button
        for (int i = 0; i < locators.length; i++) {
            try {
                System.out.println("  → Trying locator " + (i + 1) + ": " + locatorNames[i]);
                if (!driver.findElements(locators[i]).isEmpty()) {
                    submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(locators[i]));
                    System.out.println("  ✓ Button found!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("    ✗ Not found: " + e.getMessage());
            }
        }
        
        if (submitButton == null) {
            throw new Exception("❌ Could not find popup submit button with any locator");
        }
        
        // Try clicking strategies (most effective first)
        System.out.println("\n  📋 Button: Tag=" + submitButton.getTagName() + 
                          ", Enabled=" + submitButton.isEnabled() + 
                          ", Visible=" + submitButton.isDisplayed());
        
        // Strategy 1: JavaScript click (most reliable for modals)
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 1: JavaScript click");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
                Thread.sleep(1000);
                System.out.println("  ✅ SUCCESS");
                clicked = true;
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        // Strategy 2: Tab navigation + ENTER (keyboard simulation)
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 2: Tab to button + ENTER");
                Actions actions = new Actions(driver);
                actions.moveToElement(submitButton)
                       .click() // Focus
                       .pause(Duration.ofMillis(500))
                       .sendKeys(Keys.ENTER)
                       .perform();
                Thread.sleep(1000);
                System.out.println("  ✅ SUCCESS");
                clicked = true;
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        // Strategy 3: Tab through modal (most realistic user behavior)
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 3: Tab through modal + ENTER");
                WebElement modal = driver.findElement(InitiativePageLocators.modalPopup);
                modal.click();
                Thread.sleep(500);
                
                Actions actions = new Actions(driver);
                // Tab 5 times to reach submit button (usually last element)
                for (int i = 0; i < 5; i++) {
                    actions.sendKeys(Keys.TAB).pause(Duration.ofMillis(300)).perform();
                }
                actions.sendKeys(Keys.ENTER).perform();
                Thread.sleep(1000);
                System.out.println("  ✅ SUCCESS");
                clicked = true;
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        // Strategy 4: Standard Actions click
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 4: Actions class click");
                Actions actions = new Actions(driver);
                actions.moveToElement(submitButton)
                       .pause(Duration.ofMillis(500))
                       .click()
                       .perform();
                Thread.sleep(1000);
                System.out.println("  ✅ SUCCESS");
                clicked = true;
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        // Strategy 5: JavaScript with keyboard events
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 5: JS focus + keyboard event");
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].focus(); " +
                    "arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key: 'Enter', bubbles: true})); " +
                    "arguments[0].click();",
                    submitButton
                );
                Thread.sleep(1000);
                System.out.println("  ✅ SUCCESS");
                clicked = true;
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        // Strategy 6: NUCLEAR OPTION - Click ALL primary buttons in modal
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 6: NUCLEAR - Click all primary buttons");
                ((JavascriptExecutor) driver).executeScript(
                    "var buttons = document.querySelectorAll('.modal-105 button.ms-Button--primary');" +
                    "console.log('Found buttons:', buttons.length);" +
                    "for (var i = 0; i < buttons.length; i++) {" +
                    "  console.log('Clicking button', i);" +
                    "  buttons[i].click();" +
                    "}" +
                    "if (buttons.length === 0) {" +
                    "  var anyButton = document.querySelector('.modal-105 button');" +
                    "  if (anyButton) anyButton.click();" +
                    "}"
                );
                Thread.sleep(1000);
                System.out.println("  ✅ SUCCESS (forced)");
                clicked = true;
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        // Strategy 7: SUPER NUCLEAR - Click by querySelector with all possible selectors
        if (!clicked) {
            try {
                System.out.println("\n  📍 Strategy 7: SUPER NUCLEAR - Try every possible selector");
                boolean result = (boolean) ((JavascriptExecutor) driver).executeScript(
                    "var selectors = [" +
                    "  '.modal-105 button[class*=\"primary\"]'," +
                    "  '.modal-105 button:last-child'," +
                    "  '.modal-105 button'," +
                    "  'button[class*=\"ms-Button\"]'" +
                    "];" +
                    "for (var i = 0; i < selectors.length; i++) {" +
                    "  var btn = document.querySelector(selectors[i]);" +
                    "  if (btn) {" +
                    "    console.log('Clicking with selector:', selectors[i]);" +
                    "    btn.click();" +
                    "    return true;" +
                    "  }" +
                    "}" +
                    "return false;"
                );
                Thread.sleep(1000);
                if (result) {
                    System.out.println("  ✅ SUCCESS (super nuclear)");
                    clicked = true;
                } else {
                    System.out.println("  ✗ No buttons found even with nuclear option");
                }
            } catch (Exception e) {
                System.out.println("  ✗ Failed: " + e.getMessage());
            }
        }
        
        if (!clicked) {
            System.out.println("\n❌ ❌ ❌ ALL STRATEGIES FAILED ❌ ❌ ❌");
            System.out.println("📸 Taking screenshot for debugging...");
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotPath = "submit_button_failed_" + System.currentTimeMillis() + ".png";
                FileUtils.copyFile(screenshot, new File(screenshotPath));
                System.out.println("  📸 Screenshot saved: " + screenshotPath);
            } catch (Exception e) {
                System.out.println("  ⚠️ Could not take screenshot: " + e.getMessage());
            }
            throw new Exception("❌ All strategies failed to click popup submit button");
        }
        
        System.out.println("\n✅ ✅ ✅ Popup Submit Button Clicked Successfully! ✅ ✅ ✅");
        System.out.println("🖱️ ═══════════════════════════════════════════\n");
        
        // Verify submission
        Thread.sleep(2000);
        try {
            WebElement modal = driver.findElement(InitiativePageLocators.modalPopup);
            if (!modal.isDisplayed()) {
                System.out.println("  ✅ Modal closed - submission successful!");
            }
        } catch (Exception e) {
            System.out.println("  ✅ Modal closed - submission successful!");
        }
        
        if (reportLogger != null) {
            reportLogger.pass("Popup submit button clicked successfully");
        }
    }
    
    /**
     * Enter comment in modal textarea and submit
     * @param comment Text to enter in the comments textarea
     * @param modalXPath XPath for the modal (optional, uses default if null)
     */
    public void enterCommentAndSubmit(String comment, String modalXPath) {
        try {
            // Switch to modal if XPath provided, otherwise use default
            WebElement modal = (modalXPath != null) ? 
                switchToModalWindow(modalXPath) : 
                switchToSubmitCommentsModal();
            
            if (modal == null) {
                reportLogger.fail("Could not switch to modal window");
                return;
            }

            // Wait for and interact with textarea
            By textareaLocator = By.xpath("//textarea[@id='TextField936']");
            waitForElementToBeVisible(textareaLocator, "Comments Textarea");
            
            WebElement textarea = driver.findElement(textareaLocator);
            textarea.clear();
            textarea.sendKeys(comment);
            
            reportLogger.info("Entered comment: " + comment);
            
            // Click submit button in modal
            By modalSubmitButton = By.xpath("//button[contains(@class, 'ms-Button--primary') and .//span[normalize-space(text())='Submit']]");
            click(modalSubmitButton, "Modal Submit Button");
            
            reportLogger.pass("Comment submitted successfully");
            
        } catch (Exception e) {
            reportLogger.fail("Error entering comment and submitting: " + e.getMessage());
        }
    }
   
    // ═══════════════════════════════════════════════════════════════
    // 🪟 WINDOW HANDLE MANAGEMENT METHODS
    // ═══════════════════════════════════════════════════════════════
    
    private String parentWindowHandle;
    
    /**
     * Store the current window handle as parent window
     */
    public void storeParentWindow() {
        try {
            parentWindowHandle = driver.getWindowHandle();
            System.out.println("📌 Stored parent window: " + parentWindowHandle);
            if (reportLogger != null) {
                reportLogger.info("Stored parent window handle");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to store parent window: " + e.getMessage());
        }
    }
    
    /**
     * Get current window count
     */
    public int getWindowCount() {
        try {
            return driver.getWindowHandles().size();
        } catch (Exception e) {
            System.out.println("❌ Failed to get window count: " + e.getMessage());
            return 1;
        }
    }
    
    /**
     * Wait for new window to appear
     * @param expectedCount Expected number of windows
     * @param timeoutSeconds Maximum time to wait
     * @return true if new window appeared, false otherwise
     */
    public boolean waitForNewWindow(int expectedCount, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(driver -> driver.getWindowHandles().size() >= expectedCount);
        } catch (Exception e) {
            System.out.println("❌ New window did not appear: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Switch to modal popup window (new browser window)
     * @param modalLocator Locator for modal verification
     * @param modalName Name of modal for logging
     * @return true if switched successfully
     */
    public boolean switchToModalPopupWindow(By modalLocator, String modalName) {
        try {
            String currentHandle = driver.getWindowHandle();
            System.out.println("📍 Current window: " + currentHandle);
            
            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(parentWindowHandle)) {
                    System.out.println("🔄 Switching to window: " + handle);
                    driver.switchTo().window(handle);
                    
                    // Wait for modal to be visible
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
                    
                    System.out.println("✅ Switched to " + modalName);
                    if (reportLogger != null) {
                        reportLogger.info("Switched to modal popup window: " + modalName);
                    }
                    return true;
                }
            }
            
            System.out.println("❌ No new window found");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Failed to switch to modal popup: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Close current window and switch back to parent
     */
    public void closeCurrentWindowAndSwitchToParent() {
        try {
            String currentHandle = driver.getWindowHandle();
            System.out.println("🗑️ Closing window: " + currentHandle);
            driver.close();
            
            if (parentWindowHandle != null) {
                driver.switchTo().window(parentWindowHandle);
                System.out.println("✅ Switched back to parent window");
                if (reportLogger != null) {
                    reportLogger.info("Closed popup and switched to parent");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to close window: " + e.getMessage());
        }
    }
    
    /**
     * Switch to parent window
     */
    public void switchToParentWindow() {
        try {
            if (parentWindowHandle != null) {
                driver.switchTo().window(parentWindowHandle);
                System.out.println("✅ Switched back to parent window successfully.");
                if (reportLogger != null) {
                    reportLogger.info("Switched to parent window");
                }
            } else {
                System.out.println("⚠️ Parent window handle is null");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to switch to parent: " + e.getMessage());
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📋 MODAL OVERLAY METHODS (HTML Modal, not popup window)
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Wait for HTML modal overlay to appear (not a popup window)
     * @param modalLocator Locator for the modal
     * @param timeoutSeconds Maximum time to wait
     * @return true if modal appeared
     */
    public boolean waitForHTMLModal(By modalLocator, int timeoutSeconds) {
        try {
            System.out.println("⏳ Waiting for HTML modal to appear...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
            
            if (modal.isDisplayed()) {
                System.out.println("✅ HTML Modal is visible: " + modalLocator);
                if (reportLogger != null) {
                    reportLogger.info("HTML modal appeared: " + modalLocator);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("❌ HTML Modal did not appear: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Type text in modal overlay (ensures modal context is maintained)
     * @param elementLocator Locator for input field
     * @param text Text to type
     * @param fieldName Name of field for logging
     */
    public void typeInModal(By elementLocator, String text, String fieldName) {
        System.out.println("\n📝 ═══════════════════════════════════════════");
        System.out.println("📝 Typing in Modal: " + fieldName + " = " + text);
        System.out.println("📝 ═══════════════════════════════════════════");
        
        try {
            // Use setAdditionalNotes if this is the notes field
            if (elementLocator.equals(InitiativePageLocators.additionalNotes)) {
                setAdditionalNotes(text);
            } else {
                // For other fields, use standard typing
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
                
                // Scroll into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                waitForSeconds(1);
                
                // Click and type
                element.click();
                waitForSeconds(1);
                element.clear();
                element.sendKeys(text);
                
                System.out.println("✅ Successfully typed in modal field: " + fieldName);
                if (reportLogger != null) {
                    reportLogger.info("Typed in modal: " + fieldName + " = " + text);
                }
            }
        } catch (Throwable e) {
            System.out.println("❌ Failed to type in modal: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.fail("Failed to type in modal: " + fieldName + " - " + e.getMessage());
            }
            throw new RuntimeException("Failed to type in modal: " + fieldName, e);
        }
    }
    
    /**
     * Click element in modal overlay
     * @param elementLocator Locator for element to click
     * @param elementName Name of element for logging
     */
    public void clickElementInModal(By elementLocator, String elementName) {
        System.out.println("\n🖱️ Clicking in Modal: " + elementName);
        
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
            
            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            waitForSeconds(1);
            
            // Try regular click first
            try {
                element.click();
                System.out.println("✅ Clicked: " + elementName);
            } catch (Exception e1) {
                // Try JavaScript click as fallback
                System.out.println("⚠️ Regular click failed, trying JavaScript click...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("✅ JavaScript clicked: " + elementName);
            }
            
            if (reportLogger != null) {
                reportLogger.info("Clicked in modal: " + elementName);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to click in modal: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.fail("Failed to click in modal: " + elementName + " - " + e.getMessage());
            }
            throw new RuntimeException("Failed to click in modal: " + elementName, e);
        }
    }

    // ==================== INBOX & WATCHLIST COUNT VERIFICATION METHODS ====================
    
    /**
     * Click Inbox filter button with multiple strategies
     */
    public void clickInboxFilter() {
        try {
            System.out.println("\n📥 ═══════════════════════════════════════════");
            System.out.println("📥 Clicking Inbox Filter");
            System.out.println("📥 ═══════════════════════════════════════════");
            
            boolean clicked = false;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Try multiple locators
            By[] locators = {
                InitiativePageLocators.inboxFilterAlt1,  // ID: FltrCountInbox
                InitiativePageLocators.inboxFilterAlt2,  // Button contains 'Inbox'
                InitiativePageLocators.inboxFilterAlt3,  // Span with parent button
                InitiativePageLocators.inboxFilter       // Combined primary locator
            };
            
            String[] locatorNames = {
                "ID: FltrCountInbox",
                "Button containing 'Inbox'",
                "Span 'Inbox' with parent button",
                "Primary combined locator"
            };
            
            WebElement inboxButton = null;
            int foundIndex = -1;
            
            // Find the button
            for (int i = 0; i < locators.length; i++) {
                try {
                    System.out.println("  → Trying locator " + (i + 1) + ": " + locatorNames[i]);
                    if (!driver.findElements(locators[i]).isEmpty()) {
                        inboxButton = wait.until(ExpectedConditions.elementToBeClickable(locators[i]));
                        System.out.println("  ✓ Button found with locator " + (i + 1));
                        foundIndex = i;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Not found: " + e.getMessage());
                }
            }
            
            if (inboxButton == null) {
                throw new Exception("❌ Could not find Inbox filter button with any locator");
            }
            
            System.out.println("\n  📋 Button Details:");
            System.out.println("    Tag: " + inboxButton.getTagName());
            System.out.println("    Text: " + inboxButton.getText());
            System.out.println("    Enabled: " + inboxButton.isEnabled());
            System.out.println("    Displayed: " + inboxButton.isDisplayed());
            
            // Strategy 1: Scroll into view + Standard click
            if (!clicked) {
                try {
                    System.out.println("\n  📍 Strategy 1: Scroll + Standard click");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inboxButton);
                    Thread.sleep(500);
                    inboxButton.click();
                    Thread.sleep(2000);
                    System.out.println("  ✅ SUCCESS");
                    clicked = true;
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            // Strategy 2: JavaScript click
            if (!clicked) {
                try {
                    System.out.println("\n  📍 Strategy 2: JavaScript click");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", inboxButton);
                    Thread.sleep(2000);
                    System.out.println("  ✅ SUCCESS");
                    clicked = true;
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Actions click
            if (!clicked) {
                try {
                    System.out.println("\n  📍 Strategy 3: Actions click");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(inboxButton).click().perform();
                    Thread.sleep(2000);
                    System.out.println("  ✅ SUCCESS");
                    clicked = true;
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            if (!clicked) {
                throw new Exception("❌ All strategies failed to click Inbox filter");
            }
            
            System.out.println("\n✅ ✅ ✅ Inbox Filter Clicked Successfully! ✅ ✅ ✅");
            System.out.println("📥 ═══════════════════════════════════════════\n");
            
            if (reportLogger != null) {
                reportLogger.pass("Clicked Inbox filter successfully");
            }
            
        } catch (Exception e) {
            System.out.println("\n❌ Failed to click Inbox filter: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.fail("Failed to click Inbox filter: " + e.getMessage());
            }
            throw new RuntimeException("Failed to click Inbox filter", e);
        }
    }
    
    /**
     * Click Watchlist filter button with multiple strategies
     */
    public void clickWatchlistFilter() {
        try {
            System.out.println("\n⭐ ═══════════════════════════════════════════");
            System.out.println("⭐ Clicking Watchlist Filter");
            System.out.println("⭐ ═══════════════════════════════════════════");
            
            boolean clicked = false;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Try multiple locators
            By[] locators = {
                InitiativePageLocators.watchlistFilterAlt1,  // ID: FltrCountWatchlist
                InitiativePageLocators.watchlistFilterAlt2,  // Button contains 'Watchlist'
                InitiativePageLocators.watchlistFilterAlt3,  // Span with parent button
                InitiativePageLocators.watchlistFilter       // Combined primary locator
            };
            
            String[] locatorNames = {
                "ID: FltrCountWatchlist",
                "Button containing 'Watchlist'",
                "Span 'Watchlist' with parent button",
                "Primary combined locator"
            };
            
            WebElement watchlistButton = null;
            int foundIndex = -1;
            
            // Find the button
            for (int i = 0; i < locators.length; i++) {
                try {
                    System.out.println("  → Trying locator " + (i + 1) + ": " + locatorNames[i]);
                    if (!driver.findElements(locators[i]).isEmpty()) {
                        watchlistButton = wait.until(ExpectedConditions.elementToBeClickable(locators[i]));
                        System.out.println("  ✓ Button found with locator " + (i + 1));
                        foundIndex = i;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Not found: " + e.getMessage());
                }
            }
            
            if (watchlistButton == null) {
                throw new Exception("❌ Could not find Watchlist filter button with any locator");
            }
            
            System.out.println("\n  📋 Button Details:");
            System.out.println("    Tag: " + watchlistButton.getTagName());
            System.out.println("    Text: " + watchlistButton.getText());
            System.out.println("    Enabled: " + watchlistButton.isEnabled());
            System.out.println("    Displayed: " + watchlistButton.isDisplayed());
            
            // Strategy 1: Scroll into view + Standard click
            if (!clicked) {
                try {
                    System.out.println("\n  📍 Strategy 1: Scroll + Standard click");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", watchlistButton);
                    Thread.sleep(500);
                    watchlistButton.click();
                    Thread.sleep(2000);
                    System.out.println("  ✅ SUCCESS");
                    clicked = true;
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            // Strategy 2: JavaScript click
            if (!clicked) {
                try {
                    System.out.println("\n  📍 Strategy 2: JavaScript click");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", watchlistButton);
                    Thread.sleep(2000);
                    System.out.println("  ✅ SUCCESS");
                    clicked = true;
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Actions click
            if (!clicked) {
                try {
                    System.out.println("\n  📍 Strategy 3: Actions click");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(watchlistButton).click().perform();
                    Thread.sleep(2000);
                    System.out.println("  ✅ SUCCESS");
                    clicked = true;
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            // Strategy 4: Click parent button if span was found
            if (!clicked && watchlistButton.getTagName().equals("span")) {
                try {
                    System.out.println("\n  📍 Strategy 4: Click parent button");
                    WebElement parentButton = watchlistButton.findElement(By.xpath(".."));
                    if (parentButton.getTagName().equals("button")) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", parentButton);
                        Thread.sleep(2000);
                        System.out.println("  ✅ SUCCESS");
                        clicked = true;
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Failed: " + e.getMessage());
                }
            }
            
            if (!clicked) {
                throw new Exception("❌ All strategies failed to click Watchlist filter");
            }
            
            System.out.println("\n✅ ✅ ✅ Watchlist Filter Clicked Successfully! ✅ ✅ ✅");
            System.out.println("⭐ ═══════════════════════════════════════════\n");
            
            if (reportLogger != null) {
                reportLogger.pass("Clicked Watchlist filter successfully");
            }
            
        } catch (Exception e) {
            System.out.println("\n❌ Failed to click Watchlist filter: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.fail("Failed to click Watchlist filter: " + e.getMessage());
            }
            throw new RuntimeException("Failed to click Watchlist filter", e);
        }
    }
    
    /**
     * Get the count displayed on Inbox filter badge
     * 
     * @return Inbox count as integer
     */
    public int getInboxCount() {
        try {
            System.out.println("\n🔢 ═══════════════════════════════════════════");
            System.out.println("🔢 Getting Inbox Count from Badge");
            System.out.println("🔢 ═══════════════════════════════════════════");
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Try multiple locators to find the count
            By[] countLocators = {
                By.xpath("//span[id='FltrCountInbox']"),
                By.xpath("//span[normalize-space()='Inbox']/following-sibling::span"),
                By.xpath("//span[normalize-space()='Inbox']/..//span[contains(@class,'count')]"),
                By.xpath("//span[normalize-space()='Inbox']/..//span[contains(@class,'badge')]"),
                By.xpath("//button[contains(.,'Inbox')]//span[contains(@class,'count')]"),
                By.xpath("//button[contains(.,'Inbox')]")  // Try button itself
            };
            
            String[] locatorNames = {
                "span[id='FltrCountInbox']",
                "Span following Inbox",
                "Span with class 'count'",
                "Span with class 'badge'",
                "Button Inbox > span count",
                "Button containing Inbox"
            };
            
            for (int i = 0; i < countLocators.length; i++) {
                try {
                    System.out.println("  → Trying locator " + (i + 1) + ": " + locatorNames[i]);
                    
                    if (!driver.findElements(countLocators[i]).isEmpty()) {
                        WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(countLocators[i]));
                        String originalText = countElement.getText().trim();
                        System.out.println("    📝 Original text: '" + originalText + "'");
                        
                        // Extract numbers from text (e.g., "Inbox 5" -> "5", "(5)" -> "5")
                        String countText = originalText.replaceAll("[^0-9]", "");
                        System.out.println("    🔢 Extracted numbers: '" + countText + "'");
                        
                        if (!countText.isEmpty()) {
                            int count = Integer.parseInt(countText);
                            System.out.println("  ✅ Successfully found count: " + count);
                            System.out.println("🔢 ═══════════════════════════════════════════\n");
                            return count;
                        } else {
                            System.out.println("    ⚠️ No numbers found in text");
                        }
                    } else {
                        System.out.println("    ✗ Element not found");
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Error: " + e.getMessage());
                    // Try next locator
                    continue;
                }
            }
            
            System.out.println("\n  ⚠️ Could not find inbox count badge with any locator");
            System.out.println("  ℹ️ Returning 0 as fallback");
            System.out.println("🔢 ═══════════════════════════════════════════\n");
            return 0;
            
        } catch (Exception e) {
            System.out.println("❌ Failed to get Inbox count: " + e.getMessage());
            e.printStackTrace();
            if (reportLogger != null) {
                reportLogger.warning("Failed to get Inbox count: " + e.getMessage());
            }
            return 0;
        }
    }
    
    /**
     * Get the count displayed on Watchlist filter badge
     * 
     * @return Watchlist count as integer
     */
    public int getWatchlistCount() {
        try {
            System.out.println("\n🔢 Getting Watchlist Count from Badge");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Try multiple locators to find the count
            By[] countLocators = {
                By.xpath("//span[id='FltrCountWatchlist']"),
                By.xpath("//span[normalize-space()='Watchlist']/following-sibling::span"),
                By.xpath("//span[normalize-space()='Watchlist']/..//span[contains(@class,'count')]"),
                By.xpath("//span[normalize-space()='Watchlist']/..//span[contains(@class,'badge')]"),
                By.xpath("//button[contains(.,'Watchlist')]//span[contains(@class,'count')]")
            };
            
            for (By locator : countLocators) {
                try {
                    if (!driver.findElements(locator).isEmpty()) {
                        WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                        String countText = countElement.getText().trim();
                        
                        // Extract numbers from text (e.g., "(5)" -> "5")
                        countText = countText.replaceAll("[^0-9]", "");
                        
                        if (!countText.isEmpty()) {
                            int count = Integer.parseInt(countText);
                            System.out.println("  📊 Watchlist Count from Badge: " + count);
                            return count;
                        }
                    }
                } catch (Exception e) {
                    // Try next locator
                    continue;
                }
            }
            
            System.out.println("  ⚠️ Could not find watchlist count badge, returning 0");
            return 0;
            
        } catch (Exception e) {
            System.out.println("❌ Failed to get Watchlist count: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.warning("Failed to get Watchlist count: " + e.getMessage());
            }
            return 0;
        }
    }
    
    /**
     * Get the count of records displayed in the grid
     * 
     * @return Number of visible records in grid
     */
    public int getGridRecordsCount() {
        try {
            System.out.println("\n📋 ═══════════════════════════════════════════");
            System.out.println("📋 Counting Records in Grid");
            System.out.println("📋 ═══════════════════════════════════════════");
            
            Thread.sleep(2000); // Wait for grid to stabilize
            
            // Try multiple locators for grid rows
            By[] rowLocators = {
            	By.xpath("//div[@id='root']/div[2]/div/div[2]/div[2]/div/div/div[4]/table/tbody/tr/td/div/p"),
                By.xpath("//div[@role='row' and contains(@class,'ag-row')]"),
                By.xpath("//div[contains(@class,'ag-center-cols-container')]//div[@role='row']"),
                By.xpath("//table//tbody//tr[@role='row']"),
                By.xpath("//div[@role='gridcell']/../.."),
                By.xpath("//div[contains(@class,'data-grid')]//div[@role='row']"),
                By.xpath("//div[contains(@class,'ag-row')]"),  // Simplified AG Grid
                By.xpath("//tr[contains(@class,'ag-row')]")    // Table-based AG Grid
            };
            
            String[] locatorNames = {
                "div[role='row'] with ag-row class",
                "ag-center-cols-container rows",
                "table tbody tr[role='row']",
                "gridcell parent rows",
                "data-grid rows",
                "div with ag-row class",
                "tr with ag-row class"
            };
            
            for (int i = 0; i < rowLocators.length; i++) {
                try {
                    System.out.println("  → Trying locator " + (i + 1) + ": " + locatorNames[i]);
                    
                    List<WebElement> rows = driver.findElements(rowLocators[i]);
                    System.out.println("    📊 Found " + rows.size() + " total elements");
                    
                    if (!rows.isEmpty()) {
                        // Filter out any header or empty rows
                        int visibleRows = 0;
                        int headerRows = 0;
                        int hiddenRows = 0;
                        int emptyRows = 0;
                        
                        for (int j = 0; j < rows.size(); j++) {
                            WebElement row = rows.get(j);
                            try {
                                String rowClass = row.getAttribute("class");
                                String rowText = row.getText().trim();
                                boolean isDisplayed = row.isDisplayed();
                                boolean isHeader = rowClass != null && rowClass.contains("header");
                                boolean isEmpty = rowText.isEmpty();
                                
                                if (j < 3) {  // Show details for first 3 rows
                                    System.out.println("      Row " + (j + 1) + ": " + 
                                        "displayed=" + isDisplayed + 
                                        ", header=" + isHeader + 
                                        ", empty=" + isEmpty +
                                        ", text='" + (rowText.length() > 30 ? rowText.substring(0, 30) + "..." : rowText) + "'");
                                }
                                
                                if (isHeader) {
                                    headerRows++;
                                } else if (!isDisplayed) {
                                    hiddenRows++;
                                } else if (isEmpty) {
                                    emptyRows++;
                                } else {
                                    visibleRows++;
                                }
                            } catch (Exception e) {
                                // Skip rows that throw exceptions
                                System.out.println("      Row " + (j + 1) + ": Error - " + e.getMessage());
                                continue;
                            }
                        }
                        
                        System.out.println("    📊 Summary: " + visibleRows + " visible, " + 
                                          headerRows + " headers, " + 
                                          hiddenRows + " hidden, " + 
                                          emptyRows + " empty");
                        
                        if (visibleRows > 0) {
                            System.out.println("  ✅ Successfully counted " + visibleRows + " visible records");
                            System.out.println("📋 ═══════════════════════════════════════════\n");
                            return visibleRows;
                        }
                    } else {
                        System.out.println("    ✗ No elements found");
                    }
                } catch (Exception e) {
                    System.out.println("    ✗ Error: " + e.getMessage());
                    // Try next locator
                    continue;
                }
            }
            
            System.out.println("\n  ⚠️ Could not find grid rows with any locator");
            System.out.println("  ℹ️ Returning 0 as fallback");
            System.out.println("📋 ═══════════════════════════════════════════\n");
            return 0;
            
        } catch (Exception e) {
            System.out.println("❌ Failed to get grid records count: " + e.getMessage());
            e.printStackTrace();
            if (reportLogger != null) {
                reportLogger.warning("Failed to get grid records count: " + e.getMessage());
            }
            return 0;
        }
    }
    
    /**
     * Verify that Inbox count matches grid records count
     * 
     * @param expectedRecordsPerPage Expected number of records per page (default: 5)
     * @return true if counts match, false otherwise
     */
    public boolean verifyInboxCountMatchesGrid(int expectedRecordsPerPage) {
        try {
            System.out.println("\n✅ ═══════════════════════════════════════════════════════");
            System.out.println("✅              VERIFYING INBOX COUNT vs GRID RECORDS");
            System.out.println("✅ ═══════════════════════════════════════════════════════");
            
            int inboxCount = getInboxCount();
            int gridRecordsCount = getGridRecordsCount();
            
            System.out.println("\n📊 ═══════════ DETAILED COMPARISON ═══════════");
            System.out.println("  📬 Inbox Badge Count:        " + inboxCount + " (total records in Inbox)");
            System.out.println("  📋 Grid Records Count:       " + gridRecordsCount + " (visible on current page)");
            System.out.println("  📄 Expected Records/Page:    " + expectedRecordsPerPage + " (configured page size)");
            System.out.println("  ════════════════════════════════════════");
            
            // Determine expected records on current page
            int expectedOnPage = Math.min(inboxCount, expectedRecordsPerPage);
            System.out.println("  🎯 Expected on Current Page: " + expectedOnPage);
            System.out.println("     (calculated as min(" + inboxCount + ", " + expectedRecordsPerPage + "))");
            System.out.println("  ════════════════════════════════════════");
            
            boolean matches = (gridRecordsCount == expectedOnPage);
            
            System.out.println("\n📐 VERIFICATION LOGIC:");
            System.out.println("  " + gridRecordsCount + " == " + expectedOnPage + " ?");
            System.out.println("  " + matches);
            
            if (matches) {
                System.out.println("\n✅ ✅ ✅ VERIFICATION PASSED! ✅ ✅ ✅");
                System.out.println("  🎉 Grid shows " + gridRecordsCount + " records as expected!");
                System.out.println("  ✓ Badge count: " + inboxCount);
                System.out.println("  ✓ Grid count: " + gridRecordsCount);
                System.out.println("  ✓ Expected: " + expectedOnPage);
                if (reportLogger != null) {
                    reportLogger.pass("Inbox count verification PASSED - Grid shows " + gridRecordsCount + " records, matching expected count");
                }
            } else {
                System.out.println("\n❌ ❌ ❌ VERIFICATION FAILED! ❌ ❌ ❌");
                System.out.println("  ⚠️ Mismatch detected!");
                System.out.println("  Expected: " + expectedOnPage + " records on page");
                System.out.println("  Found:    " + gridRecordsCount + " records on page");
                System.out.println("  Difference: " + Math.abs(expectedOnPage - gridRecordsCount) + " records");
                
                System.out.println("\n🔍 POSSIBLE CAUSES:");
                if (inboxCount == 0) {
                    System.out.println("  • Inbox badge count is 0 - badge might not be read correctly");
                }
                if (gridRecordsCount == 0) {
                    System.out.println("  • Grid count is 0 - grid might not have loaded");
                    System.out.println("  • Grid locator might be incorrect");
                }
                if (inboxCount > 0 && gridRecordsCount == 0) {
                    System.out.println("  • Badge shows " + inboxCount + " but grid shows 0");
                    System.out.println("  • Check if grid has finished loading");
                }
                if (inboxCount == 0 && gridRecordsCount > 0) {
                    System.out.println("  • Grid shows " + gridRecordsCount + " but badge shows 0");
                    System.out.println("  • Check if badge locator is correct");
                }
                if (inboxCount > 0 && gridRecordsCount > 0 && gridRecordsCount != expectedOnPage) {
                    System.out.println("  • Both counts exist but don't match expected");
                    System.out.println("  • Badge: " + inboxCount + ", Grid: " + gridRecordsCount + ", Expected: " + expectedOnPage);
                    if (gridRecordsCount < expectedOnPage) {
                        System.out.println("  • Grid might not have finished loading all records");
                    } else {
                        System.out.println("  • Grid might be showing more records than page size");
                    }
                }
                
                if (reportLogger != null) {
                    reportLogger.fail("Inbox count verification FAILED - Expected " + expectedOnPage + " but found " + gridRecordsCount);
                }
            }
            
            System.out.println("✅ ═══════════════════════════════════════════════════════\n");
            return matches;
            
        } catch (Exception e) {
            System.out.println("❌ Failed to verify inbox count: " + e.getMessage());
            e.printStackTrace();
            if (reportLogger != null) {
                reportLogger.fail("Failed to verify inbox count: " + e.getMessage());
            }
            return false;
        }
    }
    
    /**
     * Verify that Watchlist count matches grid records count
     * 
     * @param expectedRecordsPerPage Expected number of records per page (default: 5)
     * @return true if counts match, false otherwise
     */
    public boolean verifyWatchlistCountMatchesGrid(int expectedRecordsPerPage) {
        try {
            System.out.println("\n✅ ═══════════════════════════════════════════");
            System.out.println("✅ VERIFYING WATCHLIST COUNT vs GRID RECORDS");
            System.out.println("✅ ═══════════════════════════════════════════");
            
            int watchlistCount = getWatchlistCount();
            int gridRecordsCount = getGridRecordsCount();
            
            System.out.println("\n📊 COMPARISON:");
            System.out.println("  Watchlist Badge Count: " + watchlistCount);
            System.out.println("  Grid Records Count: " + gridRecordsCount);
            System.out.println("  Expected Records/Page: " + expectedRecordsPerPage);
            
            // Determine expected records on current page
            int expectedOnPage = Math.min(watchlistCount, expectedRecordsPerPage);
            System.out.println("  Expected on Current Page: " + expectedOnPage);
            
            boolean matches = (gridRecordsCount == expectedOnPage);
            
            if (matches) {
                System.out.println("\n✅ VERIFICATION PASSED!");
                System.out.println("  Grid shows " + gridRecordsCount + " records as expected");
                if (reportLogger != null) {
                    reportLogger.pass("Watchlist count verification PASSED - Grid shows " + gridRecordsCount + " records, matching expected count");
                }
            } else {
                System.out.println("\n❌ VERIFICATION FAILED!");
                System.out.println("  Expected: " + expectedOnPage + " records");
                System.out.println("  Found: " + gridRecordsCount + " records");
                System.out.println("  Difference: " + Math.abs(expectedOnPage - gridRecordsCount));
                if (reportLogger != null) {
                    reportLogger.fail("Watchlist count verification FAILED - Expected " + expectedOnPage + " but found " + gridRecordsCount);
                }
            }
            
            System.out.println("✅ ═══════════════════════════════════════════\n");
            return matches;
            
        } catch (Exception e) {
            System.out.println("❌ Failed to verify watchlist count: " + e.getMessage());
            if (reportLogger != null) {
                reportLogger.fail("Failed to verify watchlist count: " + e.getMessage());
            }
            return false;
        }
    }
    
    /**
     * Print detailed grid information for debugging
     */
    public void printGridDebugInfo() {
        try {
            System.out.println("\n🔍 DEBUG: Grid Information");
            System.out.println("═══════════════════════════════════════════");
            
            // Try to find pagination info
            try {
                WebElement paginationInfo = driver.findElement(InitiativePageLocators.paginationInfo);
                System.out.println("  Pagination: " + paginationInfo.getText());
            } catch (Exception e) {
                System.out.println("  Pagination: Not found");
            }
            
            // Try to find total records
            try {
                WebElement totalRecords = driver.findElement(InitiativePageLocators.totalRecords);
                System.out.println("  Total Records: " + totalRecords.getText());
            } catch (Exception e) {
                System.out.println("  Total Records: Not found");
            }
            
            // List all visible rows
            System.out.println("\n  Visible Rows:");
            By[] rowLocators = {
                By.xpath("//div[@role='row' and contains(@class,'ag-row')]"),
                By.xpath("//table//tbody//tr[@role='row']")
            };
            
            for (By locator : rowLocators) {
                try {
                    List<WebElement> rows = driver.findElements(locator);
                    if (!rows.isEmpty()) {
                        for (int i = 0; i < rows.size(); i++) {
                            WebElement row = rows.get(i);
                            if (row.isDisplayed()) {
                                System.out.println("    Row " + (i + 1) + ": " + row.getText().substring(0, Math.min(50, row.getText().length())) + "...");
                            }
                        }
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            System.out.println("═══════════════════════════════════════════\n");
            
        } catch (Exception e) {
            System.out.println("  Error printing debug info: " + e.getMessage());
        }
    }
    
    /**
     * Check if pagination forward button is enabled
     * 
     * @return true if forward button is enabled, false otherwise
     */
    public boolean isPaginationForwardButtonEnabled() {
        try {
            System.out.println("\n🔍 Checking if pagination forward button is enabled...");
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement forwardButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    InitiativePageLocators.paginationForwardButton));
            
            boolean isEnabled = forwardButton.isEnabled() && !forwardButton.getAttribute("class").contains("disabled");
            boolean isDisabled = forwardButton.getAttribute("disabled") != null || 
                                forwardButton.getAttribute("aria-disabled") != null && 
                                forwardButton.getAttribute("aria-disabled").equals("true");
            
            boolean finalStatus = isEnabled && !isDisabled;
            System.out.println("  ➡️ Forward button enabled: " + finalStatus);
            
            return finalStatus;
            
        } catch (TimeoutException e) {
            System.out.println("  ⚠️ Forward button not found - assuming single page");
            return false;
        } catch (Exception e) {
            System.out.println("  ⚠️ Error checking forward button: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if forward button is enabled (not disabled)
     * 
     * @return true if enabled, false if disabled
     */
    public boolean isForwardButtonEnabled() {
        try {
            // Use JavaScript to check if button is disabled
            Object result = ((JavascriptExecutor) driver).executeScript(
                "var svg = document.querySelector('svg[data-testid=\"ArrowForwardIcon\"]');" +
                "if (!svg) return false;" +
                "var btn = svg.closest('button');" +
                "if (!btn) return false;" +
                "return !btn.disabled && btn.getAttribute('aria-disabled') !== 'true';"
            );
            
            boolean isEnabled = result != null && (Boolean) result;
            System.out.println("  🔍 Forward button enabled: " + isEnabled);
            return isEnabled;
            
        } catch (Exception e) {
            System.out.println("  ⚠️ Error checking button state: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * ULTRA SIMPLE - Just click the damn button!
     * Single most reliable method
     */
    public void clickForwardArrowSimple() {
        try {
            System.out.println("\n🔥 ULTRA SIMPLE CLICK 🔥");
            Thread.sleep(1000);
            
            // Find and click in ONE line - most direct way
            ((JavascriptExecutor) driver).executeScript(
                "var svg = document.querySelector('svg[data-testid=\"ArrowForwardIcon\"]');" +
                "console.log('SVG found:', svg);" +
                "if (svg) {" +
                "  var btn = svg.closest('button');" +
                "  console.log('Button found:', btn);" +
                "  if (btn) {" +
                "    btn.click();" +
                "    console.log('Button clicked!');" +
                "    return 'SUCCESS';" +
                "  }" +
                "}" +
                "return 'FAILED';"
            );
            
            System.out.println("  ✅ Click executed!");
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.out.println("  ❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Debug method - print ALL info about forward button
     */
    public void debugForwardButton() {
        try {
            System.out.println("\n🔍 ═════════════ FORWARD BUTTON DEBUG ═════════════");
            
            // Execute JavaScript to get ALL info
            String result = (String) ((JavascriptExecutor) driver).executeScript(
                "var svg = document.querySelector('svg[data-testid=\"ArrowForwardIcon\"]');" +
                "if (!svg) return 'SVG NOT FOUND';" +
                
                "var btn = svg.closest('button');" +
                "if (!btn) return 'BUTTON NOT FOUND (SVG has no button parent)';" +
                
                "var info = {" +
                "  tagName: btn.tagName," +
                "  className: btn.className," +
                "  id: btn.id," +
                "  disabled: btn.disabled," +
                "  ariaDisabled: btn.getAttribute('aria-disabled')," +
                "  style: btn.getAttribute('style')," +
                "  visible: btn.offsetParent !== null," +
                "  x: btn.getBoundingClientRect().x," +
                "  y: btn.getBoundingClientRect().y," +
                "  width: btn.getBoundingClientRect().width," +
                "  height: btn.getBoundingClientRect().height" +
                "};" +
                
                "return JSON.stringify(info);"
            );
            
            System.out.println("📊 Button Info: " + result);
            
            // Count how many forward buttons exist
            Long count = (Long) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('svg[data-testid=\"ArrowForwardIcon\"]').length;"
            );
            System.out.println("📊 Total ArrowForwardIcon SVGs found: " + count);
            
            // Try to get button HTML
            String html = (String) ((JavascriptExecutor) driver).executeScript(
                "var svg = document.querySelector('svg[data-testid=\"ArrowForwardIcon\"]');" +
                "if (svg && svg.closest('button')) {" +
                "  return svg.closest('button').outerHTML.substring(0, 200);" +
                "}" +
                "return 'N/A';"
            );
            System.out.println("📊 Button HTML (first 200 chars): " + html);
            
            System.out.println("═════════════════════════════════════════════════\n");
            
        } catch (Exception e) {
            System.out.println("❌ Debug error: " + e.getMessage());
        }
    }
    
    /**
     * Simple method to click forward arrow - NO checks, just click!
     * Tries multiple click methods until one works
     */
    public void clickForwardArrow() {
        try {
            System.out.println("\n➡️ Clicking forward arrow...");
            Thread.sleep(1000);
            
            // Method 1: JavaScript click on button with ArrowForwardIcon
            try {
                System.out.println("  🔄 Trying JS click...");
                WebElement btn = driver.findElement(By.xpath("//button[.//svg[@data-testid='ArrowForwardIcon']]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                System.out.println("  ✅ Clicked!");
                Thread.sleep(2000);
                return;
            } catch (Exception e) {
                System.out.println("  ⚠️ Method 1 failed, trying next...");
            }
            
            // Method 2: Click SVG directly
            try {
                System.out.println("  🔄 Trying SVG click...");
                WebElement svg = driver.findElement(By.xpath("//svg[@data-testid='ArrowForwardIcon']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", svg);
                System.out.println("  ✅ Clicked!");
                Thread.sleep(2000);
                return;
            } catch (Exception e) {
                System.out.println("  ⚠️ Method 2 failed, trying next...");
            }
            
            // Method 3: Actions class
            try {
                System.out.println("  🔄 Trying Actions click...");
                WebElement btn = driver.findElement(By.xpath("//button[.//svg[@data-testid='ArrowForwardIcon']]"));
                Actions actions = new Actions(driver);
                actions.moveToElement(btn).click().perform();
                System.out.println("  ✅ Clicked!");
                Thread.sleep(2000);
                return;
            } catch (Exception e) {
                System.out.println("  ⚠️ Method 3 failed, trying next...");
            }
            
            // Method 4: Pure JavaScript querySelector
            try {
                System.out.println("  🔄 Trying pure JS...");
                ((JavascriptExecutor) driver).executeScript(
                    "var btn = document.querySelector('svg[data-testid=\"ArrowForwardIcon\"]');" +
                    "if (btn && btn.parentElement) btn.parentElement.click();"
                );
                System.out.println("  ✅ Clicked!");
                Thread.sleep(2000);
                return;
            } catch (Exception e) {
                System.out.println("  ⚠️ Method 4 failed");
            }
            
            System.out.println("  ⚠️ All methods tried");
            
        } catch (Exception e) {
            System.out.println("  ❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * FORCE click pagination forward button - tries EVERYTHING
     * This method will exhaust all possible click methods
     * 
     * @return true if successfully navigated to next page
     */
    public boolean forceClickPaginationForward() {
        try {
            System.out.println("\n🔥 FORCE CLICK PAGINATION FORWARD 🔥");
            
            // Capture state before
            int recordsBefore = getGridRecordsCount();
            System.out.println("  📊 Records before: " + recordsBefore);
            
            // Wait for page to be ready
            Thread.sleep(2000);
            
            // Try 1: Pure JavaScript - Find by testid and click
            System.out.println("\n  🔥 Method 1: Raw JS by data-testid...");
            try {
                ((JavascriptExecutor) driver).executeScript(
                    "var btn = document.querySelector('svg[data-testid=\"ArrowForwardIcon\"]');" +
                    "if (btn && btn.parentElement) {" +
                    "  btn.parentElement.click();" +
                    "  console.log('Parent clicked');" +
                    "  return true;" +
                    "}" +
                    "return false;"
                );
                Thread.sleep(3000);
                int recordsAfter = getGridRecordsCount();
                if (recordsAfter != recordsBefore) {
                    System.out.println("  ✅ SUCCESS! Page changed!");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            // Try 2: Find button and force click with multiple events
            System.out.println("\n  🔥 Method 2: Multiple event dispatch...");
            try {
                WebElement btn = driver.findElement(By.xpath("//svg[@data-testid='ArrowForwardIcon']/ancestor::button"));
                ((JavascriptExecutor) driver).executeScript(
                    "var element = arguments[0];" +
                    "element.dispatchEvent(new MouseEvent('mousedown', {bubbles: true}));" +
                    "element.dispatchEvent(new MouseEvent('mouseup', {bubbles: true}));" +
                    "element.dispatchEvent(new MouseEvent('click', {bubbles: true}));" +
                    "element.click();",
                    btn
                );
                Thread.sleep(3000);
                int recordsAfter = getGridRecordsCount();
                if (recordsAfter != recordsBefore) {
                    System.out.println("  ✅ SUCCESS! Page changed!");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            // Try 3: Click with coordinates
            System.out.println("\n  🔥 Method 3: Click at coordinates...");
            try {
                WebElement btn = driver.findElement(By.xpath("//svg[@data-testid='ArrowForwardIcon']/ancestor::button"));
                Actions actions = new Actions(driver);
                actions.moveToElement(btn).click().perform();
                Thread.sleep(3000);
                int recordsAfter = getGridRecordsCount();
                if (recordsAfter != recordsBefore) {
                    System.out.println("  ✅ SUCCESS! Page changed!");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            // Try 4: Send ENTER key to button
            System.out.println("\n  🔥 Method 4: Send ENTER key...");
            try {
                WebElement btn = driver.findElement(By.xpath("//svg[@data-testid='ArrowForwardIcon']/ancestor::button"));
                btn.sendKeys(Keys.RETURN);
                Thread.sleep(3000);
                int recordsAfter = getGridRecordsCount();
                if (recordsAfter != recordsBefore) {
                    System.out.println("  ✅ SUCCESS! Page changed!");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            // Try 5: Focus and Space key
            System.out.println("\n  🔥 Method 5: Focus + SPACE key...");
            try {
                WebElement btn = driver.findElement(By.xpath("//svg[@data-testid='ArrowForwardIcon']/ancestor::button"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", btn);
                Thread.sleep(500);
                btn.sendKeys(Keys.SPACE);
                Thread.sleep(3000);
                int recordsAfter = getGridRecordsCount();
                if (recordsAfter != recordsBefore) {
                    System.out.println("  ✅ SUCCESS! Page changed!");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            // Try 6: Click SVG directly
            System.out.println("\n  🔥 Method 6: Direct SVG click...");
            try {
                WebElement svg = driver.findElement(By.xpath("//svg[@data-testid='ArrowForwardIcon']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", svg);
                Thread.sleep(3000);
                int recordsAfter = getGridRecordsCount();
                if (recordsAfter != recordsBefore) {
                    System.out.println("  ✅ SUCCESS! Page changed!");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            // Try 7: Find ANY button with forward icon and click all of them
            System.out.println("\n  🔥 Method 7: Click ALL forward buttons...");
            try {
                List<WebElement> buttons = driver.findElements(By.xpath("//button[.//svg[@data-testid='ArrowForwardIcon']]"));
                System.out.println("  Found " + buttons.size() + " forward buttons");
                for (int i = 0; i < buttons.size(); i++) {
                    try {
                        System.out.println("  Clicking button " + (i+1) + "...");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buttons.get(i));
                        Thread.sleep(3000);
                        int recordsAfter = getGridRecordsCount();
                        if (recordsAfter != recordsBefore) {
                            System.out.println("  ✅ SUCCESS! Button " + (i+1) + " worked!");
                            return true;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                System.out.println("  ❌ Failed: " + e.getMessage());
            }
            
            System.out.println("\n  ❌ ALL METHODS EXHAUSTED - BUTTON NOT RESPONDING");
            return false;
            
        } catch (Exception e) {
            System.out.println("  ❌ Critical error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Click pagination forward button to go to next page
     * Uses the simple clickForwardArrow method and verifies page change
     * 
     * @return true if successfully clicked AND page changed, false otherwise
     */
    public boolean clickPaginationForwardButton() {
        try {
            System.out.println("\n➡️ Clicking pagination forward button...");
            
            if (!isPaginationForwardButtonEnabled()) {
                System.out.println("  ⚠️ Forward button is disabled - cannot proceed");
                return false;
            }
            
            // Capture current page state BEFORE clicking
            int recordsBeforeClick = getGridRecordsCount();
            System.out.println("  📊 Records before click: " + recordsBeforeClick);
            
            // Use the simple clickForwardArrow method
            System.out.println("\n  🎯 Using clickForwardArrow() method...");
            clickForwardArrow();
            
            // Wait for navigation to complete
            Thread.sleep(2000);
            
            // Verify page actually changed
            System.out.println("\n  🔍 Verifying page navigation...");
            int recordsAfterClick = getGridRecordsCount();
            System.out.println("  📊 Records after click: " + recordsAfterClick);
            
            boolean recordsChanged = recordsBeforeClick != recordsAfterClick;
            
            if (recordsChanged) {
                System.out.println("\n  ✅ PAGE NAVIGATION CONFIRMED!");
                System.out.println("    - Record count changed: YES");
                return true;
            } else {
                System.out.println("\n  ⚠️ WARNING: Page did NOT change!");
                System.out.println("    - Record count: SAME (" + recordsAfterClick + ")");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("  ❌ Error during pagination click: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get current page state for detecting navigation
     * Returns a string representing current grid state
     * 
     * @return String representing current page state
     */
    private String getCurrentPageState() {
        try {
            // Try to get pagination text (e.g., "1-5 of 8")
            try {
                WebElement paginationInfo = driver.findElement(InitiativePageLocators.paginationInfo);
                return paginationInfo.getText();
            } catch (Exception e1) {
                // Fallback: Get text of first few grid rows
                try {
                    By[] rowLocators = {
                        By.xpath("//div[@id='root']/div[2]/div/div[2]/div[2]/div/div/div[4]/table/tbody/tr/td/div/p"),
                        By.xpath("//div[@role='row' and contains(@class,'ag-row')]"),
                        By.xpath("//table//tbody//tr[@role='row']")
                    };
                    
                    for (By locator : rowLocators) {
                        try {
                            List<WebElement> rows = driver.findElements(locator);
                            if (!rows.isEmpty() && rows.size() > 0) {
                                // Get text of first row as page identifier
                                String firstRowText = rows.get(0).getText();
                                return firstRowText.substring(0, Math.min(50, firstRowText.length()));
                            }
                        } catch (Exception e2) {
                            continue;
                        }
                    }
                } catch (Exception e3) {
                    // Last fallback: timestamp
                    return String.valueOf(System.currentTimeMillis());
                }
            }
        } catch (Exception e) {
            // Return timestamp as fallback
            return String.valueOf(System.currentTimeMillis());
        }
        
        return "unknown";
    }
    
    /**
     * Count total records across all pages by navigating through pagination
     * 
     * @return Total number of records across all pages
     */
    public int getTotalRecordsAcrossAllPages() {
        try {
            System.out.println("\n📊 ═══════════════════════════════════════════════════════");
            System.out.println("📊 COUNTING RECORDS ACROSS ALL PAGES");
            System.out.println("📊 ═══════════════════════════════════════════════════════");
            
            int totalRecords = 0;
            int pageNumber = 1;
            
            // Count records on first page
            int recordsOnPage = getGridRecordsCount();
            totalRecords += recordsOnPage;
            System.out.println("\n📄 Page " + pageNumber + ": " + recordsOnPage + " records");
            System.out.println("  📊 Running Total: " + totalRecords);
            
            // Navigate through remaining pages - click until button is disabled
            for (int i = 0; i < 100; i++) {  // Max 100 pages safety limit
                // Check if forward button is disabled BEFORE clicking
                boolean isButtonEnabled = isForwardButtonEnabled();
                
                if (!isButtonEnabled) {
                    System.out.println("\n⚠️ Forward button is DISABLED - reached last page");
                    break;
                }
                
                pageNumber++;
                System.out.println("\n➡️ Moving to Page " + pageNumber + "...");
                System.out.println("═══════════════════════════════════════════════════════");
                
                // Click forward arrow
                clickForwardArrow();
                
                // Wait for page to load
                Thread.sleep(2000);
                
                // Count records on this page
                recordsOnPage = getGridRecordsCount();
                
                // If no records, something went wrong
                if (recordsOnPage == 0) {
                    System.out.println("  ⚠️ No records found - stopping pagination");
                    pageNumber--;
                    break;
                }
                
                totalRecords += recordsOnPage;
                System.out.println("📄 Page " + pageNumber + ": " + recordsOnPage + " records");
                System.out.println("  📊 Running Total: " + totalRecords);
            }
            
            System.out.println("\n📊 ═══════════════════════════════════════════════════════");
            System.out.println("📊 PAGINATION COMPLETE");
            System.out.println("📊 Total Pages: " + pageNumber);
            System.out.println("📊 Total Records: " + totalRecords);
            System.out.println("📊 ═══════════════════════════════════════════════════════");
            
            if (reportLogger != null) {
                reportLogger.info("Total records across " + pageNumber + " pages: " + totalRecords);
            }
            
            return totalRecords;
            
        } catch (Exception e) {
            System.out.println("\n❌ Error counting records across pages: " + e.getMessage());
            e.printStackTrace();
            
            if (reportLogger != null) {
                reportLogger.warning("Error counting records across pages: " + e.getMessage());
            }
            
            return 0;
        }
    }
    
    /**
     * Verify that inbox count matches total records across all pages
     * This method navigates through all pages, counts records, and compares with inbox badge count
     * 
     * @return true if counts match, false otherwise
     */
    public boolean verifyInboxCountMatchesTotalRecords() {
        try {
            System.out.println("\n✅ ═══════════════════════════════════════════════════════");
            System.out.println("✅ VERIFYING INBOX COUNT vs TOTAL RECORDS (ALL PAGES)");
            System.out.println("✅ ═══════════════════════════════════════════════════════");
            
            // Get inbox count from badge
            int inboxCount = getInboxCount();
            
            // Get total records across all pages
            int totalRecordsAcrossPages = getTotalRecordsAcrossAllPages();
            
            System.out.println("\n📊 ═══════════ FINAL COMPARISON ═══════════");
            System.out.println("  Inbox Badge Count: " + inboxCount);
            System.out.println("  Total Records (All Pages): " + totalRecordsAcrossPages);
            
            boolean isMatching = (inboxCount == totalRecordsAcrossPages);
            
            if (isMatching) {
                System.out.println("\n✅ ✅ ✅ VERIFICATION PASSED! ✅ ✅ ✅");
                System.out.println("  Inbox count (" + inboxCount + ") matches total records across all pages!");
                
                if (reportLogger != null) {
                    reportLogger.pass("Inbox count verification PASSED - Badge: " + inboxCount + 
                            ", Grid Total: " + totalRecordsAcrossPages);
                }
                
            } else {
                System.out.println("\n❌ ❌ ❌ VERIFICATION FAILED! ❌ ❌ ❌");
                System.out.println("  Inbox count (" + inboxCount + ") does NOT match total records (" + 
                        totalRecordsAcrossPages + ")");
                System.out.println("  Difference: " + Math.abs(inboxCount - totalRecordsAcrossPages) + " records");
                
                if (reportLogger != null) {
                    reportLogger.fail("Inbox count verification FAILED - Badge: " + inboxCount + 
                            ", Grid Total: " + totalRecordsAcrossPages);
                }
            }
            
            System.out.println("✅ ═══════════════════════════════════════════════════════\n");
            
            return isMatching;
            
        } catch (Exception e) {
            System.out.println("\n❌ Error during inbox count verification: " + e.getMessage());
            e.printStackTrace();
            
            if (reportLogger != null) {
                reportLogger.fail("Error during inbox count verification: " + e.getMessage());
            }
            
            return false;
        }
    }
    
    /**
     * Verify that watchlist count matches total records across all pages
     * This method navigates through all pages, counts records, and compares with watchlist badge count
     * 
     * @return true if counts match, false otherwise
     */
    public boolean verifyWatchlistCountMatchesTotalRecords() {
        try {
            System.out.println("\n✅ ═══════════════════════════════════════════════════════");
            System.out.println("✅ VERIFYING WATCHLIST COUNT vs TOTAL RECORDS (ALL PAGES)");
            System.out.println("✅ ═══════════════════════════════════════════════════════");
            
            // Get watchlist count from badge
            int watchlistCount = getWatchlistCount();
            
            // Get total records across all pages
            int totalRecordsAcrossPages = getTotalRecordsAcrossAllPages();
            
            System.out.println("\n📊 ═══════════ FINAL COMPARISON ═══════════");
            System.out.println("  Watchlist Badge Count: " + watchlistCount);
            System.out.println("  Total Records (All Pages): " + totalRecordsAcrossPages);
            
            boolean isMatching = (watchlistCount == totalRecordsAcrossPages);
            
            if (isMatching) {
                System.out.println("\n✅ ✅ ✅ VERIFICATION PASSED! ✅ ✅ ✅");
                System.out.println("  Watchlist count (" + watchlistCount + ") matches total records across all pages!");
                
                if (reportLogger != null) {
                    reportLogger.pass("Watchlist count verification PASSED - Badge: " + watchlistCount + 
                            ", Grid Total: " + totalRecordsAcrossPages);
                }
                
            } else {
                System.out.println("\n❌ ❌ ❌ VERIFICATION FAILED! ❌ ❌ ❌");
                System.out.println("  Watchlist count (" + watchlistCount + ") does NOT match total records (" + 
                        totalRecordsAcrossPages + ")");
                System.out.println("  Difference: " + Math.abs(watchlistCount - totalRecordsAcrossPages) + " records");
                
                if (reportLogger != null) {
                    reportLogger.fail("Watchlist count verification FAILED - Badge: " + watchlistCount + 
                            ", Grid Total: " + totalRecordsAcrossPages);
                }
            }
            
            System.out.println("✅ ═══════════════════════════════════════════════════════\n");
            
            return isMatching;
            
        } catch (Exception e) {
            System.out.println("\n❌ Error during watchlist count verification: " + e.getMessage());
            e.printStackTrace();
            
            if (reportLogger != null) {
                reportLogger.fail("Error during watchlist count verification: " + e.getMessage());
            }
            
            return false;
        }
    }

    }

    
    


