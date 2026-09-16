# TestNG Retry Logic Implementation Guide

## **What is Retry Logic?**

Retry logic automatically re-runs failed tests to handle flaky tests (element not found, timeouts, network issues).

**Without Retry:**
```
Test fails once → Test marked as FAILED → Suite continues
```

**With Retry (3 attempts):**
```
Test fails on Attempt 1 → Retry
Test fails on Attempt 2 → Retry
Test passes on Attempt 3 → Test marked as PASSED ✓
```

---

## **STEP 1: Create IRetryAnalyzer Implementation**

Create file: `src/test/java/utilities/RetryAnalyzer.java`

```java
package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = (Logger) LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 3;  // Retry failed tests 3 times
    
    /**
     * Called after each test failure
     * Returns true to retry the test, false to mark as failed
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("⚠️ RETRYING TEST: " + result.getName() 
                    + " (Attempt " + (retryCount + 1) + "/" + (MAX_RETRY_COUNT + 1) + ")");
            return true;  // ← Retry this test
        }
        logger.error("❌ TEST FAILED AFTER " + MAX_RETRY_COUNT + " RETRIES: " + result.getName());
        return false;  // ← No more retries, mark as failed
    }
}
```

---

## **STEP 2: Create Custom Retry Annotation (Optional but Recommended)**

Create file: `src/test/java/annotations/Retry.java`

```java
package annotations;

import utilities.RetryAnalyzer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark tests that should be retried on failure
 * Usage: @Test @Retry
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int times() default 3;
}
```

---

## **STEP 3: Create Advanced RetryAnalyzer with Configurable Retries**

Create file: `src/test/java/utilities/AdvancedRetryAnalyzer.java`

```java
package utilities;

import annotations.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class AdvancedRetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = (Logger) LogManager.getLogger(AdvancedRetryAnalyzer.class);
    private int retryCount = 0;
    
    @Override
    public boolean retry(ITestResult result) {
        // Get max retry count from @Retry annotation
        Retry retryAnnotation = result.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(Retry.class);
        
        int maxRetries = (retryAnnotation != null) ? retryAnnotation.times() : 2;
        
        // Skip retry for certain exceptions
        if (shouldNotRetry(result)) {
            logger.info("⏭️ SKIPPING RETRY - Assertion/Configuration Error: " 
                    + result.getName());
            return false;
        }
        
        if (retryCount < maxRetries) {
            retryCount++;
            String testName = result.getName();
            String exception = result.getThrowable().getClass().getSimpleName();
            
            logger.warn("⚠️ RETRYING: " + testName 
                    + " (Attempt " + (retryCount + 1) + "/" + (maxRetries + 1) + ")"
                    + " | Error: " + exception);
            
            return true;  // Retry
        }
        
        retryCount = 0;  // Reset for next test
        return false;    // No more retries
    }
    
    /**
     * Don't retry for assertion failures or configuration errors
     */
    private boolean shouldNotRetry(ITestResult result) {
        Throwable throwable = result.getThrowable();
        
        return throwable instanceof AssertionError
                || throwable instanceof IllegalArgumentException
                || throwable instanceof NullPointerException;
    }
}
```

---

## **STEP 4: Create Listener to Show Retry Info in Reports**

Create file: `src/test/java/utilities/RetryListener.java`

```java
package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class RetryListener implements ITestListener {
    
    private static final Logger logger = (Logger) LogManager.getLogger(RetryListener.class);
    
    @Override
    public void onTestFailure(ITestResult result) {
        // Check if test will be retried
        if (result.getMethod().getRetryAnalyzer() != null) {
            logger.info("📊 Test marked for retry: " + result.getName());
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        // Check if this test was retried
        int passCount = result.getTestContext().getPassedTests().size();
        if (result.getTestContext().getFailedButWithinSuccessPercentageTests()
                .getResults(result.getMethod()).size() > 0) {
            logger.info("✅ TEST PASSED ON RETRY: " + result.getName());
        }
    }
}
```

---

## **STEP 5: Apply RetryAnalyzer to Tests**

### **Method 1: Individual Test with @Test annotation**

Update `src/test/java/testCases/TC02_Login.java`:

```java
package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.loginPage;
import testBases.BaseClass;
import utilities.AdvancedRetryAnalyzer;

public class TC02_Login extends BaseClass {
    
    @Test(groups = {"Master","Sanity","Regression"}, retryAnalyzer = AdvancedRetryAnalyzer.class)
    public void verifyAccountLogin(){
        logger.info("***** Starting TC02 *****");
        try {
            loginPage lp = new loginPage(driver);
            lp.setEmail(p.getProperty("email"));
            lp.setPassword(p.getProperty("password"));
            lp.clickLogin();
            Assert.assertTrue(isMyAccDisplayed());
            logger.info("***** Finished TC02 *****");
        }
        catch(Exception e){
            logger.error("Test Failed...");
            Assert.fail();
        }
    }
}
```

### **Method 2: Using Custom @Retry Annotation**

```java
@Test(groups = {"Master","Sanity","Regression"}, retryAnalyzer = AdvancedRetryAnalyzer.class)
@Retry(times = 5)  // Retry up to 5 times
public void verifyAccountLogin(){
    // Test logic
}
```

### **Method 3: BaseClass Level (All tests inherit retry)**

Update `src/test/java/testBases/BaseClass.java`:

```java
public class BaseClass {
    // ... existing code
    
    @BeforeClass(groups = {"Master","Sanity","Regression"})
    @Parameters({"os","browser"})
    public void setup(String os, String br) throws IOException {
        // Existing setup code
    }
    
    // All extending classes will have retry enabled
}
```

