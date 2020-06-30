package com.iosapp.pages;

import com.maveric.core.utils.web.WebActions;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class LoginPage extends WebActions {

    public AppiumDriver<?> driver;
    WebDriverWait wait;
    By username_txt = MobileBy.AccessibilityId("test-Username");
    By password_txt = MobileBy.AccessibilityId("test-Password");
    By loginBtn = MobileBy.AccessibilityId("test-LOGIN");
    By productsHeader = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'PRODUCTS'");
    By menu = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeOther' AND name == 'test-Menu'");
    By menu_header = MobileBy.AccessibilityId("test-ALL ITEMS");
    By logoutBtn = MobileBy.AccessibilityId("test-LOGOUT");
    By login_page = MobileBy.AccessibilityId("Username Password LOGIN");
    By invalidLoginMsg = MobileBy.AccessibilityId("Username and password do not match any user in this service.");


    public LoginPage(AppiumDriver<?> driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void login(String user_name, String password) {

        driver.findElement(username_txt).sendKeys(user_name);
        driver.findElement(password_txt).sendKeys(password);
        logScreenshot("login");
        driver.findElement(loginBtn).click();

    }

    public void isOnProductsPage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(productsHeader));
        Assert.assertTrue(driver.findElement(productsHeader).isDisplayed());
        logScreenshot("login successful");
    }

    public void isOnLoginPage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(login_page));
        Assert.assertTrue(driver.findElement(username_txt).isDisplayed());
        Assert.assertTrue(driver.findElement(password_txt).isDisplayed());

    }

    public void openMenu() {
        driver.findElement(menu).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(menu_header));
    }

    public void logout() {
        driver.findElement(logoutBtn).click();
        logScreenshot("successfully logged out");
    }

    public void verifyInvalidLogin() {
        wait.until(ExpectedConditions.presenceOfElementLocated(invalidLoginMsg));
        logScreenshot("invalid login");
    }


}