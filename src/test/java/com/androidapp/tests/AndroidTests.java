package com.androidapp.tests;

import com.androidapp.pages.LoginPage;
import com.androidapp.pages.ProductPage;
import com.maveric.core.driver.Driver;
import com.maveric.core.testng.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.Test;

public class AndroidTests extends BaseTest {

    private String user_name = "standard_user";
    private String password = "secret_sauce";

    @Test(enabled = false, groups = {"android"})
    public void loginWithValidCredentials() {
        AppiumDriver<?> driver = Driver.getAppiumDriver();
        LoginPage page = new LoginPage(driver);
        page.isOnLoginPage();
        page.login(user_name, password);
        page.isOnProductsPage();
        page.openMenu();
        page.logout();
        page.isOnLoginPage();
    }

    @Test(groups = {"android"})
    public void loginWithInvalidCredentials() {
        String user_name = "samUser";
        String password = "PasswordSam";
        AppiumDriver<?> driver = Driver.getAppiumDriver();
        LoginPage page = new LoginPage(driver);
        page.isOnLoginPage();
        page.login(user_name, password);
        page.verifyInvalidLogin();
    }

    @Test(groups = {"android"})
    public void checkoutProduct() {
        String firstName = "sai";
        String lastName = "kiran";
        String zipCode = "12345";
        AppiumDriver<?> driver = Driver.getAppiumDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.isOnLoginPage();
        loginPage.login(user_name, password);
        loginPage.isOnProductsPage();
        ProductPage prdPage = new ProductPage(driver);
        prdPage.checkoutProduct(firstName, lastName, zipCode);
        loginPage.isOnProductsPage();
        loginPage.openMenu();
        loginPage.logout();
        loginPage.isOnLoginPage();
    }
}