Apply in XML:
```xml
<test name="LoginTest" retryAnalyzer="utilities.AdvancedRetryAnalyzer">
    <classes>
        <class name="testCases.TC02_Login"/>
    </classes>
</test>
```

---

## **STEP 6: Update XML Configuration**

Update `master.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Master Suite">
    <listeners>
        <listener class-name="utilities.ExtentReportManager"></listener>
        <listener class-name="utilities.RetryListener"></listener>  <!-- ← Add this -->
    </listeners>
    
    <test verbose="2" preserve-order="true" name="Master Tests">
        <parameter name="os" value="Windows" />
        <parameter name="browser" value="chrome" />
        <classes>
            <class name="testCases.TC01_Registration"/>
            <class name="testCases.TC02_Login"/>
            <class name="testCases.TC03_LoginDDP"/>
        </classes>
    </test>
</suite>
```

---

## **STEP 7: Configuration Properties File**

Create/Update `src/test/resources/retry.properties`:

```properties
# Retry Configuration
max.retry.count=3
retry.delay.seconds=2
exclude.retry.exceptions=AssertionError,IllegalArgumentException
include.retry.groups=Sanity,Regression
exclude.retry.groups=Smoke
```

---

## **STEP 8: Advanced RetryAnalyzer with Delay**

Update `AdvancedRetryAnalyzer.java` to add delay between retries:

```java
package utilities;

import annotations.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class AdvancedRetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = (Logger) LogManager.getLogger(AdvancedRetryAnalyzer.class);
    private int retryCount = 0;
    private static final int DEFAULT_RETRY_DELAY = 2;  // seconds
    
    @Override
    public boolean retry(ITestResult result) {
        Retry retryAnnotation = result.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(Retry.class);
        
        int maxRetries = (retryAnnotation != null) ? retryAnnotation.times() : 2;
        
        if (shouldNotRetry(result)) {
            return false;
        }
        
        if (retryCount < maxRetries) {
            retryCount++;
            
            // Wait before retrying (allows server/resources to stabilize)
            long delayMs = DEFAULT_RETRY_DELAY * 1000;
            try {
                logger.info("⏳ Waiting " + DEFAULT_RETRY_DELAY + " seconds before retry...");
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            String testName = result.getName();
            String exception = result.getThrowable().getClass().getSimpleName();
            logger.warn("⚠️ RETRYING: " + testName 
                    + " (Attempt " + (retryCount + 1) + "/" + (maxRetries + 1) + ")"
                    + " | Error: " + exception);
            
            return true;
        }
        
        retryCount = 0;
        return false;
    }
    
    private boolean shouldNotRetry(ITestResult result) {
        Throwable throwable = result.getThrowable();
        
        return throwable instanceof AssertionError
                || throwable instanceof IllegalArgumentException
                || throwable instanceof NullPointerException;
    }
}
```

---

## **USAGE EXAMPLES**

### **Example 1: Basic Retry (All Tests)**
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void testLogin() {
    // Auto-retry on failure
}
```

### **Example 2: Custom Retry Count**
```java
@Test(retryAnalyzer = AdvancedRetryAnalyzer.class)
@Retry(times = 5)
public void testFlakyFeature() {
    // Retry up to 5 times
}
```

### **Example 3: Conditional Retry (Only certain groups)**
```java
@Test(groups = {"Sanity"}, retryAnalyzer = AdvancedRetryAnalyzer.class)
public void criticalTest() {
    // Only Sanity tests retry
}
```

### **Example 4: Skip Retry for Known Issues**
```java
@Test(retryAnalyzer = AdvancedRetryAnalyzer.class)
public void testWithKnownIssue() {
    try {
        // Test logic
    } catch (AssertionError e) {
        // Assertion errors won't be retried
        throw e;
    }
}
```

---

## **REPORT OUTPUT**

With retry logic enabled, you'll see:

```
FAILED: testCases.TC02_Login.verifyAccountLogin
⚠️ RETRYING TEST: verifyAccountLogin (Attempt 2/3)

FAILED: testCases.TC02_Login.verifyAccountLogin
⚠️ RETRYING TEST: verifyAccountLogin (Attempt 3/3)

PASSED: testCases.TC02_Login.verifyAccountLogin
✅ TEST PASSED ON RETRY
```

**Extent Report shows:**
- Original failure with screenshot
- Retry attempts with timestamps
- Final result (PASS/FAIL)

---

## **BENEFITS**

✅ Automatic flaky test handling  
✅ Reduces false failures  
✅ Saves time (no manual re-run)  
✅ Configurable per test  
✅ Improves pipeline stability  
✅ Tracks retry attempts in reports  

---

## **BEST PRACTICES**

| Practice | Reason |
|----------|--------|
| **Max 3 retries** | More = waste of time, indicates real issue |
| **Add delay between retries** | Allows resources to stabilize |
| **Skip retry for AssertionError** | Real test failure, not flakiness |
| **Log retry attempts** | Audit trail for debugging |
| **Use for Sanity/Regression** | Not for Smoke tests |
| **Monitor retry metrics** | Identify consistently flaky tests |

---

## **TESTING RETRY LOGIC**

```java
@Test(retryAnalyzer = AdvancedRetryAnalyzer.class)
@Retry(times = 3)
public void testRetryLogic() {
    int attemptNumber = getAttemptNumber();  // 1, 2, or 3
    
    if (attemptNumber < 3) {
        Assert.fail("Intentional fail for retry test");  // Fail first 2 times
    }
    Assert.assertTrue(true);  // Pass on 3rd attempt
}
```

Expected result: **PASSED** ✅ (after 2 retries)

