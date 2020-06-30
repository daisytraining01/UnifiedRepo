package com.maveric.core.driver;


import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

public class Driver {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<AppiumDriver<?>> appiumDriver = new ThreadLocal<>();

    public static void setWebDriver(WebDriver driver_) {

        driver.set(driver_);
    }

    public static void setAppiumDriver(AppiumDriver<?> driver_) {
        appiumDriver.set(driver_);
    }

    public static WebDriver getWebDriver() {
        return driver.get();
    }

    public static AppiumDriver<?> getAppiumDriver() {
        return appiumDriver.get();
    }

    public static boolean isWebDriverEmpty() {

        return driver.get() == null;
    }

    public static boolean isAppiumDriverEmpty() {

        return appiumDriver.get() == null;
    }


    public static void quitDriver() {
        if (!isWebDriverEmpty()) {
            driver.get().quit();
        }
        if (!isAppiumDriverEmpty()) {
            appiumDriver.get().quit();
        }

    }
}
