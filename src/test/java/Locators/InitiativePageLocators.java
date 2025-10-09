package Locators;

import org.openqa.selenium.By;

public class InitiativePageLocators {

  
    
     
	  // ✅ Initiative Tree Menu (sidebar hover trigger Final)
   public static By initree = By.xpath("//div[@class='MuiBox-root css-t8gcwd']");
   
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
    
    //public static By AddIni=By.xpath("//span[@class='id_2']");
    
  //  public static By SelectINI=By.xpath("//td[contains(text(), 'Quick Change Request')]");
  
    public static By IniTitle=By.xpath("//input[@id='TextField5']");
    
    public static By IniBG =By.xpath("//span[@id='Dropdown20-option']");
    public static By IniOU =By.xpath("//span[@id='Dropdown21-option']");
    public static By startdate =By.xpath("//input[@class='ms-TextField-field field-231']");
    public static By enddate =By.xpath("//input[@aria-labelledby='TextFieldLabel35']");
    public static By savedraft =By.xpath("//button[text()='Save as Draft']");
    public static By iniDescription =By.xpath("//textarea[@id='TextField15']");
    // Submit comments modal container and textarea
    public static By submitCommentsContainer = By.xpath("//div[contains(@class,'ms-TextField') and .//label[normalize-space(.)='Comments for Submit']]");
    //public static By additionalNotes = By.xpath("//label[normalize-space(.)='Comments for Submit']/following::textarea[@id='TextField561'][1]");
    public static By additionalNotes=By.xpath("//textarea[@id='TextField74']");
    
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
    
    public static By Submit =  By.xpath(" //span[@style='cursor: pointer; margin-right: 0.3rem; margin-left: 0.3rem; font-size: 0.9em;' and normalize-space(text())='Submit']");
    
    
    public static By popSubmit =  By.xpath(" ////button[@class='ms-Button ms-Button--primary root-264']");
}