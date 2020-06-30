package com.internetapp.stepDefs;

import com.internetapp.pages.LoginPage;
import io.cucumber.java8.En;

public class StepDefs implements En {

    LoginPage login = new LoginPage();

    public StepDefs() {

        Given("I am on the Login page {string}", (String url) -> {
            login.navigate(url);

        });
        Then("I enter username {string} and password {string}", (String username, String password) -> {
            login.login(username, password);


        });

        And("I Assert the Message {string}", (String error) -> {
            login.assertMessage(error);
        });

        Then("I logout of the application", () -> {
            login.logout();


        });

    }

}
