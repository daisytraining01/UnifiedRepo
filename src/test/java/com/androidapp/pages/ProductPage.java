package com.androidapp.pages;

import com.maveric.core.utils.mobile.MobileActions;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ProductPage extends MobileActions {
    public AppiumDriver<?> driver;
    WebDriverWait wait;

    By product1 = MobileBy.xpath("//android.widget.TextView[@text='Sauce Labs Backpack']");
    By addToCartBtn = MobileBy.xpath("//android.view.ViewGroup[@content-desc='test-ADD TO CART']");
    By productPrice = MobileBy.AccessibilityId("test-Price");
    By backToProductsBtn = MobileBy.AccessibilityId("test-BACK TO PRODUCTS");

    By removeBtn = MobileBy.AccessibilityId("test-REMOVE");
    By cartbtn = MobileBy.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/android.view.ViewGroup/android.widget.ImageView");
    By cartHeader = MobileBy.xpath("//android.widget.TextView[@text='YOUR CART']");
    By checkoutBtn = MobileBy.AccessibilityId("test-CHECKOUT");


    By checkoutHeader = MobileBy.xpath("//android.widget.TextView[@text='CHECKOUT: INFORMATION']");
    By firstName = MobileBy.AccessibilityId("test-First Name");
    By lastName = MobileBy.AccessibilityId("test-Last Name");
    By zipCode = MobileBy.AccessibilityId("test-Zip/Postal Code");
    By checkoutContinue = MobileBy.AccessibilityId("test-CONTINUE");

    By overviewHeader = MobileBy.xpath("//android.widget.TextView[@text='CHECKOUT: OVERVIEW']");
    By finishBtn = MobileBy.AccessibilityId("test-FINISH");

    By completeHeader = MobileBy.xpath("//android.widget.TextView[@text='CHECKOUT: COMPLETE!']");
    By completeMsg = MobileBy.AccessibilityId("THANK YOU FOR YOU ORDER");
    By backHomeBtn = MobileBy.AccessibilityId("test-BACK HOME");


    public ProductPage(AppiumDriver<?> driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void checkoutProduct(String firstName_, String lastName_, String zip_) {
        isDisplayed(product1);
//        click(product1);
//        wait(backToProductsBtn);
//        logScreenshot("PDP Page");
//        isDisplayed(productPrice);
        click(addToCartBtn);
        logScreenshot("add to cart");
        wait(removeBtn);
        click(cartbtn);
        wait(cartHeader);
        click(checkoutBtn);
        wait(checkoutHeader);
        enter(firstName, firstName_);
        enter(lastName, lastName_);
        enter(zipCode, zip_);
        logScreenshot("checkout address");
        click(checkoutContinue);
        wait(overviewHeader);
        logScreenshot("checkout overview");
        swipeToText(MatchStrategy.EXACT, "FINISH");
        click(finishBtn);
        wait(completeHeader);
        logScreenshot("checkout completed");
        click(backHomeBtn);

    }


    public void click(By element) {
        driver.findElement(element).click();
    }

    public void isDisplayed(By element) {
        Assert.assertTrue(driver.findElement(element).isDisplayed());
    }

    public void enter(By element, String data) {
        driver.findElement(element).sendKeys(data);
    }

    public void wait(By element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }
}
