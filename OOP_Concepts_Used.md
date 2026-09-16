# OOP Concepts Used in Your Java Selenium Hybrid Framework

## Overview
Your framework demonstrates all 4 core OOP concepts:
1. **Inheritance** ✅
2. **Encapsulation** ✅
3. **Polymorphism** ✅
4. **Abstraction** ✅

---

## 1. **INHERITANCE** ✅

**Definition**: A class inheriting properties and methods from a parent class using `extends` keyword.

### **Where Used:**

#### **A) Page Object Inheritance Hierarchy**

**Parent Class:**
```java
// basePage.java
public class basePage {
    WebDriver driver;
    
    basePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
```

**Child Classes (Page Objects):**
```java
// homePage.java
public class homePage extends basePage {
    public homePage(WebDriver driver){
        super(driver);  // Calls parent constructor
    }
    
    public void clickMyAccount() { }
    public void clickRegister() { }
    public void clickLogin() { }
}
```

```java
// loginPage.java
public class loginPage extends basePage {
    public loginPage(WebDriver driver){
        super(driver);  // Calls parent constructor
    }
    
    public void setEmail(String emailId) { }
    public void setPassword(String pwd) { }
    public void clickLogin() { }
}
```

```java
// AccountRegisterpage.java
public class AccountRegisterpage extends basePage {
    public AccountRegisterpage(WebDriver driver){
        super(driver);  // Calls parent constructor
    }
    
    public void setFirstName(String fname) { }
    public void setLastName(String lname) { }
    // ... more methods
}
```

**Why Inheritance Here?**
- ✅ All page objects need the `driver` instance
- ✅ All need `PageFactory.initElements()` initialization
- ✅ Avoids code duplication
- ✅ Single source of truth for common page functionality

---

#### **B) Test Class Inheritance Hierarchy**

**Parent Class:**
```java
// BaseClass.java
public class BaseClass {
    public static WebDriver driver;
    public Logger logger;
    public Properties p;
    
    @BeforeClass
    public void setup(String os, String br) throws IOException {
        // Driver initialization
        // Config loading
        // Implicit waits
    }
    
    @AfterClass
    public void teardown(){
        // Browser cleanup
    }
    
    public String randomAlpha() { }
    public String randomPhno() { }
    public String randomAlphaNum() { }
    public String captureScreen(String tname) { }
}
```

**Child Classes (Test Cases):**
```java
// TC01_Registration.java
public class TC01_Registration extends BaseClass {
    @Test(groups = {"Master","Sanity","Regression"})
    public void verifyAccountregistration(){
        // Inherits: driver, logger, randomAlpha(), captureScreen()
        logger.info("Starting test...");
        String email = randomAlpha() + "@gmail.com";
    }
}
```

```java
// TC02_Login.java
public class TC02_Login extends BaseClass {
    @Test(groups = {"Master","Sanity","Regression"})
    public void verifyAccountLogin(){
        // Inherits: driver, logger, randomPhno(), captureScreen()
        logger.info("Starting test...");
    }
}
```

**Why Inheritance Here?**
- ✅ All tests need browser setup (driver initialization)
- ✅ All tests need logging capabilities
- ✅ All tests need random data generation
- ✅ All tests need screenshot capability
- ✅ One `@BeforeClass` and `@AfterClass` for all tests

**Inheritance Benefit:**
```
Before inheritance (Bad):
- Each test class has 50+ lines of setup code
- Duplicate code everywhere
- Changes to setup = update 10 test classes

After inheritance (Good):
- Setup code in ONE place (BaseClass)
- All tests inherit it automatically
- Change setup once = all tests benefit
```

---

## 2. **ENCAPSULATION** ✅

**Definition**: Bundling data (variables) and methods together, hiding internal details, exposing only what's necessary.

### **Where Used:**

#### **A) Private WebElements, Public Methods**

