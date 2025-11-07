# Authentication Framework Guide

## Overview

This framework now supports **configurable authentication types** to handle different login scenarios:
- **FORM Login**: Traditional username + password form authentication
- **SSO Login**: Microsoft SSO authentication using email + password

## Configuration

### config.properties Setup

The authentication type is configured in `src/main/resources/config.properties`:

```properties
# Authentication Configuration
# authType: FORM | SSO
authType=FORM

# Credentials for FORM login (username + password)
username=User1
password=123456

# Credentials for SSO login (email + password)
email=user@example.com
ssoPassword=123456
```

### Configuration Parameters

| Parameter | Values | Description |
|-----------|--------|-------------|
| `authType` | `FORM` or `SSO` | Determines which authentication method to use |
| `username` | String | Username for FORM login |
| `password` | String | Password for both FORM and SSO (if ssoPassword not specified) |
| `email` | String | Email address for SSO login |
| `ssoPassword` | String | Password for SSO login (optional, falls back to `password`) |

## Usage Methods

### Method 1: Using LoginHelper (Recommended)

The `LoginHelper` class provides a clean, configuration-driven approach:

```java
// Create LoginHelper instance
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);

// Perform login based on authType in config.properties
loginHelper.performLogin();
```

### Method 2: BaseTest Integration

By default, `BaseTest` uses `LoginHelper`. Set the flag in your test class:

```java
public class MyTest extends BaseTest {
    // Use LoginHelper (default: true)
    // Set to false to use legacy auto-detection
    
    @Test
    public void myTestMethod() {
        // Login is automatically performed in @BeforeMethod
        // based on configuration
    }
}
```

### Method 3: Force Specific Authentication Type

Override the configured authentication type:

```java
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);

// Force FORM login (ignores config.authType)
loginHelper.performLogin(AuthType.FORM);

// Force SSO login (ignores config.authType)
loginHelper.performLogin(AuthType.SSO);
```

### Method 4: Custom Credentials

Provide credentials at runtime instead of using config:

```java
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);

// FORM login with custom credentials
loginHelper.performLogin(AuthType.FORM, "customUser", "customPass");

// SSO login with custom credentials
loginHelper.performLogin(AuthType.SSO, "user@company.com", "customPass");
```

### Method 5: Direct Page Object Usage

Use `LoginPage` methods directly for more control:

```java
LoginPage loginPage = new LoginPage(webDriver, reportLogger);

// Perform FORM login
loginPage.performFormLogin("username", "password");

// Perform SSO login
loginPage.performSSOLogin("email@example.com", "password");

// Legacy auto-detection (deprecated)
loginPage.login("username", "password");
```

## Examples

### Example 1: Switch Between FORM and SSO

**For FORM Login:**
```properties
# config.properties
authType=FORM
username=User1
password=123456
```

```java
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
loginHelper.performLogin(); // Uses FORM login
```

**For SSO Login:**
```properties
# config.properties
authType=SSO
email=user@company.com
ssoPassword=SecurePass123
```

```java
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
loginHelper.performLogin(); // Uses SSO login
```

### Example 2: Multiple Users with Different Auth Types

```java
@Test
public void testMultipleUsers() {
    LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
    
    // Test with FORM user
    loginHelper.performLogin(AuthType.FORM, "formUser", "pass123");
    // ... perform tests ...
    
    // Logout and test with SSO user
    // logout logic here
    loginHelper.performLogin(AuthType.SSO, "sso@company.com", "pass456");
    // ... perform tests ...
}
```

### Example 3: Data-Driven Testing

```java
@Test(dataProvider = "authProvider")
public void testWithDifferentAuthTypes(String authType, String cred1, String cred2) {
    LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
    
    AuthType type = AuthType.valueOf(authType);
    loginHelper.performLogin(type, cred1, cred2);
    
    // ... perform tests ...
}

@DataProvider(name = "authProvider")
public Object[][] getAuthData() {
    return new Object[][] {
        {"FORM", "user1", "pass1"},
        {"FORM", "user2", "pass2"},
        {"SSO", "email1@company.com", "ssoPass1"},
        {"SSO", "email2@company.com", "ssoPass2"}
    };
}
```

## Architecture

### Component Overview

```
┌─────────────────┐
│   BaseTest      │ ← Your test classes extend this
└────────┬────────┘
         │ uses
         ▼
┌─────────────────┐
│  LoginHelper    │ ← Manages authentication logic
└────────┬────────┘
         │ delegates to
         ▼
┌─────────────────┐
│   LoginPage     │ ← Performs actual UI interactions
└─────────────────┘
```

### Class Responsibilities

#### LoginHelper
- Reads authentication configuration
- Validates credentials
- Determines which login method to invoke
- Provides flexible API for different scenarios

