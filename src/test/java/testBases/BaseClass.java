package testBases;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;


public class BaseClass {
    public static WebDriver driver;
    public Logger logger;
    public Properties p;
    @BeforeClass(groups = {"Master","Sanity","Regression"})
    @Parameters({"os","browser"})
    public void setup(String os, String br) throws IOException {
        FileReader fr = new FileReader("./src//test//resources//config.properties");
        p = new Properties();
        p.load(fr);

        logger = (Logger) LogManager.getLogger(this.getClass());

        switch(br.toLowerCase()) {
            case "chrome": driver = new ChromeDriver();break;
            case "edge": driver = new EdgeDriver();break;
            default: logger.error("Wrong Browser Parameter..."); return;
        }
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(p.getProperty("appurl"));
        driver.manage().window().maximize();
    }
    @AfterClass(groups = {"Master","Sanity","Regression"})
    public void teardown(){
        driver.quit();
    }
    public String randomAlpha(){
        String randStr = RandomStringUtils.randomAlphabetic(5);
        return randStr;
    }
    public String randomPhno(){
        String randStr = RandomStringUtils.randomNumeric(10);
        return randStr;
    }

    public String randomAlphaNum(){
        String randStr = RandomStringUtils.randomAlphabetic(5);
        String randNum = RandomStringUtils.randomNumeric(3);

        return (randStr+"$"+randNum);
    }

    //Screenshot
    public String captureScreen(String tname) throws IOException {

        String timeStamp =
                new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile =
                takesScreenshot.getScreenshotAs(OutputType.FILE);

        String targetFilePath =
                System.getProperty("user.dir")
                        + "\\screenshots\\"
                        + tname
                        + "_"
                        + timeStamp
                        + ".png";

        File targetFile = new File(targetFilePath);

        FileUtils.copyFile(sourceFile, targetFile);

        return targetFilePath;
    }
}