```java
// loginPage.java
public class loginPage extends basePage {
    
    // PRIVATE - Hidden from outside
    @FindBy(xpath = "//input[@name='email']")
    private WebElement email;  // Not directly accessible
    
    @FindBy(xpath = "//input[@id='input-password']")
    private WebElement password;  // Not directly accessible
    
    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginBtn;  // Not directly accessible
    
    // PUBLIC - Exposed methods to interact with private elements
    public void setEmail(String emailId){
        email.sendKeys(emailId);
    }
    
    public void setPassword(String pwd){
        password.sendKeys(pwd);
    }
    
    public void clickLogin(){
        loginBtn.click();
    }
}
```

**Test Usage:**
```java
// TC02_Login.java
loginPage lp = new loginPage(driver);

// Can use public methods
lp.setEmail("test@gmail.com");
lp.setPassword("password123");

// CANNOT access private elements directly
// lp.email.sendKeys("test@gmail.com");  // COMPILATION ERROR!
// lp.password.click();  // COMPILATION ERROR!
```

**Why Encapsulation Here?**
- ✅ **Data Hiding**: XPath locators are hidden internal details
- ✅ **Controlled Access**: Only through public methods
- ✅ **Flexibility**: Can change implementation without breaking tests
- ✅ **Protection**: Prevents accidental element manipulation

**Example - Easy Maintenance:**
```
Before: Element XPath changes
- Need to update 10 test files
- High risk of breaking tests

After (with Encapsulation): Element XPath changes
- Update ONLY setEmail() method in loginPage.java
- All tests still work!
- No test code changes needed
```

---

#### **B) Properties Encapsulation**

```java
// BaseClass.java
public class BaseClass {
    public static WebDriver driver;  // Shared across all tests
    public Logger logger;  // Each test instance gets its own logger
    public Properties p;  // Configuration properties
    
    @BeforeClass
    public void setup(String os, String br) throws IOException {
        // Internal implementation hidden
        FileReader fr = new FileReader("./src//test//resources//config.properties");
        p = new Properties();
        p.load(fr);
        // Details of driver initialization hidden
    }
}
```

**Test Usage:**
```java
// TC02_Login.java
public class TC02_Login extends BaseClass {
    @Test
    public void verifyAccountLogin(){
        // Tests only access what they need
        lp.setEmail(p.getProperty("email"));  // Use property
        logger.info("Test started");  // Use logger
        
        // Don't need to know HOW logger or config is initialized
        // That's encapsulated in BaseClass
    }
}
```

---

#### **C) ExcelUtils Encapsulation**

```java
// ExcelUtils.java
public class ExcelUtils {
    
    // PRIVATE - Internal state, hidden from users
    private FileInputStream fi;
    private FileOutputStream fo;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFRow row;
    private XSSFCell cell;
    
    // PUBLIC - Only expose what's needed
    public String getCellData(String sheetName, int rownum, int column) 
        throws IOException {
        // Complex logic hidden here
        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        // ... more complex operations
        return data;
    }
    
    public void setCellData(String sheetName, int rownum, 
                           int column, String data) throws IOException {
        // Complex logic hidden here
    }
}
```

**DataProviders Usage:**
```java
// DataProviders.java
public class DataProviders {
    @DataProvider(name="LoginData")
    public String[][] getData() throws IOException {
        ExcelUtils xlutil = new ExcelUtils(path);
        
        // Only calls public methods
        int totalrows = xlutil.getRowCount("Sheet1");
        int totalcols = xlutil.getCellCount("Sheet1", 1);
        String logindata[][] = new String[totalrows][totalcols];
        
        for(int i=1; i<=totalrows; i++) {
            for(int j=0; j<totalcols; j++) {
                // Simple method call - complex logic is encapsulated
                logindata[i-1][j] = xlutil.getCellData("Sheet1", i, j);
            }
        }
        
        return logindata;
    }
}
```

**Benefit:**
- User just calls `xlutil.getCellData()` - simple
- Complex Excel operations hidden internally
- Can change Excel implementation without affecting DataProviders

---

## 3. **POLYMORPHISM** ✅

**Definition**: Multiple forms of the same method name, different implementations based on context.

### **Types of Polymorphism Used:**

#### **A) Method Overloading (Compile-time Polymorphism)**

In your framework's **BaseClass.java**:

