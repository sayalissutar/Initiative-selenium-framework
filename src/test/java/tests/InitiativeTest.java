package tests;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;

import Base.BaseTest;
import Pages.InitiativePage;
import Utils.ExcelReader;

// ✅ Allure Imports
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Step;

@Epic("Initiative Management")
@Feature("Initiative Operations")
public class InitiativeTest extends BaseTest {

	// ✅ Generic DataProvider to fetch data by TC_ID from Excel
	@DataProvider(name = "initiativeData")
	public Object[][] getInitiativeData(Method method) throws Exception {
	    String testCaseId = method.getName(); // use test method name as TC_ID
	    ExcelReader reader = new ExcelReader("TestdataIni.xlsx", "Login"); 
	    int rowCount = reader.getRowCount();

	    for (int i = 0; i < rowCount; i++) {
	        String excelTCID = reader.getData(i + 1, 0).trim(); // first col = TC_ID

	        if (excelTCID.equalsIgnoreCase(testCaseId)) {
	            int paramCount = method.getParameterCount();  // number of args in test method
	            Object[][] data = new Object[1][paramCount];

	            for (int j = 0; j < paramCount; j++) {
	                String cellValue = reader.getData(i + 1, j + 1); // j+1 bcz col0 = TC_ID
	                data[0][j] = (cellValue == null) ? "" : cellValue.trim();
	            }
	            return data;
	        }
	    }
	    return new Object[0][0]; // No match found
	}


    @Test(priority = 1, enabled = false)
    @Description("TC_001 - Verify Initiative List Page Header with Close Button")
    @Story("Initiative Navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_001() throws Throwable {
        initiativePage = new InitiativePage(webDriver, reportLogger);
        
        // Step 1: Click close button after login
        //clickCloseButtonAfterLogin();
        
        // Step 2: Navigate to Initiative tree
        navigateToInitiativePage();
        
        // Step 3: Verify header
        verifyInitiativeHeader();
        
        System.out.println("✅ Initiative navigation and header verified Correct");
    }

	/*
	 * @Step("Click Close Button After Login") private void
	 * clickCloseButtonAfterLogin() throws Throwable {
	 * initiativePage.clickCloseButton();
	 * reportLogger.info("Close button clicked after login"); }
	 */

    @Step("Navigate to Initiative Page")
    private void navigateToInitiativePage() throws Throwable {
        initiativePage.navigateToInitiative();
    }

    @Step("Verify Initiative Header")
    private void verifyInitiativeHeader() throws Throwable {
        initiativePage.verifyInitiativeHeader("Initiative Management > Initiative");
    }

    // ✅ This now uses only NOI from Excel
    @Test(priority = 2, enabled = false, dataProvider = "initiativeData")
    @Description("TC_002 - Select Nature of Initiative")
    @Story("Initiative Creation")
    @Severity(SeverityLevel.NORMAL)
    public void TC_002(String noi) throws Throwable {
        navigateToInitiativePage();
        clickInitiativeBeforeAdd();
        clickAddButton();
        selectNatureOfInitiative(noi);
        verifyInitiativeHeaderAfterAdd();
    }

    @Step("Click Initiative Before Add")
    private void clickInitiativeBeforeAdd() throws Throwable {
        initiativePage.Initiativebeforeclickadd();
    }

    @Step("Click Add Button")
    private void clickAddButton() throws Throwable {
        initiativePage.ClickADD();
    }

    @Step("Select Nature of Initiative: {noi}")
    private void selectNatureOfInitiative(String noi) throws Throwable {
        initiativePage.SelectNOI(noi);
    }

    @Step("Verify Initiative Header After Add")
    private void verifyInitiativeHeaderAfterAdd() throws Throwable {
        initiativePage.verifyInitiativeHeaderini("Initiative");
    }

    // ✅ This now uses full row data from Excel with Window Handle Management
    @Test(priority = 3, enabled = true, dataProvider = "initiativeData")
    @Description("TC_003 - Create New Initiative with Window Handle Management")
    @Story("Initiative Creation")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_003(String noi, String title, String description, String bg, String ou, String startdate, String enddate, String notes) throws Throwable {
        // Store parent window at the start
        storeParentWindow();
        
        // Step 1: Click close button after login (if present)
       // clickCloseButtonAfterLogin();
        
        // Step 2: Navigate and create initiative
        navigateToInitiativePage();
        clickInitiativeBeforeAdd();
        clickAddButton();
        selectNatureOfInitiative(noi);
        fillInitiativeDetails(title, description, bg, ou, startdate, enddate);
        saveDraftAndSubmit();
        
        // Handle modal with window handle management
        handleSubmitModalWithWindowHandling(notes);
        
        System.out.println("✅ Initiative created successfully with NOI: " + noi + ", Title: " + title);
    }

    @Step("Fill Initiative Details - Title: {title}, BG: {bg}, OU: {ou}")
    private void fillInitiativeDetails(String title, String description, String bg, String ou, String startdate, String enddate) throws Throwable {
        initiativePage.setInitiativeTitle(title);
        initiativePage.setInitiativedescription(description);
        initiativePage.selectInitiativeBGWithActions(bg);
        initiativePage.selectInitiativeOUWithActions(ou);
        initiativePage.setInitiativestartdate(startdate);
        initiativePage.setInitiativeenddate(enddate);
    }

    @Step("Save Draft and Submit Initiative")
    private void saveDraftAndSubmit() throws Throwable {
        initiativePage.ClickSD();
        initiativePage.waitForSubmit();
        initiativePage.ClickSubmit();
    }

    @Step("Store Parent Window")
    private void storeParentWindow() {
        initiativePage.storeParentWindow();
        printWindowInfo("After storing parent");
    }
    
