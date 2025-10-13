package tests;

import org.testng.annotations.Test;
import Base.BaseTest;

public class LoginTest extends BaseTest {

    @Test
    public void openLoginPage() throws InterruptedException {
        System.out.println("🔹 Login Page Test Started");

        // Wait to keep browser open for demo
        Thread.sleep(5000);

        System.out.println("🔹 Login Page Test Ended here");
    }
}
