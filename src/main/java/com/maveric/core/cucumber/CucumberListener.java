package com.maveric.core.cucumber;

import com.maveric.core.driver.Driver;
import com.maveric.core.utils.web.WebActions;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberListener {
    public static final Logger logger = LogManager.getLogger();
    private static ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    public static Scenario getScenario() {
        return scenario.get();
    }

    public static void setScenario(Scenario scenario_) {
        scenario.set(scenario_);
    }

    @Before(order = 1)
    public void before(Scenario scenario) {
        CucumberListener.setScenario(scenario);
    }

    @After(order = 1)
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            takeFailureScreenshot();
        }
    }

    private void takeFailureScreenshot() {
        if (!Driver.isWebDriverEmpty() || !Driver.isAppiumDriverEmpty()) {
            WebActions actions = new WebActions();
            actions.logScreenshot("Failure Screenshot");
        }
    }

}
