package com.internetapp.pages;

import com.maveric.core.config.ConfigProperties;
import com.maveric.core.driver.Driver;
import com.maveric.core.utils.web.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import static com.maveric.core.utils.reporter.Report.log;

public class CITILoginPage extends WebActions {

    private final By btn_login = By.tagName("button");
    private final By txt_username = By.id("username");
    private final By txt_password = By.id("password");
    private final By message = By.id("flash");
    private final By btn_logout = By.cssSelector("i.icon-signout");
    WebDriverWait wait;
    WebDriver driver;

    public CITILoginPage() {
        driver = Driver.getWebDriver();
        wait = new WebDriverWait(driver, ConfigProperties.WAIT_TIMEOUT.getInt());
    }

    public CITILoginPage navigate(String url) {
        driver.navigate().to(url);

        logScreenshot("login");
        ;
        log("sample log");

        return this;

    }

    public CITILoginPage login(String usr, String pwd) {
        wait.until(ExpectedConditions.presenceOfElementLocated(txt_username))
                .sendKeys(usr);
        driver.findElement(txt_password).sendKeys(pwd);
        driver.findElement(btn_login).click();
        logScreenshot("login successful");
        return this;

    }

    public CITILoginPage assertMessage(String errMsg) {
        String errText = wait.until(ExpectedConditions.presenceOfElementLocated(message)).getText();
        Assert.assertTrue(errText.contains(errMsg));
        logScreenshot("assert");

        return this;

    }

    public CITILoginPage logout() {
        wait.until(ExpectedConditions.presenceOfElementLocated(btn_logout));
        driver.findElement(btn_logout).click();
        logScreenshot("logout");

        return this;
    }
}
