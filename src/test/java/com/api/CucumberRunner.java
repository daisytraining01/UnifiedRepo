package com.api;

import com.maveric.core.cucumber.CucumberBaseTest;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"./src/test/resources/features"},
        tags = "@api",
        glue = "com.api"
)
public class CucumberRunner extends CucumberBaseTest {

}
