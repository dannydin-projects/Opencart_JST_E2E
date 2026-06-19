package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class loginPage extends basePage{
    public loginPage(WebDriver driver){
        super(driver);
    }

    @FindBy(xpath = "//input[@name='email']")
    WebElement email;
    @FindBy(xpath = "//input[@id='input-password']")
    WebElement password;
    @FindBy(xpath = "//input[@value='Login']")
    WebElement loginBtn;

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
