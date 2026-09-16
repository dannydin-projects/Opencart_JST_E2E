# Method Overloading for Custom WebDriver Methods - Step-by-Step Guide

## **What is Method Overloading?**

Method overloading is an OOP concept where **multiple methods have the same name but DIFFERENT parameters**. Java determines which method to call based on:
- Number of parameters
- Type of parameters
- Order of parameters

### **Example:**
```java
// All methods have same name "click" but different signatures
public void click(WebElement element) { }                    // 1 parameter
public void click(By locator) { }                           // Different type
public void click(WebElement element, int timeoutInSecs) { } // 2 parameters
public void click(String xpath, int waitTime) { }           // Different order
```

---

## **IMPLEMENTATION STEPS**

### **Step 1: Create a Custom WebDriver Utility Class**

Create a new file: `src/test/java/utilities/WebDriverActions.java`

```java
package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WebDriverActions {
    
    private static WebDriver driver;
    private static Logger logger = (Logger) LogManager.getLogger(WebDriverActions.class);
    private static final int DEFAULT_WAIT_TIME = 10;
    
    // Constructor
    public WebDriverActions(WebDriver driver) {
        this.driver = driver;
    }
    
    // ============================================
    // METHOD OVERLOADING FOR: CLICK
    // ============================================
    
    /**
     * Overload 1: Click using WebElement directly
     * Usage: click(emailElement)
     */
    public void click(WebElement element) {
        try {
            element.click();
            logger.info("✓ Clicked on WebElement: " + element.getText());
        } catch (Exception e) {
            logger.error("✗ Failed to click WebElement: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 2: Click using By locator with default wait
     * Usage: click(By.xpath("//button[@id='submit']"))
     */
    public void click(By locator) {
        click(locator, DEFAULT_WAIT_TIME);
    }
    
    /**
     * Overload 3: Click using By locator with custom timeout
     * Usage: click(By.xpath("//button[@id='submit']"), 5)
     */
    public void click(By locator, int timeoutInSecs) {
        try {
            WebElement element = waitForElementAndGetIt(locator, timeoutInSecs);
            element.click();
            logger.info("✓ Clicked on element with locator: " + locator);
        } catch (Exception e) {
            logger.error("✗ Failed to click on locator " + locator + ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 4: Click using XPath string directly
     * Usage: click("//button[@id='submit']")
     */
    public void click(String xpath) {
        click(By.xpath(xpath), DEFAULT_WAIT_TIME);
    }
    
    /**
     * Overload 5: Click using XPath with custom timeout
     * Usage: click("//button[@id='submit']", 15)
     */
    public void click(String xpath, int timeoutInSecs) {
        click(By.xpath(xpath), timeoutInSecs);
    }
    
    /**
     * Overload 6: JavaScript Click (for hidden elements or overlay issues)
     * Usage: jsClick(element)
     */
    public void jsClick(WebElement element) {
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("✓ JavaScript clicked on element: " + element.getText());
        } catch (Exception e) {
            logger.error("✗ Failed to JS click: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 7: JS Click using By locator
     * Usage: jsClick(By.id("submitBtn"))
     */
    public void jsClick(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            jsClick(element);
        } catch (Exception e) {
            logger.error("✗ Failed to JS click using locator: " + e.getMessage());
            throw e;
        }
    }
    
    // ============================================
    // METHOD OVERLOADING FOR: SENDKEYS (TYPE TEXT)
    // ============================================
    
    /**
     * Overload 1: Type text in WebElement
     * Usage: sendKeys(emailElement, "test@gmail.com")
     */
    public void sendKeys(WebElement element, String text) {
        try {
            element.clear();
            element.sendKeys(text);
            logger.info("✓ Typed text: '" + text + "' in WebElement");
        } catch (Exception e) {
            logger.error("✗ Failed to type text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 2: Type text using By locator with default wait
     * Usage: sendKeys(By.id("email"), "test@gmail.com")
     */
    public void sendKeys(By locator, String text) {
        sendKeys(locator, text, DEFAULT_WAIT_TIME);
    }
    
    /**
     * Overload 3: Type text using By locator with custom timeout
     * Usage: sendKeys(By.id("email"), "test@gmail.com", 5)
     */
    public void sendKeys(By locator, String text, int timeoutInSecs) {
        try {
            WebElement element = waitForElementAndGetIt(locator, timeoutInSecs);
            sendKeys(element, text);
        } catch (Exception e) {
            logger.error("✗ Failed to type in locator: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 4: Type text using XPath string
     * Usage: sendKeys("//input[@name='email']", "test@gmail.com")
     */
    public void sendKeys(String xpath, String text) {
        sendKeys(By.xpath(xpath), text, DEFAULT_WAIT_TIME);
    }
    
    /**
     * Overload 5: Type text using XPath with custom timeout
     * Usage: sendKeys("//input[@name='email']", "test@gmail.com", 5)
     */
    public void sendKeys(String xpath, String text, int timeoutInSecs) {
        sendKeys(By.xpath(xpath), text, timeoutInSecs);
    }
    
    /**
     * Overload 6: Type text with character delay (slower typing for real user simulation)
     * Usage: sendKeysWithDelay(element, "password123", 100)
     */
    public void sendKeysWithDelay(WebElement element, String text, long delayInMs) {
        try {
            element.clear();
            for (char c : text.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                Thread.sleep(delayInMs);
            }
            logger.info("✓ Typed text with delay: '" + text + "'");
        } catch (Exception e) {
            logger.error("✗ Failed to type with delay: " + e.getMessage());
            throw e;
        }
    }
    
    // ============================================
    // METHOD OVERLOADING FOR: GET TEXT
    // ============================================
    
    /**
     * Overload 1: Get text from WebElement
     * Usage: getText(element)
     */
    public String getText(WebElement element) {
        try {
            String text = element.getText();
            logger.info("✓ Retrieved text: '" + text + "'");
            return text;
        } catch (Exception e) {
            logger.error("✗ Failed to get text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 2: Get text using By locator with default wait
     * Usage: getText(By.xpath("//p[@class='message']"))
     */
    public String getText(By locator) {
        return getText(locator, DEFAULT_WAIT_TIME);
    }
    
    /**
     * Overload 3: Get text using By locator with custom timeout
     * Usage: getText(By.xpath("//p[@class='message']"), 5)
     */
    public String getText(By locator, int timeoutInSecs) {
        try {
            WebElement element = waitForElementAndGetIt(locator, timeoutInSecs);
            return getText(element);
        } catch (Exception e) {
            logger.error("✗ Failed to get text from locator: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload 4: Get text using XPath string
     * Usage: getText("//span[@class='error-message']")
     */
    public String getText(String xpath) {
        return getText(By.xpath(xpath), DEFAULT_WAIT_TIME);
    }
    
    // ============================================
    // METHOD OVERLOADING FOR: IS ELEMENT VISIBLE
    // ============================================
    
    /**
     * Overload 1: Check if WebElement is visible
     * Usage: isVisible(element)
     */
    public boolean isVisible(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            logger.info("✓ Element visibility: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.info("✗ Element is not visible");
            return false;
        }
    }
    
    /**
     * Overload 2: Check if element is visible using By locator
     * Usage: isVisible(By.id("confirmationMsg"))
     */
    public boolean isVisible(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return isVisible(element);
        } catch (Exception e) {
            logger.info("✗ Element with locator " + locator + " not found");
            return false;
        }
    }
    
    /**
     * Overload 3: Check if element is visible using XPath string
     * Usage: isVisible("//div[@class='success']")
     */
    public boolean isVisible(String xpath) {
        return isVisible(By.xpath(xpath));
    }
    
    // ============================================
    // HELPER METHODS
    // ============================================
    
    /**
     * Wait for element to be clickable and return it
     */
    private WebElement waitForElementAndGetIt(By locator, int timeoutInSecs) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecs));
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            logger.error("✗ Element not found within timeout: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for element to be present
     */
    public WebElement waitForElement(By locator, int timeoutInSecs) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecs));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("✗ Element not found: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get attribute value
     */
    public String getAttribute(WebElement element, String attributeName) {
        try {
            String value = element.getAttribute(attributeName);
            logger.info("✓ Retrieved attribute '" + attributeName + "': " + value);
            return value;
        } catch (Exception e) {
            logger.error("✗ Failed to get attribute: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overload: Get attribute using By locator
     */
    public String getAttribute(By locator, String attributeName) {
        try {
            WebElement element = driver.findElement(locator);
            return getAttribute(element, attributeName);
        } catch (Exception e) {
            logger.error("✗ Failed to get attribute from locator: " + e.getMessage());
            throw e;
        }
    }
}
```

