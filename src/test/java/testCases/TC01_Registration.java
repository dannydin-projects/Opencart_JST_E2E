package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.AccountRegisterpage;
import pageObjects.homePage;
import testBases.BaseClass;

import java.sql.SQLOutput;

public class TC01_Registration extends BaseClass {
    @Test(groups = {"Master","Sanity","Regression"})
    public void verifyAccountregistration(){
        logger.info("***** Starting TC01 *****");
        try {
            logger.info("At Home Page...");
            homePage hp = new homePage(driver);
            hp.clickMyAccount();
            hp.clickRegister();
            logger.info("Starting Registration process...");
            AccountRegisterpage reg = new AccountRegisterpage(driver);
            reg.setFirstName(randomAlpha().toUpperCase());
            reg.setLastName(randomAlpha().toUpperCase());
            reg.setEmail(randomAlpha() + "@gmail.com");
            reg.setTelephone(randomPhno());
            String pwd = randomAlphaNum();
            reg.setPassword(pwd);
            reg.setConfirmPassword(pwd);
            reg.setPrivacyPolicy();
            reg.clickContinue();
            logger.info("Validating message...");
            String cnfMsg = reg.getConfirmationMsg();
            logger.info(cnfMsg);
            Assert.assertEquals(cnfMsg, "Your Account Has Been Created!");
        }
        catch(Exception e){
            logger.error("Test Failed...");
            logger.debug(e);
            Assert.fail();
        }
        logger.info("***** Finished Execution TC01 *****");
    }
}
