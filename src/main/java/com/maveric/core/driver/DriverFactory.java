package com.maveric.core.driver;

import com.maveric.core.config.ConfigProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class DriverFactory {

    public static final Logger logger = LogManager.getLogger();
    public static final String OS = System.getProperty("os.name").toLowerCase();

    public enum Browser {
        firefox, chrome, edge, iexplorer, safari
    }

    public enum RemoteGrid {
        sauce, browserstack, grid
    }

    public enum Platform {
        windows, macos, ios, android, linux
    }

    public void driverSetup() {
        Browser browser = getBrowser();

        if (isRemote()) {
            createRemoteDriver();
        } else {
            Driver.setWebDriver(createDriver(browser));
        }

    }

    public WebDriver createDriver(Browser browser) {
        switch (browser) {
            case firefox:
                return getFirefoxDriver();
            case chrome:
                return getChromeDriver();
            case edge:
                return getEdgeDriver();
            case iexplorer:
                return getIEDriver();
            case safari:
                return getSafariDriver();
            default:
                throw new RuntimeException("Invalid Browser");
        }
    }

    private WebDriver getSafariDriver() {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setCapability("safari.cleanSession", true);
        return new SafariDriver(safariOptions);
    }

    private WebDriver getIEDriver() {
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        ieOptions.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
        ieOptions.setCapability("requireWindowFocus", true);
        ieOptions.takeFullPageScreenshot();
        ieOptions.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
        ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, false);
        ieOptions.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
        ieOptions.setCapability("ignoreProtectedModeSettings", true);

        return new InternetExplorerDriver(ieOptions);

    }

    private WebDriver getEdgeDriver() {
        EdgeOptions edgeOptions = new EdgeOptions();
        return new EdgeDriver(edgeOptions);
    }

    private WebDriver getChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(ConfigProperties.HEADLESS.getBoolean());
        if (OS.contains("nix") || OS.contains("nux")
                || OS.contains("aix")) {
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--no-sandbox");
        }

        if (!ConfigProperties.CHROME_DEVICE.get().isEmpty()) {
            chromeOptions.setExperimentalOption(
                    "mobileEmulation", new HashMap<String, Object>() {{
                        put("deviceName", ConfigProperties.CHROME_DEVICE.get());
                    }});
        }
        return new ChromeDriver(chromeOptions);
    }

    private WebDriver getFirefoxDriver() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(ConfigProperties.HEADLESS.getBoolean());
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);
        return new FirefoxDriver(firefoxOptions);
    }

    public void createRemoteDriver() {

        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            RemoteCapabilities remoteCaps = new RemoteCapabilities(getPlatform(), getBrowser());
            URL remoteURL;
            switch (getRemoteType()) {
                case browserstack:
                    caps.merge(remoteCaps.getBrowserstackCapabilities());
                    remoteURL = getBrowserStackURL();
                    break;
                case sauce:
                    caps.merge(remoteCaps.getSauceCapabilities());
                    remoteURL = getSauceURL();
                    logger.info("sauce");
                    break;
                case grid:
                    caps.merge(remoteCaps.getGridCapabilities());
                    remoteURL = getGridURL();
                    break;
                default:
                    throw new RuntimeException("Invalid Remote Type");
            }

            caps.asMap().forEach((key, value) -> logger.info(key + " : " + value));
            boolean isNative = false;
            Object appCap = caps.getCapability("app");
            if (appCap != null && !(appCap.toString().isEmpty())) {
                isNative = true;
            }

            switch (getPlatform()) {
                case ios:
                    if (isNative)
                        Driver.setAppiumDriver(new IOSDriver<>(remoteURL, caps));
                    else
                        Driver.setWebDriver(new RemoteWebDriver(remoteURL, caps));
                    break;
                case android:
                    if (isNative)
                        Driver.setAppiumDriver(new AndroidDriver<>(remoteURL, caps));
                    else
                        Driver.setWebDriver(new RemoteWebDriver(remoteURL, caps));
                    break;
                case macos:
                case windows:
                case linux:
                    Driver.setWebDriver(new RemoteWebDriver(remoteURL, caps));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RemoteGrid getRemoteType() {
        if (ConfigProperties.BROWSER_STACK.getBoolean()) {
            return RemoteGrid.browserstack;
        } else if (ConfigProperties.SAUCE.getBoolean()) {
            return RemoteGrid.sauce;
        } else {
            return RemoteGrid.grid;
        }
    }

    public boolean isRemote() {
        return (!ConfigProperties.GRID_URL.get().isEmpty())
                || ConfigProperties.SAUCE.getBoolean()
                || ConfigProperties.BROWSER_STACK.getBoolean();
    }


    private static URL getGridURL() {
        try {
            return new URL(ConfigProperties.GRID_URL.get());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL getBrowserStackURL() {
        try {
            String url = String.format("https://%s:%s@%s",
                    ConfigProperties.BROWSER_STACK_USERNAME.get(),
                    ConfigProperties.BROWSER_STACK_ACCESS_KEY.get(),
                    ConfigProperties.BROWSER_STACK_HUB.get());
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL getSauceURL() {
        try {
            String url = String.format(
                    "https://%s:%s@%s",
                    ConfigProperties.SAUCE_USERNAME.get(),
                    ConfigProperties.SAUCE_ACCESS_KEY.get(),
                    ConfigProperties.SAUCE_HUB.get());

            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Platform getPlatform() {
        if (!ConfigProperties.PLATFORM.get().isEmpty()) {
            return Platform.valueOf(ConfigProperties.PLATFORM.get());
        } else {
            throw new RuntimeException("Invalid platform Type");
        }
    }

    public Browser getBrowser() {

        return Browser.valueOf(ConfigProperties.BROWSER.get());

    }


    public static void downloadDriver() {
        String browser_ = Browser.valueOf(ConfigProperties.BROWSER.get()).toString();
        if (!(browser_.equalsIgnoreCase("safari"))) {
            WebDriverManager.getInstance(DriverManagerType.valueOf(browser_.toUpperCase())).setup();
        }
    }

}
