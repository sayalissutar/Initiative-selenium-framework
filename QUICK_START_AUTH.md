# Quick Start: Authentication Configuration

## 🚀 Get Started in 3 Steps

### Step 1: Configure Authentication Type

Edit `src/main/resources/config.properties`:

**For Form Login (Username + Password):**
```properties
authType=FORM
username=YourUsername
password=YourPassword
```

**For SSO Login (Email + Password):**
```properties
authType=SSO
email=your.email@company.com
ssoPassword=YourPassword
```

### Step 2: Run Your Tests

Your tests will automatically use the configured authentication method:

```java
public class MyTest extends BaseTest {
    @Test
    public void myTest() {
        // Login is automatic - just write your test logic
        initiativePage.createNewInitiative("Test", "Description");
    }
}
```

### Step 3: Switch Authentication Types

Simply change `authType` in config.properties - **no code changes needed!**

---

## 📋 Configuration Reference

| Property | Required For | Example Value |
|----------|--------------|---------------|
| `authType` | Both | `FORM` or `SSO` |
| `username` | FORM | `User1` |
| `password` | FORM | `123456` |
| `email` | SSO | `user@example.com` |
| `ssoPassword` | SSO | `123456` |

---

## 💡 Common Scenarios

### Scenario 1: Use LoginHelper Directly

```java
LoginHelper loginHelper = new LoginHelper(webDriver, reportLogger, config);
loginHelper.performLogin(); // Uses config.authType
```

### Scenario 2: Override Config and Force Auth Type

```java
// Force FORM login
loginHelper.performLogin(AuthType.FORM);

// Force SSO login
loginHelper.performLogin(AuthType.SSO);
```

### Scenario 3: Custom Credentials (Not from Config)

```java
// FORM with custom credentials
loginHelper.performLogin(AuthType.FORM, "customUser", "customPass");

// SSO with custom credentials
loginHelper.performLogin(AuthType.SSO, "email@test.com", "customPass");
```

### Scenario 4: Direct Page Object Access

```java
LoginPage loginPage = new LoginPage(webDriver, reportLogger);

// FORM login
loginPage.performFormLogin("username", "password");

// SSO login
loginPage.performSSOLogin("email@example.com", "password");
```

---

## 🔧 Advanced Options

### Disable Automatic Login in BaseTest

```java
public class MyTest extends BaseTest {
    public MyTest() {
        this.useLoginHelper = false; // Disable auto-login
    }
    
    @Test
    public void testLogin() {
        // Manually perform login when needed
        loginHelper.performLogin();
    }
}
```

### Multiple Authentication in Same Test

```java
@Test
public void testMultipleUsers() {
    // Login as FORM user
    loginHelper.performLogin(AuthType.FORM, "user1", "pass1");
    // ... test logic ...
    
    // Logout and login as SSO user
    // logout code here
    loginHelper.performLogin(AuthType.SSO, "user2@test.com", "pass2");
    // ... test logic ...
}
```

---

## 📚 Files Modified/Added

| File | Purpose |
|------|---------|
| `Utils/LoginHelper.java` | Main authentication manager |
| `Pages/LoginPage.java` | Added `performFormLogin()` and `performSSOLogin()` |
| `Base/BaseTest.java` | Integrated LoginHelper |
| `config.properties` | Added authentication configuration |
| `examples/LoginHelperExample.java` | Usage examples |

---

## ⚙️ How It Works

```
1. Test starts → BaseTest.setUp()
2. Loads config.properties → Reads authType
3. LoginHelper.performLogin() → Determines FORM or SSO
4. Calls appropriate method → performFormLogin() or performSSOLogin()
5. Test continues → You're logged in!
```

---

## ✅ Benefits

✅ **No code changes** - just update config.properties  
✅ **Flexible** - override config anytime  
✅ **Clean** - separation of concerns  
✅ **Backward compatible** - old code still works  
✅ **Extensible** - easy to add new auth types  

---

## 📖 Full Documentation

For detailed information, see `AUTHENTICATION_GUIDE.md`

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| "Username not configured" | Set `username` in config.properties |
| "Email not configured" | Set `email` in config.properties |
| Wrong auth type used | Check `authType` value (must be FORM or SSO) |
| Want to skip auto-login | Set `useLoginHelper = false` in test class |

---

## 🎯 Example: Switch from FORM to SSO

**Before (config.properties):**
```properties
authType=FORM
username=User1
password=123456
```

**After (config.properties):**
```properties
authType=SSO
email=user1@company.com
ssoPassword=123456
```

**Code changes needed:** **NONE!** ✨

---

That's it! You're ready to use configurable authentication in your framework.
