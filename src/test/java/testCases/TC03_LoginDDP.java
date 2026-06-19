package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.MyAccountPage;
import pageObjects.homePage;
import pageObjects.loginPage;
import testBases.BaseClass;
import utilities.DataProviders;

public class TC03_LoginDDP extends BaseClass {
    @Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class, groups = {"Master"})
    public void verifyLoginDDP(String uname, String pass, String tcType){
        logger.info("***** Starting TC02 *****");
        try {
            logger.info("At Home Page...");
            homePage hp = new homePage(driver);
            hp.clickMyAccount();
            hp.clickLogin();
            loginPage lp = new loginPage(driver);

            lp.setEmail(uname);
            lp.setPassword(pass);
            lp.clickLogin();
            MyAccountPage ma = new MyAccountPage(driver);
            Boolean isAccountDisp = ma.isMyAccDisplayed();

            if(tcType.equalsIgnoreCase("Valid"))
            {
                Assert.assertTrue(isAccountDisp,
                        "Valid credentials failed");
                ma.clickLogout();
            }
            else
            {
                Assert.assertFalse(isAccountDisp,
                        "Invalid credentials logged in successfully");
            }

        }
        catch (Exception e){
            logger.error("Test Failed..", e);
            Assert.fail(e.getMessage());
        }
        finally {
            logger.info("***** Finished TC02 *****");
        }
    }
}
