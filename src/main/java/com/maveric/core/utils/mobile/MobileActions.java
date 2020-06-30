package com.maveric.core.utils.mobile;

import com.google.common.collect.ImmutableList;
import com.maveric.core.config.ConfigProperties;
import com.maveric.core.driver.Driver;
import com.maveric.core.utils.web.WebActions;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class MobileActions extends WebActions {
    protected AppiumDriver<?> driver;
    private WebDriverWait wait;
    private int timeout = ConfigProperties.WAIT_TIMEOUT.getInt();
    private static final Duration SCROLL_DUR = Duration.ofMillis(1000);
    private static final double SCROLL_RATIO = 0.8;
    private static int ANDROID_SCROLL_DIVISOR = 3;
    private Dimension windowSize;

    private final static Logger logger = LogManager.getLogger();
    private final static AtomicInteger counter = new AtomicInteger();

    public MobileActions() {

        driver = Driver.getAppiumDriver();
        wait = new WebDriverWait(driver, timeout);
    }

    private Dimension getWindowSize() {
        if (windowSize == null) {
            windowSize = driver.manage().window().getSize();
        }
        return windowSize;
    }

    protected void swipe(Point start, Point end, Duration duration) {

        boolean isAndroid = driver instanceof AndroidDriver<?>;
        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        if (isAndroid) {
            duration = duration.dividedBy(ANDROID_SCROLL_DIVISOR);
        } else {
            swipe.addAction(new Pause(input, duration));
            duration = Duration.ZERO;
        }
        swipe.addAction(input.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(ImmutableList.of(swipe));
    }

    protected void swipe(double startXPct, double startYPct, double endXPct, double endYPct, Duration duration) {
        Dimension size = getWindowSize();
        Point start = new Point((int) (size.width * startXPct), (int) (size.height * startYPct));
        Point end = new Point((int) (size.width * endXPct), (int) (size.height * endYPct));
        swipe(start, end, duration);
    }

    protected void scroll(ScrollDirection dir, double distance) {
        if (distance < 0 || distance > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }
        Dimension size = getWindowSize();
        Point midPoint = new Point((int) (size.width * 0.5), (int) (size.height * 0.5));
        int top = midPoint.y - (int) ((size.height * distance) * 0.5);
        int bottom = midPoint.y + (int) ((size.height * distance) * 0.5);
        int left = midPoint.x - (int) ((size.width * distance) * 0.5);
        int right = midPoint.x + (int) ((size.width * distance) * 0.5);
        if (dir == ScrollDirection.UP) {
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);
        } else if (dir == ScrollDirection.DOWN) {
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);
        } else if (dir == ScrollDirection.LEFT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);
        } else {
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);
        }
    }

    protected void scroll(ScrollDirection dir) {
        scroll(dir, SCROLL_RATIO);
    }

    protected void scroll() {
        scroll(ScrollDirection.DOWN, SCROLL_RATIO);
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }


    public void swipeToText(MatchStrategy strategy, String text) {
        switch (strategy) {
            case EXACT:
                try {
                    driver.findElement(MobileBy
                            .AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                                    + ".scrollIntoView(new UiSelector().text(\"" + text + "\").instance(0))"));
                } catch (NoSuchElementException e) {
                    throw new RuntimeException("Swiping limit is reached. Text: " + text + " not found");
                }
                break;
            case CONTAINS:
                try {
                    driver.findElement(MobileBy
                            .AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                                    + ".scrollIntoView(new UiSelector().textContains(\"" + text + "\").instance(0))"));
                } catch (NoSuchElementException e) {
                    throw new RuntimeException("Swiping limit is reached. Part text: " + text + " not found");
                }
                break;
            default:
                throw new RuntimeException("Please use correct matching strategy. Available options: 'EXACT' or 'CONTAINS'.");
        }
    }

    public enum MatchStrategy {

        EXACT, CONTAINS
    }
}
