# 🔍 SSO Diagnostic Test - URGENT

## The Problem
Your SSO login isn't working, and we need to see exactly what's happening after the Microsoft button is clicked.

## The Solution
I've created a special diagnostic test that will:
1. Click the Microsoft button
2. Print detailed information about what happens
3. Tell us the EXACT locator we need for the email field

---

## 📋 How to Run

### Option 1: Using TestNG in IDE
1. Open `src/test/java/tests/SSODiagnosticTest.java`
2. Right-click on the file
3. Select "Run as TestNG Test"

### Option 2: Using Maven
```bash
cd C:\Users\sayali.sutar\Desktop\Sel_From_Git\Initiative-selenium-framework
mvn test -Dtest=SSODiagnosticTest
```

---

## 👀 What to Look For

The test will print information in the **Console** (not the Extent Report).

Look for output like this:

```
========================================
STEP 1: INITIAL PAGE STATE
========================================
URL: https://ini.whizible.com/signin
Title: Sign In
Number of windows: 1

========================================
STEP 2: LOOKING FOR MICROSOFT BUTTON
========================================
Microsoft buttons found with 'img.ms-2': 1

========================================
STEP 3: CLICKING MICROSOFT BUTTON
========================================
Button text: Sign in with Microsoft
✅ Microsoft button clicked

========================================
STEP 4: PAGE STATE AFTER CLICK
========================================
Number of windows now: 2
Current URL: https://...
Current Title: ...

========================================
STEP 5: CHECKING FOR POPUP/NEW WINDOW
========================================
✅ NEW WINDOW DETECTED - Switching to it
Current URL: https://login.microsoftonline.com/...

========================================
STEP 6: SEARCHING FOR EMAIL FIELD
========================================
Checking main page:
  By.id: i0116 found: 1
  ✅ THIS ONE WORKS!

========================================
DIAGNOSTIC SUMMARY
========================================
✅ EMAIL FIELD FOUND!
Working locator: By.id: i0116

Add this to LoginPageLocators.java:
private By emailField = By.id("i0116");
```

---

## 📸 What to Share With Me

After running the test, copy and paste the **entire console output** starting from:

```
========================================
STEP 1: INITIAL PAGE STATE
========================================
```

All the way to:

```
========================================
DIAGNOSTIC TEST COMPLETED
========================================
```

---

## ⚡ Quick Copy-Paste

**If you see this in the output:**

```
✅ EMAIL FIELD FOUND!
Working locator: By.id("i0116")
```

**Then we're almost done! Just share:**
1. Which step found it (main page or iframe?)
2. The working locator
3. The URL it was found on

---

**If you see this:**

```
❌ EMAIL FIELD NOT FOUND
```

**Then share:**
1. The "Number of windows now" value
2. The "Current URL" after clicking Microsoft button
3. All the "Input" fields it found (will be listed at the end)

---

## 🎯 Why This Will Work

This test will tell us:
- ✅ Is Microsoft button being clicked? (Yes/No)
- ✅ Does it open a popup or redirect? (Popup/Redirect)
- ✅ What is the correct email field locator? (Exact locator)
- ✅ Is email field in iframe or main page? (Where to look)

With this information, I can fix your SSO login in 2 minutes! 🚀

---

## 🆘 If Test Won't Run

Make sure:
1. `config.properties` still has your URL: `url=https://ini.whizible.com/signin`
2. Browser is set: `browser=edge`
3. No compilation errors in the project

---

## ⏱️ Run It Now!

1. Run the diagnostic test
2. Copy the console output
3. Share it with me
4. I'll give you the exact fix!

Let's solve this! 💪




