package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import Base.BaseTest;
import java.util.Scanner;

/**
 * SIMPLE SSO DEBUG TEST
 * This test will PAUSE after clicking Microsoft button
 * So you can manually see what's on the page
 */
public class SimpleSSODebug extends BaseTest {

    @Test
    public void pauseAfterMicrosoftClick() throws Exception {
        reportLogger.info("===== SIMPLE SSO DEBUG TEST =====");
        
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     SSO DEBUG TEST - MANUAL INSPECTION                 ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Step 1: Show initial page
        System.out.println("STEP 1: On login page");
        System.out.println("URL: " + webDriver.getCurrentUrl());
        System.out.println();
        
        // Step 2: Find and click Microsoft button
        System.out.println("STEP 2: Looking for Microsoft button...");
        
        By microsoftBtn = By.cssSelector("img.ms-2");
        WebElement button = null;
        
        try {
            button = webDriver.findElement(microsoftBtn);
            System.out.println("✅ Found Microsoft button with: img.ms-2");
        } catch (Exception e) {
            System.out.println("❌ Microsoft button NOT found with img.ms-2");
            System.out.println("   Trying alternative...");
            
            try {
                microsoftBtn = By.xpath("//button[contains(@class,'microsoft')]");
                button = webDriver.findElement(microsoftBtn);
                System.out.println("✅ Found with: //button[contains(@class,'microsoft')]");
            } catch (Exception e2) {
                System.out.println("❌ Still not found. Listing all buttons:");
                java.util.List<WebElement> buttons = webDriver.findElements(By.tagName("button"));
                for (int i = 0; i < Math.min(buttons.size(), 5); i++) {
                    System.out.println("   Button " + i + ": " + buttons.get(i).getText());
                }
                throw new Exception("Cannot find Microsoft button!");
            }
        }
        
        System.out.println();
        System.out.println("STEP 3: Clicking Microsoft button NOW...");
        button.click();
        System.out.println("✅ Clicked!");
        
        Thread.sleep(5000); // Wait 5 seconds for page to load
        
        // Step 3: Show what happened
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("AFTER CLICKING MICROSOFT BUTTON:");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("Number of windows: " + webDriver.getWindowHandles().size());
        System.out.println("Current URL: " + webDriver.getCurrentUrl());
        System.out.println("Page Title: " + webDriver.getTitle());
        System.out.println();
        
        // Check if popup opened
        String originalWindow = webDriver.getWindowHandles().iterator().next();
        if (webDriver.getWindowHandles().size() > 1) {
            System.out.println("🔔 POPUP DETECTED! Switching to it...");
            for (String handle : webDriver.getWindowHandles()) {
                if (!handle.equals(originalWindow)) {
                    webDriver.switchTo().window(handle);
                    break;
                }
            }
            Thread.sleep(2000);
            System.out.println("Popup URL: " + webDriver.getCurrentUrl());
            System.out.println("Popup Title: " + webDriver.getTitle());
        }
        
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("NOW - MANUALLY LOOK AT THE BROWSER WINDOW");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("1. Can you see the email field?");
        System.out.println("2. Right-click on the email field");
        System.out.println("3. Select 'Inspect' or 'Inspect Element'");
        System.out.println("4. Look at the HTML code - what is:");
        System.out.println("   - id=\"???\"");
        System.out.println("   - name=\"???\"");
        System.out.println("   - type=\"???\"");
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("BROWSER WILL STAY OPEN FOR 2 MINUTES");
        System.out.println("Press ENTER when you're done inspecting...");
        System.out.println();
        
        // Keep browser open
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        
        System.out.println();
        System.out.println("✅ Great! Now tell me what you found:");
        System.out.println("   Email field id: ");
        System.out.println("   Email field name: ");
        System.out.println("   Email field type: ");
        System.out.println();
        
        reportLogger.pass("Debug test completed - check console");
    }
    
    @Override
    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        // Override to disable automatic login
        reportLogger = extent.createTest(method.getName());
        
        String browser = config.getProperty("browser", "edge").toLowerCase();

        switch (browser) {
            case "chrome":
                io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
                org.openqa.selenium.chrome.ChromeOptions chromeOptions = new org.openqa.selenium.chrome.ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                webDriver = new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);
                break;

            case "edge":
            default:
                try {
                    io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
                } catch (Exception e) {
                    System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
                }
                
                org.openqa.selenium.edge.EdgeOptions edgeOptions = new org.openqa.selenium.edge.EdgeOptions();
                edgeOptions.addArguments("--disable-save-password-bubble");
                edgeOptions.addArguments("--disable-notifications");
                webDriver = new org.openqa.selenium.edge.EdgeDriver(edgeOptions);
                break;
        }

        webDriver.manage().window().maximize();
        String appUrl = config.getProperty("url");
        webDriver.get(appUrl);
    }
}

