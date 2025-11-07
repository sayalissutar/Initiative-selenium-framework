package Utils;

import Pages.LoginPage;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;
import java.util.Properties;

/**
 * LoginHelper - Centralized authentication manager
 * Supports both Form-based and SSO authentication
 */
public class LoginHelper {
    
    public enum AuthType {
        FORM,   // Username + Password
        SSO     // Email + Password (Microsoft SSO)
    }
    
    private final LoginPage loginPage;
    private final Properties config;
    private final ExtentTest test;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     * @param test ExtentTest instance for reporting
     * @param config Properties object containing configuration
     */
    public LoginHelper(WebDriver driver, ExtentTest test, Properties config) {
        this.loginPage = new LoginPage(driver, test);
        this.config = config;
        this.test = test;
    }
    
    /**
     * Performs login based on configured authentication type
     * Reads authType from config.properties to determine login method
     */
    public void performLogin() {
        String authTypeStr = config.getProperty("authType", "FORM").toUpperCase();
        AuthType authType;
        
        try {
            authType = AuthType.valueOf(authTypeStr);
        } catch (IllegalArgumentException e) {
            if (test != null) {
                test.warning("Invalid authType in config: " + authTypeStr + ". Defaulting to FORM");
            }
            authType = AuthType.FORM;
        }
        
        performLogin(authType);
    }
    
    /**
     * Performs login with specified authentication type
     * @param authType Type of authentication (FORM or SSO)
     */
    public void performLogin(AuthType authType) {
        switch (authType) {
            case FORM:
                performFormLogin();
                break;
            case SSO:
                performSSOLogin();
                break;
            default:
                throw new IllegalArgumentException("Unsupported authentication type: " + authType);
        }
    }
    
    /**
     * Performs Form-based login using username and password
     */
    private void performFormLogin() {
        String username = config.getProperty("username");
        String password = config.getProperty("password");
        
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("❌ Username is not configured for FORM login!");
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("❌ Password is not configured for FORM login!");
        }
        
        if (test != null) {
            test.info("🔐 Attempting FORM login with username: " + username);
        }
        
        loginPage.performFormLogin(username, password);
        
        if (test != null) {
            test.pass("✅ FORM login completed for user: " + username);
        }
    }
    
    /**
     * Performs SSO login using email and password
     */
    private void performSSOLogin() {
        String email = config.getProperty("email");
        String password = config.getProperty("ssoPassword");
        
        // Fallback to regular password if ssoPassword not specified
        if (password == null || password.isEmpty()) {
            password = config.getProperty("password");
        }
        
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("❌ Email is not configured for SSO login!");
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("❌ Password is not configured for SSO login!");
        }
        
        if (test != null) {
            test.info("🔐 Attempting SSO login with email: " + email);
        }
        
        loginPage.performSSOLogin(email, password);
        
        if (test != null) {
            test.pass("✅ SSO login completed for email: " + email);
        }
    }
    
    /**
     * Performs login with custom credentials (overrides config)
     * @param authType Type of authentication
     * @param credential1 Username (for FORM) or Email (for SSO)
     * @param credential2 Password
     */
    public void performLogin(AuthType authType, String credential1, String credential2) {
        if (credential1 == null || credential1.isEmpty()) {
            throw new IllegalArgumentException("Credential1 (username/email) cannot be empty");
        }
        if (credential2 == null || credential2.isEmpty()) {
            throw new IllegalArgumentException("Credential2 (password) cannot be empty");
        }
        
        switch (authType) {
            case FORM:
                if (test != null) {
                    test.info("🔐 Attempting FORM login with custom credentials: " + credential1);
                }
                loginPage.performFormLogin(credential1, credential2);
                break;
            case SSO:
                if (test != null) {
                    test.info("🔐 Attempting SSO login with custom credentials: " + credential1);
                }
                loginPage.performSSOLogin(credential1, credential2);
                break;
            default:
                throw new IllegalArgumentException("Unsupported authentication type: " + authType);
        }
    }
}
