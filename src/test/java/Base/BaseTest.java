package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

// ✅ Allure Imports
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.model.Status;

import Pages.InitiativePage;
import Pages.LoginPage;
import Utils.ExcelReader;
import Utils.LoginHelper;
import Utils.ScreenshotUtil;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

    protected static WebDriver webDriver;
    protected static ExtentReports extent;
    protected static ExtentTest reportLogger;
    private LoginPage loginPage;
    protected LoginHelper loginHelper;
    protected InitiativePage initiativePage;

    protected String username, password, Title, Description, BG, OU;

    // ✅ Config file
    protected static Properties config;
    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    
    // ✅ Flag to use LoginHelper (set to true to use configuration-based auth)
    protected boolean useLoginHelper = true;

    @BeforeClass
    public void setupExtent() throws Exception {
        ExtentSparkReporter spark = new ExtentSparkReporter("extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        // ✅ Load config.properties (always used for username/password)
        try {
            config = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            config.load(fis);
            log.info("Loaded configuration file successfully.");
        } catch (IOException e) {
            log.error("Failed to load config.properties", e);
        }

        // ✅ Credentials only from config
        username = config.getProperty("username");
        password = config.getProperty("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("❌ Username/Password must be set in config.properties!");
        } else {
            log.info("✅ Username/Password loaded from config.properties");
        }
    }

    
    @BeforeMethod
    public void setUp(Method method) {
        // ✅ Create test logger per method
        reportLogger = extent.createTest(method.getName());
        log.info("===== Starting Test: " + method.getName() + " =====");

        // ✅ Pick browser from config
        String browser = config.getProperty("browser", "edge").toLowerCase();
        log.info("Launching browser: " + browser);

        switch (browser) {
            case "chrome":
                // ✅ Use WebDriverManager to automatically manage ChromeDriver
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                // ✅ Use WebDriverManager to automatically manage GeckoDriver
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
            default:
                // ✅ Fallback to manual driver setup due to network issues
                try {
                    // Try WebDriverManager first
                    WebDriverManager.edgedriver().setup();
                    log.info("✅ WebDriverManager successfully set up EdgeDriver");
                } catch (Exception e) {
                    // Fallback to manual driver path
                    log.warn("⚠️ WebDriverManager failed, using manual driver setup: " + e.getMessage());
                    System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
                }
                
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-save-password-bubble");
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.setExperimentalOption("prefs", new HashMap<String, Object>() {{
                    put("credentials_enable_service", false);
                    put("profile.password_manager_enabled", false);
                    put("profile.default_content_setting_values.notifications", 2);
                }});
                webDriver = new EdgeDriver(edgeOptions);
                break;
        }

        webDriver.manage().window().maximize();

        // ✅ Navigate to URL from config
        String appUrl = config.getProperty("url", "URLInconfig");
        webDriver.get(appUrl);
        log.info("Navigated to: " + appUrl);

        // ✅ Initialize Page Objects
        loginPage = new LoginPage(webDriver, reportLogger);
        loginHelper = new LoginHelper(webDriver, reportLogger, config);
        initiativePage = new InitiativePage(webDriver, reportLogger);

        // ✅ Perform login
        if (useLoginHelper) {
            // Use LoginHelper for configuration-based authentication
            log.info("Using LoginHelper for authentication");
            loginHelper.performLogin();
            log.info("Login successful using LoginHelper");
        } else {
            // Use legacy auto-detection login
            if (username != null && password != null) {
                loginPage.login(username, password);
                log.info("Login successful with user: " + username);
            } else {
                log.warn("⚠️ Username/Password not set. Login skipped.");
            }
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            // ✅ Allure Reporting Integration
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = saveScreenshotToTarget(webDriver, result.getName());
                
                // Add screenshot to Allure report
                if (screenshotPath != null) {
                    Allure.addAttachment("Screenshot on Failure", "image/png", 
                        new FileInputStream(screenshotPath), "png");
                }
                
                // Add failure details to Allure
                Allure.step("Test Failed: " + result.getThrowable().getMessage(), Status.FAILED);
                
                reportLogger.fail("❌ Test Failed: " + result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                log.error("❌ Test Failed: " + result.getName(), result.getThrowable());
                
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                Allure.step("Test Passed Successfully", Status.PASSED);
                reportLogger.pass("✅ Test Passed");
                log.info("✅ Test Passed: " + result.getName());
                
            } else if (result.getStatus() == ITestResult.SKIP) {
                Allure.step("Test Skipped: " + result.getThrowable().getMessage(), Status.SKIPPED);
                reportLogger.skip("⚠️ Test Skipped: " + result.getThrowable());
                log.warn("⚠️ Test Skipped: " + result.getName(), result.getThrowable());
            }
            
            // ✅ Add final screenshot for all tests
            if (webDriver != null) {
                String finalScreenshot = saveScreenshotToTarget(webDriver, result.getName() + "_final");
                if (finalScreenshot != null) {
                    Allure.addAttachment("Final Screenshot", "image/png", 
                        new FileInputStream(finalScreenshot), "png");
                }
            }
            
        } catch (Exception e) {
            log.error("⚠️ Error while capturing screenshot/report", e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
                log.info("🟥 Browser closed successfully.");
            }
        }
    }

    // =====================
    // Screenshot utilities
    // =====================
    private static String getTargetScreenshotsDir() {
        String base = System.getProperty("user.dir");
        Path dir = Paths.get(base, "target", "screenshots");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir.toString();
    }

    private static String saveScreenshotToTarget(WebDriver driver, String name) {
        try {
            if (driver == null) return null;
            String folder = getTargetScreenshotsDir();
            String fileName = name + "_" + System.currentTimeMillis() + ".png";
            Path out = Paths.get(folder, fileName);
            File src = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            Files.copy(src.toPath(), out);
            return out.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @AfterClass
    public void flushExtent() {
        if (extent != null) {
            extent.flush();
            log.info("Extent report flushed successfully.");
        }
    }

    public static String getScreenshot(WebDriver driver, String screenshotName) {
        // ✅ implement if you want screenshots in Extent report later
        return null;
    }
}
