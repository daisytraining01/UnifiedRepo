package com.iosapp.pages;

import com.maveric.core.utils.web.WebActions;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ProductPage extends WebActions {
    public AppiumDriver<?> driver;
    WebDriverWait wait;
    By product1 = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND name == 'test-Item title' AND label == 'Sauce Labs Backpack'");
    By addToCartBtn = MobileBy.AccessibilityId("test-ADD TO CART");
    By productPrice = MobileBy.AccessibilityId("test-Price");
    By backToProductsBtn = MobileBy.AccessibilityId("test-BACK TO PRODUCTS");

    By removeBtn = MobileBy.AccessibilityId("test-REMOVE");
    By cartbtn = MobileBy.xpath("(//XCUIElementTypeOther[@name='1'])[4]");
    By cartHeader = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'YOUR CART'");
    By checkoutBtn = MobileBy.AccessibilityId("test-CHECKOUT");
    By productLabel = MobileBy.AccessibilityId("Sauce Labs Backpack");

    By checkoutHeader = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'CHECKOUT: INFORMATION'");
    By firstName = MobileBy.AccessibilityId("test-First Name");
    By lastName = MobileBy.AccessibilityId("test-Last Name");
    By zipCode = MobileBy.AccessibilityId("test-Zip/Postal Code");
    By checkoutContinue = MobileBy.AccessibilityId("test-CONTINUE");

    By overviewHeader = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'CHECKOUT: OVERVIEW'");
    By finishBtn = MobileBy.AccessibilityId("test-FINISH");

    By completeHeader = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'CHECKOUT: COMPLETE!'");
    By completeMsg = MobileBy.AccessibilityId("THANK YOU FOR YOU ORDER");
    By backHomeBtn = MobileBy.AccessibilityId("test-BACK HOME");


    public ProductPage(AppiumDriver<?> driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void checkoutProduct(String firstName_, String lastName_, String zip_) {
        isDisplayed(product1);
        click(product1);
        wait(backToProductsBtn);
        logScreenshot("PDP Page");
        isDisplayed(productPrice);
        isDisplayed(productLabel);
        click(addToCartBtn);
        logScreenshot("add to cart");
        wait(removeBtn);
        click(cartbtn);
        wait(cartHeader);
        isDisplayed(productLabel);
        click(checkoutBtn);
        wait(checkoutHeader);
        enter(firstName, firstName_);
        enter(lastName, lastName_);
        enter(zipCode, zip_);
        logScreenshot("checkout address");
        click(checkoutContinue);
        wait(overviewHeader);
        logScreenshot("checkout overview");
        click(finishBtn);
        wait(completeHeader);
        isDisplayed(completeMsg);
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
