# SSO Authentication Flow Diagram

## Overview
This document traces the complete SSO authentication flow from BaseTest to the actual Microsoft button click.

---

## Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. TEST EXECUTION STARTS                                        │
│    Your test class extends BaseTest                             │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 2. BaseTest.setUp() [Line 165]                                  │
│    Code: loginHelper.performLogin();                            │
│                                                                  │
│    • useLoginHelper = true (by default)                         │
│    • loginHelper is initialized at line 158                     │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 3. LoginHelper.performLogin() [Line 39-52]                      │
│    Code:                                                         │
│      String authTypeStr = config.getProperty("authType");       │
│      authType = AuthType.valueOf(authTypeStr);                  │
│      performLogin(authType);                                    │
│                                                                  │
│    • Reads: authType=SSO from config.properties                 │
│    • Converts string "SSO" to AuthType enum                     │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 4. LoginHelper.performLogin(AuthType.SSO) [Line 59-70]         │
│    Code:                                                         │
│      switch (authType) {                                        │
│        case SSO:                                                │
│          performSSOLogin();                                     │
│          break;                                                 │
│      }                                                           │
│                                                                  │
│    • Determines which login method to use based on authType     │
│    • Calls performSSOLogin() for SSO                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 5. LoginHelper.performSSOLogin() [Line 100-125]                 │
│    Code:                                                         │
│      String email = config.getProperty("email");                │
│      String password = config.getProperty("ssoPassword");       │
│      loginPage.performSSOLogin(email, password);                │
│                                                                  │
│    • Reads: email=whizible_test@whizible.net                    │
│    • Reads: ssoPassword=Basa742690_24                           │
│    • Delegates to LoginPage for UI interaction                  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 6. LoginPage.performSSOLogin(email, password) [Line 106-163]   │
│                                                                  │
│    ✅ STEP 1: Click Microsoft SSO Button [Line 115-125]        │
│       Code:                                                      │
│         if (isPresent(loc.getMicrosoftLoginBtn(), 5)) {        │
│           click(loc.getMicrosoftLoginBtn(), "Microsoft...");   │
│         }                                                        │
│       • Locator: By.cssSelector("img.ms-2")                    │
│       • Waits up to 5 seconds for button                       │
│       • Clicks Microsoft SSO button on landing page            │
│                                                                  │
│    ✅ STEP 2: Handle Account Selection [Line 127-136]          │
│       Code:                                                      │
│         if (isPresent(loc.useAnotherAccountOption(), 3)) {     │
│           another.click();                                      │
│         }                                                        │
│       • Clicks "Use another account" if shown                  │
│                                                                  │
│    ✅ STEP 3: Enter Email [Line 138-142]                       │
│       Code:                                                      │
│         type(loc.getEmailField(), email, "Enter Email");       │
│         click(loc.getConfirmbutton(), "Next/Submit");          │
│       • Locator: By.xpath("//input[@type='email']")           │
│       • Enters: whizible_test@whizible.net                     │
│       • Clicks Next button                                     │
│                                                                  │
│    ✅ STEP 4: Enter Password [Line 144-148]                    │
│       Code:                                                      │
│         type(loc.getPasswordField(), password, "Enter...");    │
│         click(loc.getConfirmbutton(), "Sign in");              │
│       • Locator: By.xpath("//input[@type='password']")        │
│       • Enters: Basa742690_24                                  │
│       • Clicks Sign in button                                  │
│                                                                  │
│    ✅ STEP 5: Handle "Stay signed in?" [Line 150-155]         │
│       Code:                                                      │
│         if (isPresent(loc.getConfirmbutton(), 5)) {            │
│           click(loc.getConfirmbutton(), "Confirm...");         │
│         }                                                        │
│       • Clicks Yes if prompt appears                           │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 7. LOGIN COMPLETE                                               │
│    Test continues with user logged in                           │
└─────────────────────────────────────────────────────────────────┘
```

---

## Key Files and Their Roles

| File | Role | Key Lines |
|------|------|-----------|
| **config.properties** | Configuration | `authType=SSO`<br>`email=whizible_test@whizible.net`<br>`ssoPassword=Basa742690_24` |
| **BaseTest.java** | Test Setup | Line 165: Calls `loginHelper.performLogin()` |
| **LoginHelper.java** | Authentication Router | Line 40-52: Reads config<br>Line 59-70: Routes to SSO/FORM<br>Line 100-125: Prepares SSO credentials |
| **LoginPage.java** | UI Interaction | Line 106-163: Actual SSO flow<br>Line 115-125: **Microsoft button click** |
| **LoginPageLocators.java** | Element Locators | Line 10: `microsoftLoginBtn`<br>Line 16: `emailField`<br>Line 17: `passwordField` |

---

## Configuration-to-Action Mapping

```
config.properties                    What Happens
─────────────────                    ────────────────────────────────────
authType=SSO          ──────────────> LoginHelper routes to performSSOLogin()
                                      ↓
email=whizible_test@  ──────────────> LoginPage uses this email
whizible.net                          ↓
                                      LoginPage.performSSOLogin():
ssoPassword=          ──────────────> 1. Clicks Microsoft button ✓
Basa742690_24                         2. Enters email ✓
                                      3. Enters password ✓
                                      4. Completes login ✓
```

---

## Why You Don't See SSO Code in BaseTest

**BaseTest is intentionally kept clean!** It only calls:
```java
loginHelper.performLogin();
```

The actual SSO logic is **delegated** to:
- **LoginHelper** → Determines FORM vs SSO
- **LoginPage** → Performs the actual clicks and typing

This follows the **Separation of Concerns** design pattern:
- ✅ BaseTest = Test orchestration
- ✅ LoginHelper = Authentication routing
- ✅ LoginPage = UI interaction logic

---

## How to Verify SSO is Working

### 1. Check your config.properties:
```properties
authType=SSO  ← Must be SSO, not FORM
email=whizible_test@whizible.net
ssoPassword=Basa742690_24
```

### 2. Run your test and look for these log messages:
```
Using LoginHelper for authentication
🔐 Attempting SSO login with email: whizible_test@whizible.net
Clicking Microsoft SSO button
Enter Email
Next/Submit
Enter Password
Sign in
✅ SSO login completed for email: whizible_test@whizible.net
```

### 3. Verify the flow in your Extent Report:
- Should show "Clicking Microsoft SSO button"
- Should show email entry
- Should show password entry

---

## Troubleshooting

| Issue | Check |
|-------|-------|
| Still looking for username field | Verify `authType=SSO` in config.properties |
| Microsoft button not clicked | Check locator: `By.cssSelector("img.ms-2")` |
| Email field not found | Wait for page load after Microsoft button click |
| Password field timeout | Check Microsoft redirect is working |

---

## Summary

✅ **The SSO code IS there!** It's just organized in layers:
1. BaseTest triggers the flow
2. LoginHelper reads config and routes to SSO
3. LoginPage clicks Microsoft button and enters credentials

✅ **Microsoft button click happens at:**
- File: `LoginPage.java`
- Method: `performSSOLogin()`
- Lines: 115-125

✅ **Email is fetched from:**
- File: `config.properties`
- Property: `email=whizible_test@whizible.net`
- Read by: `LoginHelper.performSSOLogin()` at line 101

The framework is working as designed! 🎯




