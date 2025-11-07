package Locators;

import org.openqa.selenium.By;

public class InitiativePageLocators {

  
    
     
	  // ✅ Initiative Tree Menu (sidebar hover trigger Final)
   //public static By initree = By.xpath("//div[@class='MuiBox-root css-t8gcwd']");
   
   public static By initree = By.xpath(" //div[@class='pro-sidebar collapsed']");
   
   //public static By initreec = By.xpath("//div[@id='root']");
   
   
   public static By initreec = By.xpath("//div[@class='filter-detail d-flex align-items-center justify-content-start']");
   
  


    // ✅ Sidebar collapsed menu trigger
    public static By hoverMenuTrigger = By.xpath("//div[contains(@class,'pro-sidebar') and contains(@class,'collapsed')]");

    // ✅ Initiative Option (arrow expand/collapse)
    public static By initiativeOption = By.xpath("//span[contains(@class,'pro-arrow')]");

    // ✅ Initiative Node (using normalize-space to avoid whitespace issues)
    public static By initiativeNode = By.xpath("//*[normalize-space(text())='Initiative']");
    // Header of the Initiative page
    public static By pageHeader = By.xpath("//p[@class='MuiTypography-root MuiTypography-body2 css-uqd4be']");
    
  // public static By AddIni=By.xpath("//span[@class='ms-Button-flexContainer flexContainer-173']");
    
    public static By AddIni=By.xpath("//span[@id='id__2']");
    
    //public static By AddIni=By.xpath("//button[@class='ms-Button ms-Button--primary root-175']");
    
    //public static By AddIni=By.xpath("//span[@class='id_2']");
    
  //  public static By SelectINI=By.xpath("//td[contains(text(), 'Quick Change Request')]");
  
    public static By IniTitle=By.xpath("//input[@id='TextField5']");
    
    public static By IniBG =By.xpath("//span[@id='Dropdown20-option']");
    public static By IniOU =By.xpath("//span[@id='Dropdown21-option']");
    public static By startdate =By.xpath("//input[@id='DatePicker24-label']");
    public static By enddate =By.xpath("//input[@id='DatePicker31-label']");
    public static By savedraft =By.xpath("//button[text()='Save as Draft']");
    public static By iniDescription =By.xpath("//textarea[@id='TextField15']");
    // Submit comments modal container and textarea
    public static By submitCommentsContainer = By.xpath("//div[@class='modal-105']"); // The actual modal container
    public static By additionalNotes=By.xpath("//div[@class='modal-105']//textarea"); // Textarea inside the modal (flexible)
    // Alternative specific locators if needed:
    //public static By additionalNotesById = By.xpath("//textarea[@id='TextField220']");
    //public static By additionalNotesByLabel = By.xpath("//label[normalize-space(.)='Comments for Submit']/following::textarea[1]");
    //public static By additionalNotesOld=By.xpath("//div[@class='ms-TextField-fieldGroup fieldGroup-204']");
    
    public static By Pageheaderini=By.xpath("//h5[text()='Initiative']");
    // Predefined filter buttons
    public static By draftFilter = By.xpath("//span[normalize-space()='Draft']");
    public static By inboxFilter = By.xpath("//span[normalize-space()='Inbox']");
    public static By watchlistFilter = By.xpath("//span[normalize-space()='Watchlist']");

    // Search bar/input field
    public static By searchInput = By.xpath("//input[@placeholder='Search']");

    // Total record count and visible count
    public static By totalRecords = By.xpath("//span[contains(text(),'Total')]");
    public static By countRecords = By.xpath("//div[@class='count-info']");
   
    
   public static By toastAlert =  By.xpath("//div[@id=4]//div");
   public static By modalPopup  =  By.xpath("//div[@class='modal-105']");

    public static By getIniTitle() { return IniTitle; }
    public static By getAdditionalNotes() { return additionalNotes; }
    
    
    
    //public static By Submit =  By.xpath("//span[normalize-space(.)='Submit']");
    
   // public static By Submit =  By.xpath(" //span[@style='cursor: pointer; font-size: 10px; color: rgb(59, 130, 246); display: inline-flex; align-items: center; gap: 2px; padding: 4px 7px; background-color: rgb(239, 246, 255); border-radius: 4px; font-weight: 500; border: 1px solid rgb(147, 197, 253); transition: 0.3s; white-space: nowrap; flex-shrink: 0; opacity: 1; transform: translateY(0px)']");
    
    //public static By Submit =  By.xpath("//svg[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-vubbuv']");
    
    // More robust Submit button locators (try most specific to least specific)
    public static By Submit = By.xpath("//div[@id='root']/div[2]/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div/div/span[7]");
    
    // Alternative Submit locators (add more if this one doesn't work)
    public static By SubmitAlt1 = By.xpath("//button[.//svg[@data-testid='SendIcon']]");
    public static By SubmitAlt2 = By.xpath("//span[contains(normalize-space(),'Submit')]");
    public static By SubmitAlt3 = By.xpath("//span[contains(text(),'Submit') and .//svg]");
 
    // Popup Submit button locators (from the modal that appears after clicking Submit)
    // Note: The span id__296 is INSIDE the button, so we need to find the button parent
    public static By popSubmit = By.xpath("//span[@id='id__296']/ancestor::button | //button[.//span[@id='id__296']]");
    public static By popSubmitAlt1 = By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button--primary')]");
    public static By popSubmitAlt2 = By.xpath("//div[@class='modal-105']//button[contains(text(),'Submit')]");
    public static By popSubmitAlt3 = By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button')]");
    public static By popSubmitAlt4 = By.xpath("//button[@id='id__150']");
    public static By popSubmitAlt5 = By.xpath("//span[@id='id__296']"); // Last resort - try clicking the span directly
   
    
    // ✅ Close button (×) after login - Multiple locator options
    public static By closeButton = By.xpath("//button[text()='×' or contains(text(),'×') or normalize-space()='×']");
    public static By closeButtonByStyle = By.xpath("//button[contains(@style, 'border-radius: 50%') and contains(text(),'×')]");
    		
    public static By closeButtonByText = By.xpath("//button[contains(text(),'×')]");
}