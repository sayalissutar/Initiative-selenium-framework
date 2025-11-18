package Locators;

import org.openqa.selenium.By;

/**
 * Page Object Locators for Initiative Management Module
 * 
 * This class contains all locators used in Initiative page automation.
 * Following Page Object Model (POM) design pattern with:
 * - Static locators for reusable elements
 * - Dynamic helper methods for parameterized locators
 * - Alternative locators for robust element finding
 * 
 * DESIGN PRINCIPLES:
 * 1. Keep all static/reusable locators here
 * 2. Use helper methods (getDynamic*) for parameterized locators
 * 3. Group related locators with clear comments
 * 4. Provide alternative locators for unstable elements
 * 
 * @author Automation Team
 * @version 2.0 - Refactored for consistency
 */
public class InitiativePageLocators {

    // ==================== NAVIGATION ====================
    
    /** Initiative tree menu in sidebar */
    public static By initree = By.xpath("//div[@class='pro-sidebar collapsed']");
    
    /** Initiative filter/detail section */
    public static By initreec = By.xpath("//div[@class='filter-detail d-flex align-items-center justify-content-start']");
    
    /** Sidebar collapsed menu trigger */
    public static By hoverMenuTrigger = By.xpath("//div[contains(@class,'pro-sidebar') and contains(@class,'collapsed')]");
    
    /** Initiative option arrow (expand/collapse) */
    public static By initiativeOption = By.xpath("//span[contains(@class,'pro-arrow')]");
    
    /** Initiative node in navigation */
    public static By initiativeNode = By.xpath("//*[normalize-space(text())='Initiative']");
    
    /** All iframes on page */
    public static By allIframes = By.tagName("iframe");
    
    // ==================== PAGE HEADERS ====================
    
    /** Main page header */
    public static By pageHeader = By.xpath("//p[@class='MuiTypography-root MuiTypography-body2 css-uqd4be']");
    
    /** Initiative page header */
    public static By Pageheaderini = By.xpath("//h5[text()='Initiative']");
    
    // ==================== FORM FIELDS ====================
    
    /** Add Initiative button */
    public static By AddIni = By.xpath("//span[@id='id__2']");
    
    /** Initiative Title input field */
    public static By IniTitle = By.xpath("//input[@id='TextField5']");
    
    /** Initiative Description textarea */
    public static By iniDescription = By.xpath("//textarea[@id='TextField15']");
    
    /** Business Group dropdown */
    public static By IniBG = By.xpath("//span[@id='Dropdown20-option']");
    
    /** Operating Unit dropdown */
    public static By IniOU = By.xpath("//span[@id='Dropdown21-option']");
    
    /** Start Date picker */
    public static By startdate = By.xpath("//input[@id='DatePicker24-label']");
    
    /** End Date picker */
    public static By enddate = By.xpath("//input[@id='DatePicker31-label']");
    
    /** Save as Draft button */
    public static By savedraft = By.xpath("//button[text()='Save as Draft']");
    
    // ==================== SUBMIT & MODAL ====================
    
    /** Primary Submit button */
    public static By Submit = By.xpath("//div[@id='root']/div[2]/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div/div/span[7]");
    
    /** Alternative Submit button locators */
    public static By SubmitAlt1 = By.xpath("//button[.//svg[@data-testid='SendIcon']]");
    public static By SubmitAlt2 = By.xpath("//span[contains(normalize-space(),'Submit')]");
    public static By SubmitAlt3 = By.xpath("//span[contains(text(),'Submit') and .//svg]");
    
    /** Modal popup container */
    public static By modalPopup = By.xpath("//div[@class='modal-105']");
    
    /** Submit comments container */
    public static By submitCommentsContainer = By.xpath("//div[@class='modal-105']");
    
    /** Additional notes textarea in modal */
    public static By additionalNotes = By.xpath("//div[@class='modal-105']//textarea");
    
