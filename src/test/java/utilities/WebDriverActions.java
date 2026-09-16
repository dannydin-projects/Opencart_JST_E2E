package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebDriverActions {
    private WebDriver driver;
    private static Logger logger;

    public WebDriverActions(WebDriver driver){
        this.driver = driver;
        this.logger = (Logger) LogManager.getLogger(WebDriverActions.class);
    }

    public void click(WebElement element){
        try {
            element.click();
            logger.info("✓ Clicked on WebElement: " + element.getText());
        }
        catch(ElementClickInterceptedException eci){
            click(element,true);
        }
        catch (Exception e){
            logger.error("✗ Failed to click WebElement: " + e.getMessage());
            throw e;
        }
    }
    public void click(By locator){
        try {
            click(locator, 10);
        }catch(Exception e){
            logger.error("✗ Failed to locate WebElement: " + e.getMessage());
            throw e;
        }
    }

    public void click(By locator, int timeOutSecs){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutSecs));
            WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(locator));
            click(ele);
        }
        catch(Exception e){
            logger.error("✗ Failed to locate WebElement within "+ timeOutSecs + ":" + e.getMessage());
            throw e;
        }
    }

    private void click(WebElement element, boolean jsExecute){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("✓ JavaScript clicked on element: " + element.getText());
        } catch (Exception e) {
            logger.error("✗ Failed to JS click: " + e.getMessage());
            throw e;
        }
    }
}
