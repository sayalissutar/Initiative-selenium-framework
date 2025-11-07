# SSO Login - Enhanced Debug Guide

## 🔧 What I Fixed

The SSO login now includes **comprehensive debugging and multiple fallback strategies** to handle various popup/window/iframe scenarios.

---

## 🎯 New Features

### 1. **Detailed Logging at Every Step**
Your Extent Report will now show:
```
🔐 Performing SSO login with email: whizible_test@whizible.net
Current URL before Microsoft click: https://ini.whizible.com/signin
Microsoft SSO button found - clicking it
Microsoft SSO button clicked successfully
Number of windows open: 2
Current URL after Microsoft click: https://...
Detected multiple windows - attempting to switch to popup
Switched to new window. URL: https://login.microsoftonline.com/...
```

### 2. **Multiple Email Field Search Strategies**

**Strategy 1: Main Page with Multiple Locators**
- `By.xpath("//input[@type='email']")`
- `By.id("i0116")`
- `By.name("loginfmt")`
- `By.xpath("//input[@name='loginfmt']")`
- `By.cssSelector("input[type='email']")`

**Strategy 2: Search in iFrames**
- Searches all iframes on the page
- Tries each locator in each iframe
- Logs which iframe contains the email field

**Strategy 3: Extended Wait (15 seconds)**
- Waits longer for dynamic content
- Uses OR condition for multiple locators

### 3. **Window/Popup Handling**
- Detects if a new window/tab opens
- Automatically switches to the popup
- Tracks URL changes
- Switches back to original window after login

### 4. **Diagnostic Information on Failure**
If email field is not found, you'll see:
```
❌ EMAIL FIELD NOT FOUND - Diagnostic Info:
Current URL: https://...
Page Title: Sign in to your account
Number of windows: 2
Page contains 'email' or 'login' keywords - check locators
```

---

## 📋 What You'll See When Running Tests

### ✅ Successful Flow:
```
🔐 Performing SSO login with email: whizible_test@whizible.net
Current URL before Microsoft click: https://ini.whizible.com/signin
Microsoft SSO button found - clicking it
Microsoft SSO button clicked successfully
Number of windows open: 2
Detected multiple windows - attempting to switch to popup
Switched to new window. URL: https://login.microsoftonline.com/...
Starting comprehensive search for email field...
Strategy 1: Checking main page context
✅ Email field found with locator: By.id: i0116
Entering email: whizible_test@whizible.net
Email entered successfully
Clicked Next button
Waiting for password field...
Password entered successfully
Clicked Sign in button
Found 'Stay signed in?' prompt - clicking Yes
Switched back to original window
✅ SSO login completed successfully for: whizible_test@whizible.net
```

### ❌ If Email Field Not Found:
```
🔐 Performing SSO login with email: whizible_test@whizible.net
Microsoft SSO button found - clicking it
Microsoft SSO button clicked successfully
Number of windows open: 1
Same window - checking for URL change or redirect
URL redirected to Microsoft SSO: https://login.microsoftonline.com/...
Starting comprehensive search for email field...
Strategy 1: Checking main page context
Strategy 2: Searching in iframes...
Found 2 iframes on page
Checking iframe 0
Checking iframe 1
✅ Email field found in iframe 1 with locator: By.xpath: //input[@type='email']
[continues with email entry...]
```

---

## 🐛 Troubleshooting Guide

### Issue 1: "Microsoft SSO button not found on page"

**Possible Causes:**
- Locator changed: `By.cssSelector("img.ms-2")`
- Button loads dynamically

**Solution:**
Check the locator in `LoginPageLocators.java`:
```java
private By microsoftLoginBtn = By.cssSelector("img.ms-2");
```

Update if necessary based on your page structure.

---

### Issue 2: "Number of windows open: 1" (No popup detected)

**Meaning:** Microsoft SSO opens in the same window (URL redirect)