    /** Popup Submit button - finds button containing span */
    public static By popSubmit = By.xpath("//span[@id='id__296']/ancestor::button | //button[.//span[@id='id__296']]");
    
    /** Alternative popup Submit button locators */
    public static By popSubmitAlt1 = By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button--primary')]");
    public static By popSubmitAlt2 = By.xpath("//div[@class='modal-105']//button[contains(text(),'Submit')]");
    public static By popSubmitAlt3 = By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button')]");
    public static By popSubmitAlt4 = By.xpath("//button[@id='id__150']");
    public static By popSubmitAlt5 = By.xpath("//span[@id='id__296']");
    
    // ==================== FILTERS & SEARCH ====================
    
    /** Draft filter button */
    public static By draftFilter = By.xpath("//span[normalize-space()='Draft']");
    
    /** Inbox filter button - Primary */
    public static By inboxFilter = By.xpath("//span[id='FltrCountInbox'] | //span[normalize-space()='Inbox'] | //button[contains(.,'Inbox')]");
    
    /** Inbox filter button - Alternative locators */
    public static By inboxFilterAlt1 = By.xpath("//span[id='FltrCountInbox']");
    public static By inboxFilterAlt2 = By.xpath("//button[contains(.,'Inbox')]");
    public static By inboxFilterAlt3 = By.xpath("//span[normalize-space()='Inbox']/parent::button");
    
    /** Watchlist filter button - Primary */
    public static By watchlistFilter = By.xpath("//span[id='FltrCountWatchlist'] | //span[normalize-space()='Watchlist'] | //button[contains(.,'Watchlist')]");
    
    /** Watchlist filter button - Alternative locators */
    public static By watchlistFilterAlt1 = By.xpath("//span[id='FltrCountWatchlist']");
    public static By watchlistFilterAlt2 = By.xpath("//button[contains(.,'Watchlist')]");
    public static By watchlistFilterAlt3 = By.xpath("//span[normalize-space()='Watchlist']/parent::button");
    
    /** Search input field */
    public static By searchInput = By.xpath("//input[@placeholder='Search']");
    
    // ==================== RECORDS & ALERTS ====================
    
    /** Total records count */
    public static By totalRecords = By.xpath("//span[contains(text(),'Total')]");
    
    /** Visible records count */
    public static By countRecords = By.xpath("//div[@class='count-info']");
    
    /** Inbox count displayed on filter badge */
    public static By inboxCount = By.xpath("//span[id='FltrCountInbox'] | //span[normalize-space()='Inbox']/following-sibling::span | //button[contains(.,'Inbox')]//span[contains(@class,'count')]");
    
    /** Watchlist count displayed on filter badge */
    public static By watchlistCount = By.xpath("//span[id='FltrCountWatchlist'] | //span[normalize-space()='Watchlist']/following-sibling::span | //button[contains(.,'Watchlist')]//span[contains(@class,'count')]");
    
    /** Grid rows/records in the table */
    public static By gridRows = By.xpath("//div[contains(@class,'ag-center-cols-container')]//div[@role='row'] | //table//tbody//tr[@role='row']");
    
    /** Grid data rows (excluding headers) */
    public static By gridDataRows = By.xpath("//div[@role='gridcell' and @col-id] | //tbody//tr[@role='row' and not(contains(@class,'header'))]");
    
    /** Pagination info */
    public static By paginationInfo = By.xpath("//div[contains(@class,'pagination')]//span[contains(text(),'of')]");
    
    /** Records per page dropdown */
    public static By recordsPerPageDropdown = By.xpath("//select[contains(@class,'page-size')] | //div[contains(@class,'page-size')]//select");
    
    /** Pagination forward/next page button */
    public static By paginationForwardButton = By.xpath("//svg[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-vubbuv']");
          
    
    /** Pagination backward/previous page button */
    public static By paginationBackwardButton = By.xpath(
            "//button[.//svg[@data-testid='ArrowBackIcon']] | " +
            "//svg[@data-testid='ArrowBackIcon']/parent::button | " +
            "//svg[@data-testid='ArrowBackIcon']/ancestor::button | " );
            
    
    /** Toast alert message */
    public static By toastAlert = By.xpath("//div[@id=4]//div");
    
