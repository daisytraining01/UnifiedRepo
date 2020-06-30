package com.internetapp.pages;

import com.maveric.core.config.ConfigProperties;
import com.maveric.core.driver.Driver;
import com.maveric.core.utils.web.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import static com.maveric.core.utils.reporter.Report.log;

public class CITIAccountSummaryPage extends WebActions {

    public CITIAccountSummaryPage() {
    	
    }



    public CITIAccountSummaryPage assertAccountSummary() {

        return this;

    }

}
