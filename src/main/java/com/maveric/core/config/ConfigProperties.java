package com.maveric.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

public enum ConfigProperties {
    APPLICATION_NAME("Test Application"),
    BUILD_NAME("TestNG Suite"),
    LISTENERS(""),
    INCLUDE_GROUPS(""),
    EXCLUDE_GROUPS(""),
    MAX_RETRY_COUNT("0"),
    WAIT_TIMEOUT("10"),
    THREAD_COUNT("1"),
    DATAPROVIDER_COUNT("1"),
    BROWSER("chrome"),
    BROWSER_VERSION(""),
    PLATFORM(""),
    PLATFORM_VERSION(""),
    CHROME_DEVICE(""),
    HEADLESS("false"),
    GRID_URL(""),
    BROWSER_STACK("false"),
    SAUCE("false"),
    BROWSER_STACK_ACCESS_KEY(""),
    BROWSER_STACK_LOCAL("false"),
    BROWSER_STACK_USERNAME(""),
    BROWSER_STACK_HUB("hub-cloud.browserstack.com/wd/hub"),
    SAUCE_HUB("ondemand.saucelabs.com/wd/hub"),
    SAUCE_ACCESS_KEY(""),
    SAUCE_USERNAME(""),
    SAUCE_OVERWRITE_FILE("true"),
    DEVICE_NAME(""),
    APP_PATH(""),
    APP_NAME("MyAPP"),
    ANDROID_ACTIVITY(""),
    ANDROID_PACKAGE(""),
    IOS_BUNDLE_ID(""),
    APPIUM_VERSION("1.17.0"),
    SELENIUM_VERSION("3.141.59"),
    APPIUM_NEW_WDA("false"),
    IOS_UDID(""),
    XCODE_SIGNING_ID(""),
    XCODE_ORG_ID(""),
    DEVICE_READY_TIMEOUT("30"),
    APP_NO_RESET("true"),
    RESET_KEYBOARD("true"),
    UNICODE_KEYBOARD("true");


    private static final Properties PROPERTIES;
    private static final Logger logger = LogManager.getLogger();

    static {
        PROPERTIES = new Properties();
        String configFile = ofNullable(getProperty("CONFIG_FILE")).orElse("./src/test/resources/config/config.properties");
        try (FileInputStream inputStream = new FileInputStream(configFile)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private final String defaultValue;

    ConfigProperties(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String get() {
        String value = ofNullable(System.getProperty(name()))
                .orElse(PROPERTIES.getProperty(name(), defaultValue));
        if (value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(get());
    }

    public int getInt() {
        return Integer.parseInt(get());
    }
}
