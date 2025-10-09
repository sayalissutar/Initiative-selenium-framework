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

    // ✅ Navigate to Initiative Page
    public void navigateToInitiative() throws Exception {
        waitForSeconds(3); // buffer wait after login

        clickWithFallback(InitiativePageLocators.initree, "Initiative Tree Menu");
        clickWithFallback(InitiativePageLocators.initiativeOption, "Initiative Option");
        clickWithFallback(InitiativePageLocators.initiativeNode, "Initiative Node");

        System.out.println("✅ Navigated to Initiative successfully!");
       
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
        // Wait for the submit comments modal/container
        waitForElementToBeVisible(InitiativePageLocators.submitCommentsContainer, "Submit Comments Modal");

        // Focus and type into the textarea
        waitForElementToBeVisible(InitiativePageLocators.additionalNotes, "Additional Notes");
        try {
            WebElement area = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(InitiativePageLocators.additionalNotes));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", area);
            area.click();
        } catch (Exception ignored) {}

        type(InitiativePageLocators.additionalNotes, notes, "Additional Notes");
        if (reportLogger != null) {
            reportLogger.info("Entered Additional Notes: " + notes);
        }
    }
    
    public void setInitiativestartdate(String startdate) throws Throwable {
        type(InitiativePageLocators.startdate,startdate, "Initiative startdate");
        if (reportLogger != null) {
            reportLogger.info("Entered Initiative startdate: " + startdate);
        }
    }

    public void setInitiativeenddate(String enddate) throws Throwable {
        type(InitiativePageLocators.startdate,enddate, "Initiative enddate");
        if (reportLogger != null) {
            reportLogger.info("Entered Initiative startdate: " + enddate);
        }
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
        waitForElementToBeVisible(InitiativePageLocators.Submit, "Submit Link");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement submitEl = wait.until(ExpectedConditions.presenceOfElementLocated(InitiativePageLocators.Submit));

        // Ensure in view
        try { ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", submitEl); } catch (Exception ignored) {}

        // Try standard click
        try {
            wait.until(ExpectedConditions.elementToBeClickable(InitiativePageLocators.Submit));
            submitEl.click();
        } catch (Exception e1) {
            // Try Actions click
            try {
                new Actions(driver).moveToElement(submitEl).pause(Duration.ofMillis(150)).click().perform();
            } catch (Exception e2) {
                // Try JS click on nearest button ancestor or self
                try {
                    ((JavascriptExecutor) driver).executeScript(
                        "var el=arguments[0]; var btn=el.closest('button'); (btn||el).click();",
                        submitEl
                    );
                } catch (Exception e3) {
                    // Last resort: send ENTER
                    try { submitEl.sendKeys(Keys.ENTER); } catch (Exception ignored) {}
                }
            }
        }

        // Verify it actually fired: wait for toast OR for submit to become not clickable/stale
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
            shortWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(InitiativePageLocators.toastAlert),
                ExpectedConditions.stalenessOf(submitEl),
                ExpectedConditions.not(ExpectedConditions.elementToBeClickable(InitiativePageLocators.Submit))
            ));
            if (reportLogger != null) reportLogger.pass("Submit action verified.");
        } catch (Exception verifyEx) {
            if (reportLogger != null) reportLogger.warning("Submit click did not show confirmation within timeout; verify downstream state.");
        }
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
    public void Clickpopsub() throws Exception {
        clickWithFallback(InitiativePageLocators.popSubmit, "Popup Submit Button");
    }

    }

    
    


