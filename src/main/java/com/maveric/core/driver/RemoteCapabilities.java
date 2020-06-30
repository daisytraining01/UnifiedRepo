package com.maveric.core.driver;

import com.google.common.collect.ImmutableMap;
import com.maveric.core.config.ConfigProperties;
import com.maveric.core.testng.listeners.DriverListener;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

import static com.maveric.core.driver.BrowserStackClient.isURL;

public class RemoteCapabilities {

    public static final Logger logger = LogManager.getLogger();
    DriverFactory.Platform platform;
    DriverFactory.Browser browser;
    private String browser_version = ConfigProperties.BROWSER_VERSION.get();
    private String platform_version = ConfigProperties.PLATFORM_VERSION.get();
    private boolean bs_local = ConfigProperties.BROWSER_STACK_LOCAL.getBoolean();
    private String app_name = ConfigProperties.APPLICATION_NAME.get();
    private String build_name = ConfigProperties.BUILD_NAME.get();
    private String device_name = ConfigProperties.DEVICE_NAME.get();
    private String app = ConfigProperties.APP_PATH.get();
    private String android_activity = ConfigProperties.ANDROID_ACTIVITY.get();
    private String android_package = ConfigProperties.ANDROID_PACKAGE.get();
    private String ios_bundleId = ConfigProperties.IOS_BUNDLE_ID.get();
    private String selenium_verison = ConfigProperties.SELENIUM_VERSION.get();
    private String appium_verison = ConfigProperties.APPIUM_VERSION.get();
    private boolean useNewWDA = ConfigProperties.APPIUM_NEW_WDA.getBoolean();
    private String udid = ConfigProperties.IOS_UDID.get();
    private String xcodeSigningId = ConfigProperties.XCODE_SIGNING_ID.get();
    private String xcodeOrgId = ConfigProperties.XCODE_ORG_ID.get();
    private int deviceTimeout = ConfigProperties.DEVICE_READY_TIMEOUT.getInt();
    private boolean app_noReset = ConfigProperties.APP_NO_RESET.getBoolean();
    private boolean unicode_keyboard = ConfigProperties.UNICODE_KEYBOARD.getBoolean();
    private boolean reset_keyboard = ConfigProperties.RESET_KEYBOARD.getBoolean();

    private String testname = DriverListener.tests.get();

    public RemoteCapabilities(DriverFactory.Platform platform, DriverFactory.Browser browser) {
        this.platform = platform;
        this.browser = browser;
    }

    public DesiredCapabilities getSauceCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("name", testname);
        caps.setCapability("build", build_name);
        switch (platform) {
            case windows:
            case macos:
            case linux:
                caps.merge(getSauceDesktopCapabilities());
                break;
            case android:
            case ios:
                caps.merge(getSauceMobileCapabilities());
                break;
        }

