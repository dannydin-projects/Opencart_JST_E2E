package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.WebDriverActions;

import java.time.Duration;

public class homePage extends basePage{
    public homePage(WebDriver driver){
        super(driver);
    }

    @FindBy(xpath="//li//a[contains(text(), 'My Account')]")
    WebElement lnkMyAccount;

    @FindBy(xpath="//a[normalize-space()='Register']")
    WebElement lnkRegister;

    @FindBy(xpath="//a[normalize-space()='Login']")
    WebElement lnkLogin;

    public void clickMyAccount()
    {
        WebDriverActions wa = new WebDriverActions(driver);
        By locator = By.xpath("//a[contains(@class, 'dropdown-toggle')]//span[contains(text(), 'My Account')] | //div[contains(@class, 'dropdown')]//a[contains(text(), 'My Account')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        wait.until(ExpectedConditions.elementToBeClickable(lnkMyAccount));
        wa.click(lnkMyAccount);
    }

    public void clickRegister()
    {
        lnkRegister.click();
    }

    public void clickLogin()
    {
        lnkLogin.click();
    }}
