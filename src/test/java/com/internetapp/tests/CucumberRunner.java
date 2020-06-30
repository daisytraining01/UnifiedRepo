package com.internetapp.tests;

import com.maveric.core.cucumber.CucumberBaseTest;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"./src/test/resources/features"},
        tags = "@web",
        glue = "com.internetapp"
)
public class CucumberRunner extends CucumberBaseTest {


}
