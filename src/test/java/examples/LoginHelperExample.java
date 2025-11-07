package examples;

import Base.BaseTest;
import Utils.LoginHelper;
import Utils.LoginHelper.AuthType;
import org.testng.annotations.Test;

/**
 * Example demonstrating how to use LoginHelper for different authentication types
 * 
 * Configuration in config.properties:
 * - authType=FORM  (for Form-based login with username/password)
 * - authType=SSO   (for SSO login with email/password)
 */
public class LoginHelperExample extends BaseTest {

    /**
     * Example 1: Use LoginHelper with configuration from config.properties
     * The LoginHelper will automatically read authType from config and perform appropriate login
     */
    @Test(priority = 1)
    public void testLoginWithConfiguredAuthType() {
        reportLogger.info("=== Example 1: Login using configured authType ===");
        
        // Create LoginHelper instance
        LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
        
        // Perform login based on authType in config.properties
        // If authType=FORM, it will use username+password
        // If authType=SSO, it will use email+password
        loginHelper.performLogin();
        
        reportLogger.pass("✅ Login completed using configured authentication type");
    }
    
    /**
     * Example 2: Force FORM login explicitly (overrides config)
     */
    @Test(priority = 2)
    public void testForceFormLogin() {
        reportLogger.info("=== Example 2: Force FORM login ===");
        
        LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
        
        // Force FORM login regardless of config.properties authType
        loginHelper.performLogin(AuthType.FORM);
        
        reportLogger.pass("✅ FORM login completed");
    }
    
    /**
     * Example 3: Force SSO login explicitly (overrides config)
     */
    @Test(priority = 3)
    public void testForceSSOLogin() {
        reportLogger.info("=== Example 3: Force SSO login ===");
        
        LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
        
        // Force SSO login regardless of config.properties authType
        loginHelper.performLogin(AuthType.SSO);
        
        reportLogger.pass("✅ SSO login completed");
    }
    
    /**
     * Example 4: FORM login with custom credentials (not from config)
     */
    @Test(priority = 4)
    public void testFormLoginWithCustomCredentials() {
        reportLogger.info("=== Example 4: FORM login with custom credentials ===");
        
        LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
        
        // Provide custom credentials for FORM login
        String customUsername = "TestUser123";
        String customPassword = "TestPass@123";
        
        loginHelper.performLogin(AuthType.FORM, customUsername, customPassword);
        
        reportLogger.pass("✅ FORM login with custom credentials completed");
    }
    
    /**
     * Example 5: SSO login with custom credentials (not from config)
     */
    @Test(priority = 5)
    public void testSSOLoginWithCustomCredentials() {
        reportLogger.info("=== Example 5: SSO login with custom credentials ===");
        
        LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
        
        // Provide custom credentials for SSO login
        String customEmail = "testuser@company.com";
        String customPassword = "TestPass@123";
        
        loginHelper.performLogin(AuthType.SSO, customEmail, customPassword);
        
        reportLogger.pass("✅ SSO login with custom credentials completed");
    }
}

