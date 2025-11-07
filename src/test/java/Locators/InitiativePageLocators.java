package Locators;

import org.openqa.selenium.By;

/**
 * Page Object Locators for Initiative Management Module
 * 
 * This class contains all locators used in Initiative page automation.
 * Following Page Object Model (POM) design pattern.
 * 
 * @author Automation Team
 * @version 1.0
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
    
    /** Inbox filter button */
    public static By inboxFilter = By.xpath("//span[normalize-space()='Inbox']");
    
    /** Watchlist filter button */
    public static By watchlistFilter = By.xpath("//span[normalize-space()='Watchlist']");
    
    /** Search input field */
    public static By searchInput = By.xpath("//input[@placeholder='Search']");
    
    // ==================== RECORDS & ALERTS ====================
    
    /** Total records count */
    public static By totalRecords = By.xpath("//span[contains(text(),'Total')]");
    
    /** Visible records count */
    public static By countRecords = By.xpath("//div[@class='count-info']");
    
    /** Toast alert message */
    public static By toastAlert = By.xpath("//div[@id=4]//div");
    
    // ==================== CLOSE BUTTONS ====================
    
    /** Close button (standard) */
    public static By closeButton = By.xpath("//button[text()='×' or contains(text(),'×') or normalize-space()='×']");
    
    /** Close button by style */
    public static By closeButtonByStyle = By.xpath("//button[contains(@style, 'border-radius: 50%') and contains(text(),'×')]");
    
    /** Close button by text */
    public static By closeButtonByText = By.xpath("//button[contains(text(),'×')]");
    
    // ==================== GETTER METHODS ====================
    
    /**
     * Get Initiative Title locator
     * @return By locator for Initiative Title field
     */
    public static By getIniTitle() {
        return IniTitle;
    }
    
    /**
     * Get Additional Notes locator
     * @return By locator for Additional Notes textarea
     */
    public static By getAdditionalNotes() {
        return additionalNotes;
    }
}
