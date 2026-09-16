package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.*;
import testBases.BaseClass;
import utilities.RetryAnalyzer;

public class TC02_Login extends BaseClass {
    @Test(groups = {"Master","Sanity","Regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void verifyAccountLogin(){
        logger.info("***** Starting TC02 *****");
        try {
            logger.info("At Home Page...");
            homePage hp = new homePage(driver);
            hp.clickMyAccount();
            hp.clickLogin();
            loginPage lp = new loginPage(driver);
            lp.setEmail(p.getProperty("email"));
            lp.setPassword(p.getProperty("password"));
            lp.clickLogin();
            MyAccountPage ma = new MyAccountPage(driver);
            Assert.assertTrue(ma.isMyAccDisplayed());
            ma.clickLogout();

        }catch (Exception e){
            logger.error("Test Failed...");
            logger.debug(e);
            Assert.fail();
        }
        logger.info("***** Finished TC02 *****");
    }
}
