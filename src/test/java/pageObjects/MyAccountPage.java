package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends basePage{
    public MyAccountPage(WebDriver driver){
        super(driver);
    }

    @FindBy(xpath = "//h2[text()= 'My Account']")
    WebElement myAcc;
    @FindBy(xpath = "//a[@class='list-group-item'][normalize-space()='Logout']")
    WebElement logoutBtn;

    public boolean isMyAccDisplayed() {
        try {
            return myAcc.isDisplayed();
        }catch(Exception e){
            return false;
        }
    }
    public void clickLogout(){
        logoutBtn.click();
    }
}