```java
// randomAlpha() - generates random alphabetic string
public String randomAlpha(){
    String randStr = RandomStringUtils.randomAlphabetic(5);
    return randStr;  // Returns: "AaBbC"
}

// randomPhno() - generates random numeric string
public String randomPhno(){
    String randStr = RandomStringUtils.randomNumeric(10);
    return randStr;  // Returns: "1234567890"
}

// randomAlphaNum() - generates random alphanumeric
public String randomAlphaNum(){
    String randStr = RandomStringUtils.randomAlphabetic(5);
    String randNum = RandomStringUtils.randomNumeric(3);
    return (randStr+"$"+randNum);  // Returns: "AaBbC$123"
}
```

**Same concept, different method names & implementations:**

```java
// TC01_Registration.java - Test usage
reg.setFirstName(randomAlpha().toUpperCase());  // "AABBC"
reg.setTelephone(randomPhno());  // "1234567890"
reg.setPassword(randomAlphaNum());  // "AaBbC$123"
```

**Better example - If you had overloading:**
```java
// Hypothetical - Method Overloading Example
public String random(int length) {
    return RandomStringUtils.randomAlphabetic(length);  // 5 chars
}

public String random(int length, String type) {
    if(type.equals("numeric")) {
        return RandomStringUtils.randomNumeric(length);  // 10 numbers
    } else if(type.equals("alphanumeric")) {
        return RandomStringUtils.randomAlphaNumeric(length);  // Mix
    }
    return "";
}

// Usage - Same method name, different parameters
randomAlpha(5);
randomAlpha(10, "numeric");
randomAlpha(8, "alphanumeric");
```

---

#### **B) Method Overriding (Runtime Polymorphism)**

**Parent Class Method:**
```java
// basePage.java
public class basePage {
    WebDriver driver;
    
    basePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
```

**Child Class Overrides Constructor:**
```java
// homePage.java
public class homePage extends basePage {
    public homePage(WebDriver driver){
        super(driver);  // Calls parent constructor
        // Could add homePage-specific initialization here
    }
}

// loginPage.java
public class loginPage extends basePage {
    public loginPage(WebDriver driver){
        super(driver);  // Calls parent constructor
        // Different implementation if needed
    }
}
```

**Runtime Polymorphism Example:**
```java
// TC01_Registration.java
homePage hp = new homePage(driver);  // homePage version
AccountRegisterpage reg = new AccountRegisterpage(driver);  // AccountRegisterpage version

// Both inherit from basePage, but each has its own constructor
// Runtime determines which one to call based on object type
```

**If basePage was abstract:**
```java
// Better Polymorphism Example (Hypothetical improvement):
public abstract class basePage {
    WebDriver driver;
    
    basePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    // Abstract method - must be implemented by child classes
    public abstract void navigateToPage();
}

public class loginPage extends basePage {
    @Override  // Explicitly shows method overriding
    public void navigateToPage() {
        // loginPage specific navigation
    }
}

public class homePage extends basePage {
    @Override  // Different implementation
    public void navigateToPage() {
        // homePage specific navigation
    }
}
```

---

#### **C) TestNG Annotations - Method Overriding Pattern**

```java
// BaseClass.java
public class BaseClass {
    @BeforeClass
    public void setup(String os, String br) throws IOException {
        // Setup logic
    }
    
    @AfterClass
    public void teardown(){
        // Cleanup logic
    }
}

// TC01_Registration.java inherits these
public class TC01_Registration extends BaseClass {
    @Test
    public void verifyAccountregistration(){
        // setup() runs before this
        // teardown() runs after this (INHERITED from BaseClass)
    }
}

// TC02_Login.java also inherits
public class TC02_Login extends BaseClass {
    @Test
    public void verifyAccountLogin(){
        // SAME setup() and teardown() run
        // (Inherited from BaseClass, overridden at runtime)
    }
}
```

---

## 4. **ABSTRACTION** ✅

**Definition**: Hiding complex implementation details, showing only essential features.

### **Where Used:**

#### **A) Business Logic Abstraction in Test Cases**