**What Framework Does:**
- Waits for URL to change to Microsoft domain
- Continues searching for email field
- **This is normal!** The framework handles both scenarios.

---

### Issue 3: "Strategy 1, 2, 3 all failed"

**Diagnostic Steps:**

1. **Check the logs for:**
   - Current URL at failure
   - Page Title
   - Number of windows

2. **Manually inspect the page:**
   - Open browser DevTools (F12)
   - After clicking Microsoft button, check:
     - Is email field in an iframe?
     - What is the actual element ID/name?
     - Is there a wait time before field appears?

3. **Update locators if needed:**

In `LoginPageLocators.java`, add your custom locator:
```java
private By emailField = By.id("your-actual-id");
```

---

### Issue 4: "Popup opens but email field still not found"

**Possible Causes:**
1. Iframe detection failed
2. Element loads after extended wait
3. Different locator needed

**Debug Steps:**

1. Check Extent Report for:
```
Found X iframes on page
Checking iframe 0
Checking iframe 1
```

2. If iframes found but field not located, the locators might be wrong

3. **Quick Fix - Add a breakpoint:**

In `LoginPage.java` line 252, add:
```java
if (!emailFieldFound) {
    System.out.println("DEBUG: Page Source snippet:");
    System.out.println(driver.getPageSource().substring(0, 500));
    // ... rest of diagnostic code
}
```

---

### Issue 5: "Email entered but Next button not clickable"

**Check logs for:**
```
Email entered successfully
[Should see] Clicked Next button
```

**If Next button not clicked:**
- Button locator might be wrong
- Button might be disabled
- Need longer wait

**Solution:**
Update button locator in `LoginPageLocators.java`:
```java
private By Confirmbutton = By.xpath("//input[@type='submit']");
```

---

## 🔍 How to Get More Debug Info

### Option 1: Add console output

In `LoginPage.java`, change info() method temporarily:
```java
private void info(String msg) { 
    System.out.println("[DEBUG] " + msg);
    if (test != null) test.info(msg); 
}
```

### Option 2: Add screenshot on failure

In `LoginPage.java`, in the failure block (line 252):
```java
if (!emailFieldFound) {
    try {
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        Files.copy(screenshot.toPath(), 
                   Paths.get("sso-failure-" + System.currentTimeMillis() + ".png"));
    } catch (Exception ignored) {}
    // ... rest of code
}
```

### Option 3: Check Extent Report

After running test, open `extent-report.html`:
- Look for each logged step
- Check which strategy found the email field
- Verify window/iframe handling

---

## 📊 Expected Scenarios

| Scenario | What Framework Does | Log Message |
|----------|---------------------|-------------|
| Popup opens | Switches to popup | "Detected multiple windows - attempting to switch to popup" |
| URL redirects | Stays in same window | "Same window - checking for URL change" |
| Email in iframe | Switches to iframe | "Email field found in iframe X" |
| Email in main page | Uses main page | "Email field found in main context" |
| Slow load | Extended wait | "Email field appeared after extended wait" |

---

## ✅ Success Indicators

Your SSO login is working if you see:
1. ✅ "Microsoft SSO button clicked successfully"
2. ✅ "Email field found with locator..."
3. ✅ "Email entered successfully"
4. ✅ "Password entered successfully"
5. ✅ "SSO login completed successfully"

---

## 🚀 Run Your Test Now

1. Make sure `config.properties` has:
   ```properties
   authType=SSO
   email=whizible_test@whizible.net
   ssoPassword=Basa742690_24
   ```

2. Run your test

3. Check Extent Report (`extent-report.html`) for detailed logs

4. If it fails, check the diagnostic messages to see which step failed

---

## 📞 Still Not Working?

**Share these details from the Extent Report:**

1. "Current URL before Microsoft click"
2. "Number of windows open" (after clicking button)
3. "Current URL after Microsoft click"
4. Which strategy was attempted (1, 2, or 3)
5. "Current URL at failure" (if failed)

This will help pinpoint the exact issue! 🎯




