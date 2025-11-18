# Locator Management - Quick Reference Guide

## 🎯 One-Page Cheat Sheet

---

## Decision Tree: Where Should This Locator Go?

```
Is the locator used more than once?
├─ YES → Put in InitiativePageLocators.java ✅
└─ NO → Still put in InitiativePageLocators.java ✅
        (for consistency and future reusability)

Does the locator need a parameter (dynamic)?
├─ YES → Create helper method in InitiativePageLocators ✅
│        Example: getDynamicOptionByText(String text)
└─ NO → Create static By field in InitiativePageLocators ✅
         Example: public static By submitButton = By.id("submit")

Is the locator unstable (needs fallbacks)?
├─ YES → Create By[] array in InitiativePageLocators ✅
│        Example: public static By[] submitButtonLocators = {...}
└─ NO → Single static locator is fine ✅
```

---

## Common Patterns & Examples

### 1. Static Locator (Most Common)
```java
// In InitiativePageLocators.java:
public static By submitButton = By.xpath("//button[@id='submit']");

// In InitiativePage.java:
click(InitiativePageLocators.submitButton, "Submit Button");
```

### 2. Dynamic Locator (With Parameters)
```java
// In InitiativePageLocators.java:
public static By getDynamicRowByIndex(int index) {
    return By.xpath("//table//tr[" + index + "]");
}

// In InitiativePage.java:
WebElement row = driver.findElement(
    InitiativePageLocators.getDynamicRowByIndex(5)
);
```

### 3. Fallback Locator Array (For Stability)
```java
// In InitiativePageLocators.java:
public static By[] submitButtonLocators = {
    By.id("submit-btn"),                    // Try ID first
    By.xpath("//button[text()='Submit']"), // Then XPath
    By.cssSelector("button.submit")        // Finally CSS
};

// In InitiativePage.java:
for (By locator : InitiativePageLocators.submitButtonLocators) {
    try {
        WebElement element = driver.findElement(locator);
        element.click();
        break;
    } catch (Exception e) {
        continue; // Try next locator
    }
}
```

---

## Before vs After Examples

### ❌ BEFORE (Bad Practice)
```java
public void clickSubmit() {
    // Hardcoded locator - BAD!
    driver.findElement(By.xpath("//button[@id='submit']")).click();
}

public void selectOption(String option) {
    // Inline dynamic locator - BAD!
    By locator = By.xpath("//li[text()='" + option + "']");
    driver.findElement(locator).click();
}
```

### ✅ AFTER (Good Practice)
```java
public void clickSubmit() {
    // Using centralized locator - GOOD!
    driver.findElement(InitiativePageLocators.submitButton).click();
}

public void selectOption(String option) {
    // Using helper method - GOOD!
    driver.findElement(
        InitiativePageLocators.getDynamicOptionByText(option)
    ).click();
}
```

---

## Quick Syntax Reference

### Static Locators
```java
// XPath
public static By element = By.xpath("//div[@id='example']");

// ID
public static By element = By.id("element-id");

// CSS Selector
public static By element = By.cssSelector(".class-name");

// Class Name
public static By element = By.className("class-name");

// Link Text
public static By element = By.linkText("Click Here");

// Partial Link Text
public static By element = By.partialLinkText("Click");

// Name
public static By element = By.name("element-name");

// Tag Name
public static By element = By.tagName("button");
```

### Dynamic Helper Methods
```java
// By Text Content
public static By getByText(String text) {
    return By.xpath("//*[text()='" + text + "']");
}

// By Normalized Text (handles whitespace)
public static By getByNormalizedText(String text) {
    return By.xpath("//*[normalize-space(text())='" + text + "']");
}

// By Attribute Value
public static By getByDataId(String dataId) {
    return By.xpath("//*[@data-id='" + dataId + "']");
}

// By Index
public static By getRowByIndex(int index) {
    return By.xpath("//table//tr[" + index + "]");
}

// By Contains Text
public static By getByContainsText(String text) {
    return By.xpath("//*[contains(text(),'" + text + "')]");
}
```

---

## Common Mistakes to Avoid

| ❌ Mistake | ✅ Correction |
|-----------|--------------|
| `By.xpath("//div")` in method | Add to InitiativePageLocators |
| Creating same locator twice | Use existing one from Locators class |
| Inline dynamic locators | Create helper method in Locators class |
| No documentation | Add JavaDoc comments to locators |
| Mixing approaches | Be consistent - always use Locators class |

---

## Review Checklist

Before committing code, verify:
- [ ] No `By.xpath()`, `By.id()`, etc. in Page methods
- [ ] All locators in `InitiativePageLocators.java`
- [ ] Dynamic locators use helper methods
- [ ] Locators have descriptive names
- [ ] Locators are documented with comments
- [ ] Similar locators are grouped together

---

## Code Smell Indicators

🚨 **Red Flags** that indicate improper locator usage:
```java
// RED FLAG #1: Inline By.xpath in method
driver.findElement(By.xpath("//button")).click();

// RED FLAG #2: Creating locator in method
By locator = By.id("button");

// RED FLAG #3: Concatenating strings for XPath in method
By dynamic = By.xpath("//div[text()='" + value + "']");

// RED FLAG #4: Duplicate locator definitions
// Same XPath appears in multiple methods
```

---

## When in Doubt...

**Ask yourself:**
1. Is this locator already in `InitiativePageLocators.java`?
   - YES → Use it
   - NO → Add it there first

2. Will this locator be used again?
   - Maybe/Yes → Definitely add to Locators class
   - No → Still add to Locators class (consistency)

3. Does the locator need parameters?
   - YES → Create helper method
   - NO → Create static field

**Golden Rule:** 
> If you're writing `By.` in a Page class method, you're probably doing it wrong!

---

## Need Help?

| Scenario | Solution |
|----------|----------|
| Locator changes frequently | Use locator arrays with fallbacks |
| Locator needs parameters | Create helper method |
| Multiple similar locators | Create parameterized helper method |
| Can't find element | Check InitiativePageLocators for alternatives |
| Unsure about best practice | Default to centralization |

---

## Real-World Examples from Our Codebase

### Example 1: Navigation
```java
// Locator
public static By initiativeNode = By.xpath("//*[normalize-space(text())='Initiative']");

// Usage
clickWithFallback(InitiativePageLocators.initiativeNode, "Initiative Node");
```

### Example 2: Form Inputs
```java
// Locators
public static By IniTitle = By.xpath("//input[@id='TextField5']");
public static By iniDescription = By.xpath("//textarea[@id='TextField15']");

// Usage
type(InitiativePageLocators.IniTitle, title, "Initiative Title");
type(InitiativePageLocators.iniDescription, description, "Description");
```

### Example 3: Modal Interaction
```java
// Locator Array (for unstable elements)
public static By[] modalSubmitButtonLocators = {
    By.xpath("//div[@class='modal-105']//button[contains(@class,'ms-Button--primary')]"),
    By.xpath("//div[@class='modal-105']//button[contains(text(),'Submit')]"),
    By.xpath("//div[@class='modal-105']//button")
};

// Usage
for (By locator : InitiativePageLocators.modalSubmitButtonLocators) {
    try {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        btn.click();
        break;
    } catch (Exception e) {
        continue;
    }
}
```

---

**Remember:** Consistency is key! Follow these patterns for a maintainable framework.

---

*Last Updated: November 2025*