**Complex Implementation Hidden:**
```java
// How Selenium works (ABSTRACTION - we don't care about details)
// We DON'T write:
new WebDriverWait(driver, Duration.ofSeconds(10))
    .until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//input[@name='email']")
    )).sendKeys("test@gmail.com");

// Instead, we get ABSTRACTION:
lp.setEmail("test@gmail.com");  // Simple, abstract, business-focused
```

**Test Level - High Abstraction:**
```java
// TC01_Registration.java
public class TC01_Registration extends BaseClass {
    @Test
    public void verifyAccountregistration(){
        logger.info("Starting TC01...");
        homePage hp = new homePage(driver);
        hp.clickMyAccount();  // ABSTRACTION - don't see Selenium code
        hp.clickRegister();   // ABSTRACTION - internal details hidden
        
        AccountRegisterpage reg = new AccountRegisterpage(driver);
        reg.setFirstName(randomAlpha());  // ABSTRACTION
        reg.setLastName(randomAlpha());   // ABSTRACTION
        reg.setEmail(randomAlpha() + "@gmail.com");  // ABSTRACTION
        
        String cnfMsg = reg.getConfirmationMsg();  // ABSTRACTION
        Assert.assertEquals(cnfMsg, "Your Account Has Been Created!");
    }
}
```

**What's abstracted?**
- ✅ XPath locators
- ✅ WebElement interactions
- ✅ Selenium waits
- ✅ Exception handling
- ✅ Error logging

**Test focuses only on:**
- Business logic (registration flow)
- Test scenarios (assertions)

---

#### **B) BaseClass Abstraction**

**Complex Setup Abstracted:**
```java
// BaseClass.java - HIDES all this complexity
public class BaseClass {
    @BeforeClass
    public void setup(String os, String br) throws IOException {
        FileReader fr = new FileReader("./src//test//resources//config.properties");
        p = new Properties();
        p.load(fr);  // Property loading abstracted
        
        logger = (Logger) LogManager.getLogger(this.getClass());  // Logger setup
        
        switch(br.toLowerCase()) {
            case "chrome": driver = new ChromeDriver(); break;  // Driver creation
            case "edge": driver = new EdgeDriver(); break;
            default: logger.error("Wrong Browser..."); return;
        }
        
        driver.manage().deleteAllCookies();  // Browser management
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  // Waits
        driver.get(p.getProperty("appurl"));  // Navigation
        driver.manage().window().maximize();  // Window management
    }
}

// Test Class - Only uses what it needs
public class TC01_Registration extends BaseClass {
    @Test
    public void verifyAccountregistration(){
        // driver is ready - abstracted away!
        homePage hp = new homePage(driver);
        // Don't care about: FileReader, logger, switch statement, etc.
    }
}
```

---

#### **C) Excel Data Abstraction**

**Complex Excel Operations Abstracted:**
```java
// ExcelUtils.java - Complex implementation hidden
public class ExcelUtils {
    
    public String getCellData(String sheetName, int rownum, int column) 
        throws IOException {
        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = row.getCell(column);
        
        DataFormatter formatter = new DataFormatter();
        
        String data;
        try {
            data = formatter.formatCellValue(cell);
        } catch (Exception e) {
            data = "";
        }
        
        workbook.close();
        fi.close();
        
        return data;
    }
}

// DataProviders.java - Simple abstraction
@DataProvider(name="LoginData")
public String[][] getData() throws IOException {
    ExcelUtils xlutil = new ExcelUtils(path);
    
    int totalrows = xlutil.getRowCount("Sheet1");
    int totalcols = xlutil.getCellCount("Sheet1", 1);
    String logindata[][] = new String[totalrows][totalcols];
    
    for(int i=1; i<=totalrows; i++) {
        for(int j=0; j<totalcols; j++) {
            logindata[i-1][j] = xlutil.getCellData("Sheet1", i, j);
        }
    }
    
    return logindata;
}
```

**What's abstracted?**
- Excel file operations (FileInputStream, XSSFWorkbook, XSSFSheet, etc.)
- Cell formatting (DataFormatter)
- Error handling
- File closing

**DataProviders only cares about:**
- Get row count
- Get column count
- Get cell value
- Return data array

---

#### **D) Screenshot Abstraction**