    /**
     * Debug method to print all window information
     */
    private void printWindowInfo(String stage) {
        try {
            java.util.Set<String> allHandles = webDriver.getWindowHandles();
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🔍 WINDOW INFO - " + stage);
            System.out.println("=".repeat(60));
            System.out.println("📊 Total Windows: " + allHandles.size());
            
            int index = 0;
            for (String handle : allHandles) {
                boolean isCurrent = handle.equals(webDriver.getWindowHandle());
                String marker = isCurrent ? "👉 CURRENT" : "   ";
                System.out.println(marker + " Window " + index + ": " + handle);
                
                // Switch to window and print title
                webDriver.switchTo().window(handle);
                String title = webDriver.getTitle();
                System.out.println("    Title: " + (title.isEmpty() ? "(No Title)" : title));
                
                index++;
            }
            System.out.println("=".repeat(60) + "\n");
        } catch (Exception e) {
            System.out.println("⚠️ Error printing window info: " + e.getMessage());
        }
    }
    
    @Step("Handle Submit Modal with Window Handling")
    private void handleSubmitModalWithWindowHandling(String notes) throws Throwable {
        try {
            // Wait briefly for modal to appear
            Thread.sleep(2000);
            
            // Print window info after clicking Submit
            printWindowInfo("After clicking Submit");
            
            // Get current window count
            int windowCount = initiativePage.getWindowCount();
            System.out.println("📊 Current window count: " + windowCount);
            
            if (windowCount > 1) {
                // Modal opened as popup window
                System.out.println("🪟 Modal opened as popup window");
                handleModalAsPopupWindow(notes);
                
                // Print window info after handling popup
                printWindowInfo("After handling popup");
            } else {
                // Modal opened as overlay
                System.out.println("📋 Modal opened as overlay");
                handleModalAsOverlay(notes);
                
                // Print window info after handling overlay
                printWindowInfo("After handling overlay");
            }
        } catch (Exception e) {
            System.err.println("❌ Error handling modal: " + e.getMessage());
            printWindowInfo("On Error");
            // Try to recover by switching back to parent
            try {
                initiativePage.switchToParentWindow();
            } catch (Exception ignored) {}
            throw e;
        }
    }
    
    @Step("Handle Modal as Popup Window")
    private void handleModalAsPopupWindow(String notes) throws Throwable {
        // Wait for new window to appear
        boolean windowAppeared = initiativePage.waitForNewWindow(2, 10);
        
        if (windowAppeared) {
            // Switch to popup window
            boolean switched = initiativePage.switchToModalPopupWindow(
                Locators.InitiativePageLocators.modalPopup,
                "Submit Comments Modal"
            );
            
            if (switched) {
                // Enter notes in popup
                initiativePage.type(
                    Locators.InitiativePageLocators.additionalNotes, 
                    notes, 
                    "Additional Notes"
                );
                
                // Click Submit button in popup
                initiativePage.clickElementInModal(
                    Locators.InitiativePageLocators.popSubmit,
                    "Submit Button"
                );
                
                // Close popup and return to parent
                initiativePage.closeCurrentWindowAndSwitchToParent();
                System.out.println("✅ Modal popup handled successfully");
            } else {
                System.err.println("❌ Failed to switch to modal popup");
            }
        } else {
            System.err.println("❌ New window did not appear within timeout");
        }
    }
    
    @Step("Handle Modal as Overlay")
    private void handleModalAsOverlay(String notes) throws Throwable {
        // Step 1: Wait for HTML modal to appear
        boolean modalVisible = initiativePage.waitForHTMLModal(
            Locators.InitiativePageLocators.modalPopup,
            15  // Wait up to 15 seconds
        );
        
        if (modalVisible) {
            log.info("✅ Modal appeared successfully");
            
            // Step 2: Wait extra time for modal to be fully ready
            Thread.sleep(3000);
            log.info("Waited for modal to be fully interactive");
            
            // Step 3: Type in the modal textarea using typeInModal method
            // This will use the enhanced setAdditionalNotes method with 12 strategies
            initiativePage.typeInModal(
                Locators.InitiativePageLocators.additionalNotes,
                notes,
                "Additional Comments"
            );
            log.info("✅ Typed comments in modal: " + notes);
            
            // Step 4: Wait before clicking submit
            Thread.sleep(2000);
            log.info("Waiting before submit");
            
            // Step 5: Click Submit button INSIDE the modal popup
            // Using Clickpopsub() which has 15 strategies including Tab navigation
            initiativePage.Clickpopsub();
            log.info("✅ Clicked Submit in modal");
            
            // Step 6: Wait for modal to close
            Thread.sleep(2000);
            
            System.out.println("✅ Modal overlay handled successfully");
        } else {
            log.error("❌ Modal did not appear within timeout");
            System.err.println("❌ Failed to handle modal overlay - modal not visible");
            throw new Exception("Modal did not appear within 15 seconds");
        }
    }
    
  
    
  
    

    @Test(priority = 4, enabled = false, dataProvider = "initiativeData")
    @Description("TC_004 - Verify Alert Message")
    @Story("Initiative Validation")
    @Severity(SeverityLevel.NORMAL)
    public void TC_004(String noi) throws Throwable {
        navigateToInitiativePage();
        clickInitiativeBeforeAdd();
        clickAddButton();
        selectNatureOfInitiative(noi);
        saveDraftWithoutTitle();
        verifyAlertMessage();
    }

    @Step("Save Draft Without Title to Trigger Alert")
    private void saveDraftWithoutTitle() throws Throwable {
        initiativePage.ClickSD();
    }

    @Step("Verify Alert Message")
    private void verifyAlertMessage() throws Throwable {
        initiativePage.verifyInitiativealtmsg("Initiative Title should not be left blank");
    }

    
    
    
    
}
