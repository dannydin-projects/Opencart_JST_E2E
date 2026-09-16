package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.WebDriverActions;

import java.time.Duration;

public class homePage extends basePage {
    public homePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//a[contains(@class, 'dropdown-toggle')]//span[contains(text(), 'My Account')]/..")
    WebElement lnkMyAccount;

    @FindBy(xpath = "//li//a[normalize-space()='Register']")
    WebElement lnkRegister;

    @FindBy(xpath = "//li//a[text()='Login']")
    WebElement lnkLogin;

    public void clickMyAccount() {
        WebDriverActions wa = new WebDriverActions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(lnkMyAccount));
        wa.click(lnkMyAccount);
    }

    public void clickRegister() {
        WebDriverActions wa = new WebDriverActions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(lnkRegister));
        wa.click(lnkRegister);
    }

    public void clickLogin() {
        WebDriverActions wa = new WebDriverActions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(lnkLogin));
        wa.click(lnkLogin);
    }
}