---

### **Step 2: Modify basePage.java to Use Custom Methods**

```java
package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utilities.WebDriverActions;

public class basePage {
    protected WebDriver driver;
    protected WebDriverActions actions;  // ← Add this
    
    public basePage(WebDriver driver){
        this.driver = driver;
        this.actions = new WebDriverActions(driver);  // ← Initialize here
        PageFactory.initElements(driver, this);
    }
}
```

---

### **Step 3: Use Method Overloading in Page Objects**

Before (Original loginPage.java):
```java
public class loginPage extends basePage {
    @FindBy(xpath = "//input[@name='email']")
    WebElement email;
    
    public void setEmail(String emailId) {
        email.sendKeys(emailId);  // ← Basic, no logging
    }
}
```

After (With Method Overloading):
```java
package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class loginPage extends basePage {
    public loginPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath = "//input[@name='email']")
    WebElement email;
    
    @FindBy(xpath = "//input[@id='input-password']")
    WebElement password;
    
    @FindBy(xpath = "//input[@value='Login']")
    WebElement loginBtn;
    
    // ✅ Using method overloading - multiple ways to set email
    
    // Overload 1: Using WebElement directly
    public void setEmail(String emailId) {
        actions.sendKeys(email, emailId);
    }
    
    // Overload 2: Using XPath string
    public void setEmailByXpath(String emailId) {
        actions.sendKeys("//input[@name='email']", emailId);
    }
    
    // Overload 3: Using By locator
    public void setEmailByLocator(String emailId) {
        actions.sendKeys(By.xpath("//input[@name='email']"), emailId);
    }
    
    // Overload 4: Using XPath with custom timeout
    public void setEmailWithTimeout(String emailId, int timeout) {
        actions.sendKeys("//input[@name='email']", emailId, timeout);
    }
    
    // Overload 5: With character delay (slower typing)
    public void setEmailWithDelay(String emailId, long delayInMs) {
        actions.sendKeysWithDelay(email, emailId, delayInMs);
    }
    
    // ✅ Using method overloading for password
    public void setPassword(String pwd) {
        actions.sendKeys(password, pwd);
    }
    
    // ✅ Using method overloading for click
    public void clickLogin() {
        // Option 1: Direct WebElement
        actions.click(loginBtn);
        
        // Option 2: Using XPath
        // actions.click("//input[@value='Login']");
        
        // Option 3: Using By locator
        // actions.click(By.xpath("//input[@value='Login']"));
        
        // Option 4: Using JavaScript (if regular click fails)
        // actions.jsClick(loginBtn);
    }
    
    // ✅ Using method overloading to get text
    public String getErrorMessage() {
        // Multiple ways to retrieve error message:
        return actions.getText(By.xpath("//span[@class='error']"), 5);
    }
    
    // ✅ Using method overloading to verify visibility
    public boolean isLoginButtonVisible() {
        return actions.isVisible(loginBtn);
    }
}
```

