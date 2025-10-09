package tests;

import org.testng.annotations.Test;
import Base.BaseTest;
import Locators.InitiativePageLocators;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;

/**
 * Example test class demonstrating modal handling utilities
 * 
 * This class shows practical examples of handling different types of modals
 * in your Initiative application.
 * 
 * @author Automation Team
 * @date October 2025
 */
public class ModalTestExample extends BaseTest {

    /**
     * Example 1: Handle Submit Modal as HTML Overlay
     * Use this if your modal appears as an overlay on the same page
     */
    @Test(priority = 1, enabled = false)
    @Description("Handle Submit Comments Modal - HTML Overlay Approach")
    @Severity(SeverityLevel.CRITICAL)
    public void testSubmitModalAsOverlay() throws Throwable {
        try {
            // Navigate to initiative
            navigateToInitiativePage();
            
            // Create initiative
            prepareInitiativeForm();
            
            // Click Submit button (opens modal)
            initiativePage.ClickSubmit();
            log.info("Clicked Submit button");
            
            // Wait for modal to appear
            boolean modalVisible = initiativePage.waitForHTMLModal(
                InitiativePageLocators.modalPopup, 
                15
            );
            
            if (modalVisible) {
                log.info("✅ Modal appeared successfully");
                
                // Type comments in modal
                String comments = "Submitting initiative for approval. Please review and approve.";
                initiativePage.typeInModal(
                    InitiativePageLocators.additionalNotes,
                    comments,
                    "Additional Comments"
                );
                
                // Click Submit button in modal
                initiativePage.clickElementInModal(
                    InitiativePageLocators.Submit,
                    "Submit Button"
                );
                
                // Wait for modal to close
                Thread.sleep(2000);
                
                log.info("✅ Modal submitted successfully");
                reportLogger.pass("Submit modal handled successfully as overlay");
                
            } else {
                log.error("❌ Modal did not appear");
                reportLogger.fail("Modal did not appear within timeout");
            }
            
        } catch (Exception e) {
            log.error("Error handling modal: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Example 2: Handle Submit Modal as Popup Window
     * Use this if your modal opens as a new window/tab
     */
    @Test(priority = 2, enabled = false)
    @Description("Handle Submit Comments Modal - Popup Window Approach")
    @Severity(SeverityLevel.CRITICAL)
    public void testSubmitModalAsPopup() throws Throwable {
        try {
            // Store parent window FIRST
            initiativePage.storeParentWindow();
            log.info("Parent window stored");
            
            // Navigate to initiative
            navigateToInitiativePage();
            
            // Create initiative
            prepareInitiativeForm();
            
            // Click Submit button (opens popup)
            initiativePage.ClickSubmit();
            log.info("Clicked Submit button");
            
            // Wait for new window to appear
            boolean newWindowAppeared = initiativePage.waitForNewWindow(2, 10);
            
            if (newWindowAppeared) {
                log.info("✅ Popup window detected");
                
                // Switch to popup window
                boolean switched = initiativePage.switchToModalPopupWindow(
                    InitiativePageLocators.modalPopup,
                    "Submit Comments Modal"
                );
                
                if (switched) {
                    log.info("✅ Switched to popup successfully");
                    
                    // Type comments in popup
                    String comments = "Submitting for approval via popup";
                    initiativePage.typeInModal(
                        InitiativePageLocators.additionalNotes,
                        comments,
                        "Additional Comments"
                    );
                    
                    // Click Submit in popup
                    initiativePage.clickElementInModal(
                        InitiativePageLocators.Submit,
                        "Submit Button"
                    );
                    
                    // Close popup and return to parent
                    initiativePage.closeCurrentWindowAndSwitchToParent();
                    
                    log.info("✅ Popup handled and closed");
                    reportLogger.pass("Submit modal handled successfully as popup");
                } else {
                    log.error("❌ Failed to switch to popup");
                    reportLogger.fail("Failed to switch to popup window");
                }
            } else {
                log.error("❌ New window did not appear");
                reportLogger.fail("Popup window did not appear within timeout");
            }
            
        } catch (Exception e) {
            log.error("Error handling popup: " + e.getMessage(), e);
            // Try to recover
            try {
                initiativePage.switchToParentWindow();
            } catch (Exception ignored) {}
            throw e;
        }
    }

    /**
     * Example 3: Smart Modal Handler - Auto-detects modal type
     * RECOMMENDED: Use this approach when you're not sure of the modal type
     */
    @Test(priority = 3, enabled = true)
    @Description("Handle Submit Comments Modal - Auto-detect Approach (RECOMMENDED)")
    @Severity(SeverityLevel.CRITICAL)
    public void testSubmitModalAutoDetect() throws Throwable {
        try {
            // Store parent window (safe to do always)
            initiativePage.storeParentWindow();
            log.info("Parent window stored");
            
            // Navigate to initiative
            navigateToInitiativePage();
            
            // Create initiative
            prepareInitiativeForm();
            
            // Click Submit button
            initiativePage.ClickSubmit();
            log.info("🔘 Clicked Submit button");
            
            // Wait for modal to load
            Thread.sleep(2000);
            
            // Auto-detect modal type
            int windowCount = initiativePage.getWindowCount();
            log.info("📊 Window count: " + windowCount);
            
            String comments = "Submitting initiative for approval. Please review.";
            
            if (windowCount > 1) {
                // Modal is a POPUP WINDOW
                handleModalAsPopup(comments);
                
            } else {
                // Modal is an OVERLAY
                handleModalAsOverlay(comments);
            }
            
            log.info("✅ Initiative submitted successfully!");
            reportLogger.pass("Modal handled successfully with auto-detection");
            
        } catch (Exception e) {
            log.error("❌ Error in testSubmitModalAutoDetect: " + e.getMessage(), e);
            reportLogger.fail("Error handling modal: " + e.getMessage());
            
            // Try to recover by switching back to parent
            try {
                initiativePage.switchToParentWindow();
            } catch (Exception ignored) {}
            
            throw e;
        }
    }

    /**
     * Example 4: Handle JavaScript Alert (validation error)
     * Use this when JavaScript alerts appear
     */
    @Test(priority = 4, enabled = false)
    @Description("Handle JavaScript Alert after validation error")
    @Severity(SeverityLevel.NORMAL)
    public void testJavaScriptAlert() throws Throwable {
        try {
            // Navigate to initiative
            initiativePage.navigateToInitiative();
            initiativePage.Initiativebeforeclickadd();
            initiativePage.ClickADD();
            initiativePage.SelectNOI("Quick Change Request");
            
            // Try to save WITHOUT required fields (should trigger alert)
            initiativePage.ClickSD();
            log.info("Clicked Save as Draft without filling required fields");
            
            // Wait a moment for alert
            Thread.sleep(1000);
            
            // Check and handle alert
            if (initiativePage.isAlertPresent()) {
                String alertText = initiativePage.getAlertText();
                log.info("⚠️ Alert detected: " + alertText);
                
                // Accept the alert
                initiativePage.handleJavaScriptAlert(true);
                
                log.info("✅ Alert handled successfully");
                reportLogger.pass("JavaScript alert handled: " + alertText);
            } else {
                log.info("ℹ️ No alert appeared (validation might be different)");
                reportLogger.info("No JavaScript alert detected");
            }
            
        } catch (Exception e) {
            log.error("Error handling alert: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Example 5: Close Modal Without Submitting
     * Shows different ways to close a modal
     */
    @Test(priority = 5, enabled = false)
    @Description("Close modal without submitting")
    @Severity(SeverityLevel.MINOR)
    public void testCloseModalWithoutSubmit() throws Throwable {
        try {
            // Navigate and prepare
            navigateToInitiativePage();
            prepareInitiativeForm();
            
            // Open modal
            initiativePage.ClickSubmit();
            log.info("Opened submit modal");
            
            // Wait for modal
            initiativePage.waitForHTMLModal(InitiativePageLocators.modalPopup, 10);
            
            // Close modal using ESC key (most reliable)
            initiativePage.closeModalWithEscape();
            log.info("✅ Modal closed using ESC key");
            
            reportLogger.pass("Modal closed successfully without submitting");
            
            // Alternative ways to close modal (uncomment to test):
            
            // Option 1: Click X button
            // By closeButton = By.xpath("//div[@class='modal-105']//button[@aria-label='Close']");
            // initiativePage.closeHTMLModal(closeButton);
            
            // Option 2: Click backdrop (outside modal)
            // By backdrop = By.xpath("//div[@class='modal-backdrop']");
            // initiativePage.closeModalByClickingBackdrop(backdrop);
            
        } catch (Exception e) {
            log.error("Error closing modal: " + e.getMessage(), e);
            throw e;
        }
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    @Step("Navigate to Initiative Page")
    private void navigateToInitiativePage() throws Throwable {
        initiativePage.navigateToInitiative();
        initiativePage.Initiativebeforeclickadd();
        initiativePage.ClickADD();
        initiativePage.SelectNOI("Quick Change Request");
        log.info("Navigated to Initiative form");
    }

    @Step("Prepare Initiative Form")
    private void prepareInitiativeForm() throws Throwable {
        // Fill form with test data
        initiativePage.setInitiativeTitle("Test Initiative - Modal Demo");
        initiativePage.setInitiativedescription("Testing modal handling functionality");
        initiativePage.selectInitiativeBGWithActions("Technology");
        initiativePage.selectInitiativeOUWithActions("IT Operations");
        
        // Save as draft
        initiativePage.ClickSD();
        log.info("Form filled and saved as draft");
        
        // Wait for Submit button to be ready
        initiativePage.waitForSubmit();
    }

    @Step("Handle Modal as Popup Window")
    private void handleModalAsPopup(String comments) throws Throwable {
        log.info("🪟 Handling as POPUP modal");
        
        boolean switched = initiativePage.switchToModalPopupWindow(
            InitiativePageLocators.modalPopup,
            "Submit Comments Modal"
        );
        
        if (switched) {
            log.info("✅ Switched to popup successfully");
            
            initiativePage.typeInModal(
                InitiativePageLocators.additionalNotes,
                comments,
                "Additional Notes"
            );
            
            initiativePage.clickElementInModal(
                InitiativePageLocators.Submit,
                "Submit Button"
            );
            
            initiativePage.closeCurrentWindowAndSwitchToParent();
            log.info("✅ Popup modal handled successfully");
        } else {
            log.error("❌ Failed to switch to popup");
            throw new Exception("Failed to switch to popup modal");
        }
    }

    @Step("Handle Modal as Overlay")
    private void handleModalAsOverlay(String comments) throws Throwable {
        log.info("📋 Handling as OVERLAY modal");
        
        boolean modalVisible = initiativePage.waitForHTMLModal(
            InitiativePageLocators.modalPopup,
            15
        );
        
        if (modalVisible) {
            log.info("✅ Modal appeared successfully");
            
            initiativePage.typeInModal(
                InitiativePageLocators.additionalNotes,
                comments,
                "Additional Notes"
            );
            
            initiativePage.clickElementInModal(
                InitiativePageLocators.Submit,
                "Submit Button"
            );
            
            Thread.sleep(2000);
            log.info("✅ Overlay modal handled successfully");
        } else {
            log.error("❌ Modal did not appear");
            throw new Exception("Modal did not appear within timeout");
        }
    }

    /**
     * Debug method to print modal information
     */
    @Step("Debug Modal Information")
    private void debugModalInfo() {
        log.info("=== MODAL DEBUG INFO ===");
        log.info("Window count: " + initiativePage.getWindowCount());
        log.info("Alert present: " + initiativePage.isAlertPresent());
        
        java.util.Set<String> handles = webDriver.getWindowHandles();
        log.info("Total windows: " + handles.size());
        
        int index = 0;
        for (String handle : handles) {
            boolean isCurrent = handle.equals(webDriver.getWindowHandle());
            String marker = isCurrent ? "[CURRENT]" : "";
            log.info("  Window " + index + " " + marker + ": " + handle);
            index++;
        }
        
        log.info("=======================");
    }
}