    // ==================== CLOSE BUTTONS ====================
    
    /** Close button (standard) */
    public static By closeButton = By.xpath("//button[text()='×' or contains(text(),'×') or normalize-space()='×']");
    
    /** Close button by style */
    public static By closeButtonByStyle = By.xpath("//button[contains(@style, 'border-radius: 50%') and contains(text(),'×')]");
    
    /** Close button by text */
    public static By closeButtonByText = By.xpath("//button[contains(text(),'×')]");
    
    // ==================== MODAL SUBMIT LOCATORS (FALLBACKS) ====================
    
    /** Array of fallback locators for modal submit button - Try in order for reliability */
    public static By[] modalSubmitButtonLocators = {
        By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button--primary')]"), // Most common
        By.xpath("//div[@class='modal-105']//button[contains(text(),'Submit')]"),
        By.xpath("//div[@class='modal-105']//button[contains(.,'Submit')]"),
        By.xpath("//div[@class='modal-105']//button"),  // ANY button in modal (aggressive)
        popSubmitAlt1,
        popSubmitAlt2,
        popSubmitAlt3
    };
    
    /** All buttons in modal (for debugging) */
    public static By allModalButtons = By.xpath("//div[@class='modal-105']//button");
    
    /** All spans with ID in modal (for debugging) */
    public static By allModalSpans = By.xpath("//div[@class='modal-105']//span[contains(@id,'id__')]");
    
    // ==================== DATE PICKER ALTERNATIVE LOCATORS ====================
    
    /** Alternative start date locator (without -label suffix) */
    public static By startdateAlt = By.xpath("//input[@id='DatePicker24']");
    
    /** Start date by placeholder */
    public static By startdateByPlaceholder = By.xpath("//input[contains(@placeholder,'start') or contains(@placeholder,'Start')]");
    
    /** Alternative end date locator (without -label suffix) */
    public static By enddateAlt = By.xpath("//input[@id='DatePicker31']");
    
    /** Second date input (for end date) */
    public static By secondDateInput = By.xpath("(//input[@type='text' and contains(@id,'DatePicker')])[2]");
    
    /** End date by placeholder */
    public static By enddateByPlaceholder = By.xpath("//input[contains(@placeholder,'end') or contains(@placeholder,'End')]");
    
    /** End date near label */
    public static By enddateNearLabel = By.xpath("//label[contains(text(),'End') or contains(text(),'end')]/following-sibling::input | //label[contains(text(),'End') or contains(text(),'end')]/..//input");
    
    // ==================== INBOX & WATCHLIST COUNT LOCATORS ====================
    
    /** Array of inbox count locators (fallback strategies) */
    public static By[] inboxCountLocators = {
        By.xpath("//span[id='FltrCountInbox']"),
        By.xpath("//span[normalize-space()='Inbox']/following-sibling::span"),
        By.xpath("//span[normalize-space()='Inbox']/..//span[contains(@class,'count')]"),
        By.xpath("//span[normalize-space()='Inbox']/..//span[contains(@class,'badge')]"),
        By.xpath("//button[contains(.,'Inbox')]//span[contains(@class,'count')]"),
        By.xpath("//button[contains(.,'Inbox')]")  // Try button itself
    };
    
    /** Array of watchlist count locators (fallback strategies) */
    public static By[] watchlistCountLocators = {
        By.xpath("//span[id='FltrCountWatchlist']"),
        By.xpath("//span[normalize-space()='Watchlist']/following-sibling::span"),
        By.xpath("//span[normalize-space()='Watchlist']/..//span[contains(@class,'count')]"),
        By.xpath("//span[normalize-space()='Watchlist']/..//span[contains(@class,'badge')]"),
        By.xpath("//button[contains(.,'Watchlist')]//span[contains(@class,'count')]")
    };
    