        return caps;
    }

    public DesiredCapabilities getBrowserstackCapabilities() {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("project", app_name);
        caps.setCapability("build", build_name);
        caps.setCapability("name", testname);
        if (platform.name().equalsIgnoreCase("macos")) {
            caps.setCapability("os", "OS X");
        } else {
            caps.setCapability("os", platform);
        }

        if (!platform_version.isEmpty()) {
            caps.setCapability("os_version", platform_version);
        }

        if (!browser_version.isEmpty()) {
            caps.setCapability("browser_version", browser_version);
        }
        if (bs_local) {
            caps.setCapability("browserstack.local", bs_local);
        }
        switch (platform) {
            case windows:
            case macos:
            case linux:
                caps.setCapability("browserstack.selenium_version", selenium_verison);
                break;
            case android:
            case ios:
                caps.merge(getBSMobileCapabilities());
                break;
        }

        return new DesiredCapabilities().merge(caps);
    }

    private DesiredCapabilities getBSMobileCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        caps.setCapability("unicodeKeyboard", unicode_keyboard);
        caps.setCapability("resetKeyboard", reset_keyboard);
        caps.setCapability("deviceReadyTimeout", deviceTimeout + 30);
        if (device_name.isEmpty()) {
            throw new RuntimeException("Device Name is required");
        }
        if (!app.isEmpty()) {
            caps.setCapability("app", BrowserStackClient.app_id);
            caps.setCapability("browser", "");

        }
        caps.setCapability("device", device_name);
        caps.setCapability("real_mobile", "true");
        caps.setCapability("browserstack.appium_version", appium_verison);

        switch (platform) {
            case android:
                if (!android_activity.isEmpty()) {
                    caps.setCapability("appActivity", android_activity);
                    caps.setCapability("browser", "");
                }
                if (!android_package.isEmpty()) {
                    caps.setCapability("appPackage", android_package);
                    caps.setCapability("browser", "");
                }
                break;
            case ios:
                if (!ios_bundleId.isEmpty()) {
                    caps.setCapability("bundleId", ios_bundleId);
                    caps.setCapability("browser", "");

                }
                break;
        }
        return caps;
    }


    public MutableCapabilities getSauceDesktopCapabilities() {
        if (platform_version.isEmpty()) {
            if (!browser.name().equalsIgnoreCase("linux")) {
                throw new RuntimeException("platform version is required");
            }
        }
        MutableCapabilities caps = new MutableCapabilities();
        String browser_ver = browser_version;
        if (browser_ver.isEmpty()) {
            browser_ver = "latest";
        }
        if (platform.name().equalsIgnoreCase("macos")) {
            HashMap<String, String> mac_versions = new HashMap<>();
            mac_versions.put("catalina", "10.15");
            mac_versions.put("mojave", "10.14");
            mac_versions.put("high sierra", "10.13");
            mac_versions.put("sierra", "10.12");
            mac_versions.put("el capitan", "10.11");
            mac_versions.put("el yosemite", "10.10");
            platform_version = Optional.of(mac_versions.get(platform_version.toLowerCase())).orElse(platform_version);

        }

        String platformName = "";
        if (platform.name().equalsIgnoreCase("linux")) {
            platformName = platform.name();
        } else {
            platformName = platform + " " + platform_version;
        }

        switch (browser) {
            case iexplorer:
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.setCapability("platformName", platformName);
                ieOptions.setCapability("browserVersion", browser_ver);
                ieOptions.setCapability("sauce:options", caps);
                return ieOptions;
            case edge:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability("platformName", platformName);
                edgeOptions.setCapability("browserVersion", browser_ver);
                edgeOptions.setCapability("sauce:options", caps);
                return edgeOptions;
            case chrome:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("w3c", true);
                chromeOptions.setCapability("platformName", platformName);
                chromeOptions.setCapability("browserVersion", browser_ver);
                chromeOptions.setCapability("sauce:options", caps);
                return chromeOptions;
            case safari:
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setCapability("platformName", platformName);
                safariOptions.setCapability("browserVersion", browser_ver);
                safariOptions.setCapability("sauce:options", caps);
                return safariOptions;
            case firefox:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("platformName", platformName);
                firefoxOptions.setCapability("browserVersion", browser_ver);
                firefoxOptions.setCapability("sauce:options", caps);
                return firefoxOptions;
        }
        return caps;
    }

    public DesiredCapabilities getGridCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", browser.name());
        if (!browser_version.isEmpty()) {
            caps.setCapability("browserVersion", browser_version);
        }
        caps.setCapability("platformName", platform);
        if (!platform_version.isEmpty()) {
            caps.setCapability("platformVersion", platform_version);
        } else {
            logger.error("platform version not provided");
        }

        switch (platform) {
            case android:
            case ios:
                caps.merge(getGridMobileCapabilities());
                break;
        }

        return caps;
    }

    public DesiredCapabilities getGridMobileCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("deviceName", device_name);
        caps.setCapability("useNewWDA", useNewWDA);
        caps.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        caps.setCapability("unicodeKeyboard", unicode_keyboard);
        caps.setCapability("resetKeyboard", reset_keyboard);
        if (browser.name().equalsIgnoreCase("chrome")) {
            String chromeDirpath = new File(WebDriverManager.chromedriver().getBinaryPath()).getParent();
            caps.setCapability("chromedriverExecutableDir", chromeDirpath);
            logger.info("chromedriver path " + chromeDirpath);
        }
        caps.setCapability("deviceReadyTimeout", deviceTimeout + 30);
        if (!app.isEmpty()) {
            caps.setCapability("app", new File(app).getAbsolutePath());
            caps.setCapability("browserName", "");
            caps.setCapability("noReset", app_noReset);
        }
        switch (platform) {
            case android:
                caps.setCapability("automationName", "UiAutomator2");
                if (!android_package.isEmpty()) {
                    caps.setCapability("appPackage", android_package);
                    caps.setCapability("browserName", "");
                }
                if (!android_activity.isEmpty()) {
                    caps.setCapability("appActivity", android_activity);
                    caps.setCapability("browserName", "");
                }
                break;
            case ios:
                caps.setCapability("automationName", "XCUITest");
                if (!ios_bundleId.isEmpty()) {
                    caps.setCapability("bundleId", ios_bundleId);
                    caps.setCapability("browserName", browser);
                }
                if (!udid.isEmpty()) {
                    caps.setCapability("udid", udid);
                }
                if ((!xcodeOrgId.isEmpty()) && (!xcodeSigningId.isEmpty())) {
                    caps.setCapability("xcodeOrgId", xcodeOrgId);
                    caps.setCapability("xcodeSigningId", xcodeSigningId);
                }
                break;
        }

        return caps;
    }

    public DesiredCapabilities getSauceMobileCapabilities() {
        if (device_name.isEmpty()) {
            throw new RuntimeException("Device Name is required");
        }

        DesiredCapabilities caps;
        switch (platform) {
            case ios:
                caps = DesiredCapabilities.iphone();
                if (!device_name.toLowerCase().contains("simulator")) {
                    device_name = device_name + " Simulator";
                }
                break;
            case android:
                caps = DesiredCapabilities.android();
                if (!device_name.toLowerCase().contains("emulator")) {
                    device_name = device_name + " Emulator";
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + platform);
        }
        caps.setCapability("appiumVersion", appium_verison);
        caps.setCapability("deviceName", device_name);
        caps.setCapability("platformVersion", platform_version);
        caps.setCapability("platformName", platform);
        caps.setCapability("browserName", browser);
        caps.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        caps.setCapability("unicodeKeyboard", unicode_keyboard);
        caps.setCapability("resetKeyboard", reset_keyboard);
        caps.setCapability("deviceReadyTimeout", deviceTimeout + 60);

        if (!app.isEmpty()) {
            if (isURL(app)) {
                caps.setCapability("app", "sauce-storage:" + app);
            } else {
                caps.setCapability("app", "sauce-storage:" + new File(app).getName());
            }
            caps.setCapability("browserName", "");
        }
        if (!android_package.isEmpty()) {
            caps.setCapability("appPackage", android_package);
            caps.setCapability("browserName", "");
        }
        if (!android_activity.isEmpty()) {
            caps.setCapability("appActivity", android_activity);
            caps.setCapability("browserName", "");
        }
        if (!ios_bundleId.isEmpty()) {
            caps.setCapability("bundleId", ios_bundleId);
        }
        return caps;
    }


}
