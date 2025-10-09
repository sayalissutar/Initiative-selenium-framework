package Utils;

import io.qameta.allure.Allure;
import org.testng.*;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TestNG listener to capture screenshots for both test failures and configuration failures.
 */
public class TestListener implements ITestListener, IConfigurationListener {

    private void attachScreenshotIfPossible(ITestResult result) {
        try {
            Object instance = result.getInstance();
            if (instance == null) return;
            // Expect BaseTest-style field named webDriver
            WebDriver driver = (WebDriver) instance.getClass().getField("webDriver").get(null);
            if (driver == null) return;

            String name = result.getName() + "_failure";
            String path = ScreenshotUtil.captureScreenshot(driver, name);
            if (path != null && Files.exists(Paths.get(path))) {
                Allure.addAttachment("Failure Screenshot", new FileInputStream(path));
            }
        } catch (NoSuchFieldException e) {
            // fallback: try to get protected/static field via reflection on superclass
            try {
                Class<?> clazz = result.getInstance().getClass().getSuperclass();
                if (clazz != null) {
                    WebDriver driver = (WebDriver) clazz.getDeclaredField("webDriver").get(null);
                    if (driver != null) {
                        String name = result.getName() + "_failure";
                        String path = ScreenshotUtil.captureScreenshot(driver, name);
                        if (path != null && Files.exists(Paths.get(path))) {
                            Allure.addAttachment("Failure Screenshot", new FileInputStream(path));
                        }
                    }
                }
            } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshotIfPossible(result);
    }

    // Capture screenshots for configuration failures (e.g., @BeforeMethod failures)
    @Override
    public void onConfigurationFailure(ITestResult itr) {
        attachScreenshotIfPossible(itr);
    }

    // Unused listener methods
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onTestFailedWithTimeout(ITestResult result) { onTestFailure(result); }
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
    @Override public void onConfigurationSuccess(ITestResult itr) {}
    @Override public void onConfigurationSkip(ITestResult itr) {}
}