---

### **Step 4: Use in Test Cases**

```java
package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.loginPage;
import testBases.BaseClass;

public class TC02_Login extends BaseClass {
    @Test(groups = {"Master","Sanity","Regression"})
    public void verifyAccountLogin(){
        logger.info("***** Starting TC02 *****");
        try {
            loginPage lp = new loginPage(driver);
            
            // ✅ Using method overloading - all these work:
            
            // Method 1: Using WebElement (fastest)
            lp.setEmail("test@gmail.com");
            
            // Method 2: Using XPath (alternative)
            // lp.setEmailByXpath("test@gmail.com");
            
            // Method 3: With custom timeout
            // lp.setEmailWithTimeout("test@gmail.com", 15);
            
            // Method 4: With character delay (real user simulation)
            // lp.setEmailWithDelay("test@gmail.com", 100);
            
            lp.setPassword("password123");
            lp.clickLogin();
            
            logger.info("***** Finished TC02 *****");
        }
        catch(Exception e){
            logger.error("Test Failed...");
            Assert.fail();
        }
    }
}
```

---

## **BENEFITS OF METHOD OVERLOADING**

| Benefit | Explanation |
|---------|------------|
| **Flexibility** | Same method name, call with WebElement, By, or XPath |
| **Readability** | Code is cleaner: `click(element)` vs `clickElement()`, `clickBy()` |
| **Reusability** | One method handles multiple parameter types |
| **Maintenance** | Easy to modify behavior in one place |
| **Less Code** | No need for `clickByXpath()`, `clickById()`, `clickByCss()` |
| **Type Safety** | Java compiler ensures correct parameter types |
| **Encapsulation** | All click logic encapsulated in one method |