#### LoginPage
- Contains UI interaction logic
- Implements `performFormLogin()` for form-based auth
- Implements `performSSOLogin()` for SSO auth
- Handles element waits, retries, and error handling

#### BaseTest
- Initializes framework components
- Loads configuration
- Orchestrates login during test setup
- Provides `useLoginHelper` flag for flexibility

## Best Practices

### 1. Use Configuration for Environment-Specific Auth
```properties
# dev-config.properties
authType=FORM
username=devUser

# prod-config.properties
authType=SSO
email=prod.user@company.com
```

### 2. Secure Credentials
- Never commit real passwords to source control
- Use environment variables or secure vaults
- Consider using encrypted property files

```java
// Example: Load password from environment
String password = System.getenv("TEST_PASSWORD");
if (password == null) {
    password = config.getProperty("password");
}
```

### 3. Handle Authentication Failures Gracefully
```java
try {
    loginHelper.performLogin();
} catch (TimeoutException e) {
    reportLogger.fail("Authentication timeout - check credentials and network");
    throw e;
}
```

### 4. Skip Login for Specific Tests
```java
public class MyTest extends BaseTest {
    
    @Override
    @BeforeMethod
    public void setUp(Method method) {
        // Don't call super.setUp() to skip automatic login
        // Initialize driver manually
    }
    
    @Test
    public void testLoginPage() {
        // Test login page UI without authenticating
    }
}
```

## Troubleshooting

### Issue: "Username/Password not configured"
**Solution**: Ensure `username` and `password` are set in config.properties for FORM login

### Issue: "Email not configured for SSO"
**Solution**: Set `email` and `ssoPassword` (or `password`) in config.properties

### Issue: Authentication fails despite correct credentials
**Solution**: 
1. Check if correct `authType` is set (FORM vs SSO)
2. Verify page locators in `LoginPageLocators.java`
3. Check for captcha or additional security measures
4. Review browser console for JavaScript errors

### Issue: Want to disable automatic login
**Solution**: Set `useLoginHelper = false` in your test class, or override `setUp()` method

```java
public class MyTest extends BaseTest {
    public MyTest() {
        this.useLoginHelper = false;
    }
}
```

## Migration from Legacy Code

If you're migrating from the old `loginPage.login()` method:

**Before:**
```java
loginPage.login(username, password);
```

**After (Option 1 - Configuration-based):**
```java
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
loginHelper.performLogin();
```

**After (Option 2 - Direct specification):**
```java
LoginPage loginPage = new LoginPage(webDriver, reportLogger);
loginPage.performFormLogin(username, password);  // For FORM
// OR
loginPage.performSSOLogin(email, password);       // For SSO
```

## API Reference

### LoginHelper

| Method | Parameters | Description |
|--------|------------|-------------|
| `performLogin()` | None | Login using configured authType |
| `performLogin(AuthType)` | authType | Login with specific auth type |
| `performLogin(AuthType, String, String)` | authType, credential1, credential2 | Login with custom credentials |

### LoginPage

| Method | Parameters | Description |
|--------|------------|-------------|
| `performFormLogin(String, String)` | username, password | Execute FORM login |
| `performSSOLogin(String, String)` | email, password | Execute SSO login |
| `login(String, String)` | username, password | Auto-detect login type (deprecated) |

### AuthType Enum

| Value | Description |
|-------|-------------|
| `FORM` | Username + Password form authentication |
| `SSO` | Microsoft SSO with email authentication |

## Advanced Scenarios

### Multi-Tenant Applications
```java
// config-tenant1.properties
authType=FORM
username=tenant1_user

// config-tenant2.properties
authType=SSO
email=user@tenant2.com
```

### Conditional Authentication
```java
String environment = System.getProperty("env", "dev");

if (environment.equals("prod")) {
    loginHelper.performLogin(AuthType.SSO);
} else {
    loginHelper.performLogin(AuthType.FORM);
}
```

### Parallel Execution with Different Auth
```java
@Test(threadPoolSize = 2, invocationCount = 2)
public void parallelAuthTest() {
    String threadName = Thread.currentThread().getName();
    
    if (threadName.contains("1")) {
        loginHelper.performLogin(AuthType.FORM, "user1", "pass1");
    } else {
        loginHelper.performLogin(AuthType.SSO, "user2@test.com", "pass2");
    }
}
```

## Summary

The new authentication framework provides:
- ✅ **Flexibility**: Easy switching between FORM and SSO
- ✅ **Configuration**: Control auth type via config file
- ✅ **Extensibility**: Add new auth types easily
- ✅ **Clean Code**: Separation of concerns
- ✅ **Backward Compatible**: Legacy methods still work
- ✅ **Runtime Override**: Custom credentials when needed

For more examples, see `examples/LoginHelperExample.java`