    // ==================== GRID ROW LOCATORS ====================
    
    /** Array of grid row locators (try in order for compatibility) */
    public static By[] gridRowLocators = {
        By.xpath("//div[@id='root']/div[2]/div/div[2]/div[2]/div/div/div[4]/table/tbody/tr/td/div/p"),
        By.xpath("//div[@role='row' and contains(@class,'ag-row')]"),
        By.xpath("//div[contains(@class,'ag-center-cols-container')]//div[@role='row']"),
        By.xpath("//table//tbody//tr[@role='row']"),
        By.xpath("//div[@role='gridcell']/../.."),
        By.xpath("//div[contains(@class,'data-grid')]//div[@role='row']"),
        By.xpath("//div[contains(@class,'ag-row')]"),  // Simplified AG Grid
        By.xpath("//tr[contains(@class,'ag-row')]")    // Table-based AG Grid
    };
    
    // ==================== DYNAMIC HELPER METHODS ====================
    
    /**
     * Get dynamic locator for NOI (Number of Initiatives) option by value
     * @param noiValue The NOI value text to search for
     * @return By locator for the specific NOI option
     */
    public static By getDynamicNOIOption(String noiValue) {
        return By.xpath("//div[@class='MuiBox-root css-ah0zvi']//td[normalize-space(text())='" + noiValue + "']");
    }
    
    /**
     * Get dynamic locator for any option by normalized text
     * @param optionText The option text to search for
     * @return By locator for the option
     */
    public static By getDynamicOptionByText(String optionText) {
        return By.xpath("//*[normalize-space(text())='" + optionText + "']");
    }
    
    /**
     * Get dynamic locator for Business Group dropdown option
     * @param bgName The Business Group name
     * @return By locator for the BG option
     */
    public static By getDynamicBGOption(String bgName) {
        return By.xpath("//*[normalize-space(text())='" + bgName + "']");
    }
    
    /**
     * Get dynamic locator for Operating Unit dropdown option
     * @param ouName The Operating Unit name
     * @return By locator for the OU option
     */
    public static By getDynamicOUOption(String ouName) {
        return By.xpath("//*[normalize-space(text())='" + ouName + "']");
    }
    
    /**
     * Get dynamic locator for textarea by ID
     * @param textareaId The ID of the textarea
     * @return By locator for the textarea
     */
    public static By getDynamicTextareaById(String textareaId) {
        return By.xpath("//textarea[@id='" + textareaId + "']");
    }
    
    /**
     * Get dynamic locator for modal by xpath
     * @param modalXPath The XPath string for modal
     * @return By locator for the modal
     */
    public static By getDynamicModalByXPath(String modalXPath) {
        return By.xpath(modalXPath);
    }
    
    /**
     * Get dynamic modal close button
     * @return By locator for modal close button
     */
    public static By getModalCloseButton() {
        return By.xpath("//div[contains(@class, 'modal')]//button[contains(@class, 'ms-Button--icon')]");
    }
    
    /**
     * Get dynamic modal submit button with text
     * @param submitText The text on the submit button (e.g., "Submit")
     * @return By locator for modal submit button
     */
    public static By getDynamicModalSubmitButton(String submitText) {
        return By.xpath("//button[contains(@class, 'ms-Button--primary') and .//span[normalize-space(text())='" + submitText + "']]");
    }
    
    // ==================== GETTER METHODS (Deprecated - Use direct access) ====================
    
    /**
     * Get Initiative Title locator
     * @return By locator for Initiative Title field
     * @deprecated Use InitiativePageLocators.IniTitle directly
     */
    @Deprecated
    public static By getIniTitle() {
        return IniTitle;
    }
    
    /**
     * Get Additional Notes locator
     * @return By locator for Additional Notes textarea
     * @deprecated Use InitiativePageLocators.additionalNotes directly
     */
    @Deprecated
    public static By getAdditionalNotes() {
        return additionalNotes;
    }
}
