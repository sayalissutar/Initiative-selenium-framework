package Utils;

import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) return null;
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String baseDir = System.getProperty("user.dir") + "/screenshots";
        String destination = baseDir + "/" + screenshotName + "_" + dateName + ".png";
        try {
            // Ensure directory exists
            Files.createDirectories(Path.of(baseDir));
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // driver may not support screenshots or not initialized
            return null;
        }
        return destination;
    }
}