---

## **COMPARISON: Before vs After Method Overloading**

### **❌ Before (Without Overloading):**
```java
// Messy - multiple methods with similar names
lp.setEmail(element);
lp.setEmailByXpath(xpath);
lp.setEmailByLocator(locator);
lp.setEmailWithWait(element, 5);
lp.setEmailWithDelay(element, 100);

// Too many similar methods
lp.clickButton(element);
lp.clickButtonByXpath(xpath);
lp.clickButtonByLocator(locator);
lp.jsClickButton(element);
```

### **✅ After (With Overloading):**
```java
// Clean and intuitive - one method name, different parameters
lp.setEmail(element);           // WebElement
lp.setEmail("//input");         // XPath string
lp.setEmail(By.id("email"));    // By locator
lp.setEmail("//input", 5);      // XPath with timeout

// Single click method, multiple ways to call
lp.click(element);
lp.click("//button");
lp.click(By.id("btn"));
lp.jsClick(element);
```

---

## **ADVANCED: Method Overloading with Different Return Types**

```java
public class WebDriverActions {
    
    // ✅ Even return type can vary with method overloading
    
    // Returns void
    public void click(WebElement element) {
        element.click();
    }
    
    // Returns boolean
    public boolean clickAndVerifyPageLoad(WebElement element) {
        element.click();
        return driver.getTitle() != null;
    }
    
    // Returns String
    public String clickAndGetText(WebElement element) {
        element.click();
        return element.getText();
    }
}
```

---

## **COMMON MISTAKES TO AVOID**

❌ **WRONG - Same name but only different return type:**
```java
public void click(WebElement element) { }
public boolean click(WebElement element) { }  // COMPILER ERROR!
// Parameters are same, only return type differs - NOT allowed
```

✅ **CORRECT - Same name, different parameters:**
```java
public void click(WebElement element) { }
public void click(By locator) { }           // Different type ✓
public void click(String xpath) { }         // Different type ✓
public void click(WebElement element, int timeout) { }  // More parameters ✓
```

---

## **REAL-WORLD EXAMPLE: Complete Implementation**

**Full usage in test:**
```java
@Test
public void testLoginWithMethodOverloading() {
    loginPage lp = new loginPage(driver);
    
    // All these method calls work - same method name, different overloads
    lp.actions.click(emailElement);                              // Overload 1
    lp.actions.click(By.id("email"));                           // Overload 2
    lp.actions.click("//input[@name='email']");                 // Overload 3
    lp.actions.click(By.id("email"), 5);                        // Overload 4
    lp.actions.jsClick(emailElement);                           // Overload 5
    
    lp.actions.sendKeys(pwdElement, "myPassword");              // Overload 1
    lp.actions.sendKeys(By.id("pwd"), "myPassword");            // Overload 2
    lp.actions.sendKeys("//input[@id='pwd']", "myPassword");    // Overload 3
    lp.actions.sendKeys("//input[@id='pwd']", "pass", 10);      // Overload 4
    lp.actions.sendKeysWithDelay(pwdElement, "pass", 100);      // Overload 5
    
    String msg = lp.actions.getText(msgElement);                // Overload 1
    String msg = lp.actions.getText(By.id("msg"));              // Overload 2
    String msg = lp.actions.getText("//span[@class='msg']");    // Overload 3
    
    boolean visible = lp.actions.isVisible(element);            // Overload 1
    boolean visible = lp.actions.isVisible(By.id("btn"));       // Overload 2
    boolean visible = lp.actions.isVisible("//button");         // Overload 3
}
```

---

## **SUMMARY**

| Step | Action |
|------|--------|
| **1** | Create `WebDriverActions.java` utility class with overloaded methods |
| **2** | Modify `basePage.java` to include `WebDriverActions` instance |
| **3** | Update page objects to use `actions.click()`, `actions.sendKeys()`, etc. |
| **4** | Use different parameter types: WebElement, By, XPath string, or timeout values |
| **5** | Test cases automatically benefit from all overloads |

✅ **Result:** Cleaner code, better maintainability, and professional test framework!
