package tests;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;

import Base.BaseTest;
import Pages.InitiativePage;
import Utils.ExcelReader;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Step;

/**
 * Initiative Management Test Suite
 * 
 * This class contains all test cases for Initiative module operations
 * including creation, submission, and validation.
 * 
 * @author Automation Team
 * @version 1.0
 */
@Epic("Initiative Management")
@Feature("Initiative Operations")
public class InitiativeTest extends BaseTest {

    /**
     * Generic DataProvider to fetch test data by TC_ID from Excel
     * 
     * @param method Test method to get data for
     * @return Object array containing test data
     * @throws Exception if Excel file cannot be read
     */
    @DataProvider(name = "initiativeData")
    public Object[][] getInitiativeData(Method method) throws Exception {
        String testCaseId = method.getName();
        ExcelReader reader = new ExcelReader("TestdataIni.xlsx", "Login");
        int rowCount = reader.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            String excelTCID = reader.getData(i + 1, 0).trim();

            if (excelTCID.equalsIgnoreCase(testCaseId)) {
                int paramCount = method.getParameterCount();
                Object[][] data = new Object[1][paramCount];

                for (int j = 0; j < paramCount; j++) {
                    String cellValue = reader.getData(i + 1, j + 1);
                    data[0][j] = (cellValue == null) ? "" : cellValue.trim();
                }
                return data;
            }
        }
        return new Object[0][0];
    }

    /**
     * TC_001 - Verify Initiative List Page Header
     * 
     * This test verifies that users can navigate to Initiative page
     * and the page header is displayed correctly.
     */
    @Test(priority = 1, enabled = false)
    @Description("TC_001 - Verify Initiative List Page Header")
    @Story("Initiative Navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_001() throws Throwable {
        initiativePage = new InitiativePage(webDriver, reportLogger);
        navigateToInitiativePage();
        verifyInitiativeHeader();
        System.out.println("✅ Initiative navigation and header verified");
    }

    /**
     * TC_002 - Select Nature of Initiative
     * 
     * This test verifies that users can select Nature of Initiative (NOI)
     * and navigate to initiative creation form.
     */
    @Test(priority = 2, enabled = false, dataProvider = "initiativeData")
    @Description("TC_002 - Select Nature of Initiative")
    @Story("Initiative Creation")
    @Severity(SeverityLevel.NORMAL)
    public void TC_002(String noi) throws Throwable {
        initiativePage = new InitiativePage(webDriver, reportLogger);
        navigateToInitiativePage();
        clickInitiativeBeforeAdd();
        clickAddButton();
        selectNatureOfInitiative(noi);
        verifyInitiativeHeaderAfterAdd();
    }

    /**
     * TC_003 - Create and Submit New Initiative
     * 
     * Complete end-to-end test for creating a new initiative,
     * filling all required fields, and submitting it with comments.
     * 
     * @param noi Nature of Initiative
     * @param title Initiative Title
     * @param description Initiative Description
     * @param bg Business Group
     * @param ou Operating Unit
     * @param startdate Start Date
     * @param enddate End Date
     * @param notes Submission Comments
     */
    @Test(priority = 3, enabled = true, dataProvider = "initiativeData")
    @Description("TC_003 - Create and Submit New Initiative")
    @Story("Initiative Creation")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_003(String noi, String title, String description, String bg, 
                       String ou, String startdate, String enddate, String notes) throws Throwable {
        initiativePage = new InitiativePage(webDriver, reportLogger);
        
        storeParentWindow();
        navigateToInitiativePage();
        clickInitiativeBeforeAdd();
        clickAddButton();
        selectNatureOfInitiative(noi);
        fillInitiativeDetails(title, description, bg, ou, startdate, enddate);
        saveDraftAndSubmit();
        handleSubmitModalWithWindowHandling(notes);
        
        System.out.println("✅ Initiative created successfully - NOI: " + noi + ", Title: " + title);
    }

    /**
     * TC_004 - Verify Validation Alert Message
     * 
     * This test verifies that appropriate validation message is shown
     * when user tries to save draft without filling mandatory fields.
     */
    @Test(priority = 4, enabled = false, dataProvider = "initiativeData")
    @Description("TC_004 - Verify Validation Alert Message")
    @Story("Initiative Validation")
    @Severity(SeverityLevel.NORMAL)
    public void TC_004(String noi) throws Throwable {
        initiativePage = new InitiativePage(webDriver, reportLogger);
        navigateToInitiativePage();
        clickInitiativeBeforeAdd();
        clickAddButton();
        selectNatureOfInitiative(noi);
        saveDraftWithoutTitle();
        verifyAlertMessage();
    }

    // ==================== STEP METHODS ====================

    @Step("Navigate to Initiative Page")
    private void navigateToInitiativePage() throws Throwable {
        initiativePage.navigateToInitiative();
    }

    @Step("Verify Initiative Header")
    private void verifyInitiativeHeader() throws Throwable {
        initiativePage.verifyInitiativeHeader("Initiative Management > Initiative");
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

    @Step("Fill Initiative Details")
    private void fillInitiativeDetails(String title, String description, String bg, 
                                      String ou, String startdate, String enddate) throws Throwable {
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

    @Step("Save Draft Without Title")
    private void saveDraftWithoutTitle() throws Throwable {
        initiativePage.ClickSD();
    }

    @Step("Verify Alert Message")
    private void verifyAlertMessage() throws Throwable {
        initiativePage.verifyInitiativealtmsg("Initiative Title should not be left blank");
    }

    // ==================== WINDOW HANDLING ====================

    /**
     * Print detailed window information for debugging
     * 
     * @param stage Description of current stage
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

    @Step("Handle Submit Modal")
    private void handleSubmitModalWithWindowHandling(String notes) throws Throwable {
        try {
            Thread.sleep(2000);
            printWindowInfo("After clicking Submit");

            int windowCount = initiativePage.getWindowCount();
            System.out.println("📊 Current window count: " + windowCount);

            if (windowCount > 1) {
                System.out.println("🪟 Modal opened as popup window");
                handleModalAsPopupWindow(notes);
                printWindowInfo("After handling popup");
            } else {
                System.out.println("📋 Modal opened as overlay");
                handleModalAsOverlay(notes);
                printWindowInfo("After handling overlay");
            }
        } catch (Exception e) {
            System.err.println("❌ Error handling modal: " + e.getMessage());
            printWindowInfo("On Error");
            
            try {
                initiativePage.switchToParentWindow();
            } catch (Exception ignored) {
                // Ignore if switch fails
            }
            throw e;
        }
    }

    @Step("Handle Modal as Popup Window")
    private void handleModalAsPopupWindow(String notes) throws Throwable {
        boolean windowAppeared = initiativePage.waitForNewWindow(2, 10);

        if (windowAppeared) {
            boolean switched = initiativePage.switchToModalPopupWindow(
                Locators.InitiativePageLocators.modalPopup,
                "Submit Comments Modal"
            );

            if (switched) {
                initiativePage.type(
                    Locators.InitiativePageLocators.additionalNotes,
                    notes,
                    "Additional Notes"
                );

                initiativePage.clickElementInModal(
                    Locators.InitiativePageLocators.popSubmit,
                    "Submit Button"
                );

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
        boolean modalVisible = initiativePage.waitForHTMLModal(
            Locators.InitiativePageLocators.modalPopup,
            15
        );

        if (modalVisible) {
            log.info("✅ Modal appeared successfully");

            Thread.sleep(3000);
            log.info("Waited for modal to be fully interactive");

            initiativePage.typeInModal(
                Locators.InitiativePageLocators.additionalNotes,
                notes,
                "Additional Comments"
            );
            log.info("✅ Typed comments in modal: " + notes);

            Thread.sleep(2000);
            log.info("Waiting before submit");

            initiativePage.Clickpopsub();
            log.info("✅ Clicked Submit in modal");

            Thread.sleep(2000);

            System.out.println("✅ Modal overlay handled successfully");
        } else {
            log.error("❌ Modal did not appear within timeout");
            System.err.println("❌ Failed to handle modal overlay - modal not visible");
            throw new Exception("Modal did not appear within 15 seconds");
        }
    }
}