**Complex Implementation:**
```java
// BaseClass.java - HIDES complexity
public String captureScreen(String tname) throws IOException {
    String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    
    TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
    File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
    
    String targetFilePath = System.getProperty("user.dir") 
                          + "\\screenshots\\" 
                          + tname + "_" + timeStamp + ".png";
    
    File targetFile = new File(targetFilePath);
    FileUtils.copyFile(sourceFile, targetFile);
    
    return targetFilePath;
}

// Test Code - SIMPLE abstraction
public class TC01_Registration extends BaseClass {
    @Test
    public void verifyAccountregistration(){
        try {
            // ... test code
        }
        catch(Exception e){
            captureScreen("TC01_Registration");  // One line!
        }
    }
}
```

---

## 5. **Summary: OOP in Your Framework**

| OOP Concept | Where Used | Example | Benefit |
|-------------|-----------|---------|---------|
| **Inheritance** | basePage → all page objects | `extends basePage` | Code reuse, single source of truth |
| | BaseClass → all test classes | `extends BaseClass` | Common setup/teardown |
| **Encapsulation** | Private WebElements | `private WebElement email` | Hide implementation, expose methods |
| | Private utility methods | Package-level access | Control who can use what |
| **Polymorphism** | Method overloading | `randomAlpha()`, `randomPhno()` | Different implementations, same concept |
| | Runtime type dispatch | Constructor inheritance | Flexible object creation |
| **Abstraction** | Page Object Methods | `setEmail()` instead of raw Selenium | Business-focused code |
| | BaseClass utilities | `captureScreen()` | Hide complex operations |
| | ExcelUtils operations | `getCellData()` | Simplify Excel interactions |

---

## 6. **Visual Diagram - Class Hierarchy**

```
                    ┌─────────────────┐
                    │   basePage      │
                    │  (Parent)       │
                    │ - driver        │
                    │ - __init__()    │
                    └────────┬────────┘
                             │
             ┌───────────────┼───────────────┐
             │               │               │
        ┌────▼────┐   ┌──────▼──────┐   ┌───▼──────────┐
        │homePage │   │ loginPage   │   │AccountRegister│
        │ (Child) │   │  (Child)    │   │page(Child)   │
        │ +click..│   │ +setEmail() │   │ +setFirst... │
        │ +click..│   │ +setPass..  │   │ +setLast...  │
        └────────┘   └─────────────┘   └──────────────┘
        
        
                    ┌──────────────────┐
                    │   BaseClass      │
                    │  (Parent)        │
                    │ - driver         │
                    │ - logger         │
                    │ - setup()        │
                    │ - teardown()     │
                    │ - randomAlpha()  │
                    │ - captureScreen()│
                    └─────────┬────────┘
                              │
             ┌────────────────┼────────────────┐
             │                │                │
        ┌────▼──────┐  ┌──────▼────┐  ┌───────▼────────┐
        │TC01_Regis │  │ TC02_Login │  │ TC03_LoginDDP  │
        │ tration   │  │  (Child)   │  │  (Child)       │
        │ (Child)   │  │ +verify... │  │ +verify...     │
        │ +verify..│  └────────────┘  └────────────────┘
        └───────────┘
```

---

## 7. **Interview Answer Template**

**When interviewer asks: "What OOP concepts have you used?"**

**Answer:**
*"I've used all four OOP concepts:*

*1. **Inheritance**: basePage is the parent class for all page objects (homePage, loginPage, etc.). BaseClass is the parent for all test classes. This eliminates code duplication for common functionality like driver initialization and page factory setup.*

*2. **Encapsulation**: WebElements are private, only exposed through public methods like setEmail(), setPassword(). This hides implementation details and makes tests resilient to UI changes.*

*3. **Polymorphism**: Multiple child classes inherit from basePage and BaseClass, each with their own implementations. Also, I have multiple random data generation methods with different logic.*

*4. **Abstraction**: Tests focus on business logic ('verify registration'), not technical details. Complex operations like Excel handling, screenshot capture, and browser setup are abstracted into utility methods.*

*This makes the framework scalable, maintainable, and easy to extend."*

---

This is a textbook example of proper OOP design in Test Automation! 🎯
