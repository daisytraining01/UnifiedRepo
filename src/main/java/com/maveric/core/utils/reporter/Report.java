package com.maveric.core.utils.reporter;

import com.maveric.core.cucumber.CucumberListener;
import org.testng.Reporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Report {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public static void log(String message) {
        if (CucumberListener.getScenario() != null) {
            CucumberListener.getScenario().log(getTimestamp() + " : " + message);
        } else {
            Reporter.log(getTimestamp() + " : " + message);
        }
    }

    public static String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
