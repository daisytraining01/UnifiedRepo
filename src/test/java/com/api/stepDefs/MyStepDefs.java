package com.api.stepDefs;

import com.api.service.Reqres;
import io.cucumber.java8.En;

public class MyStepDefs implements En {

    Reqres reqres = new Reqres();

    public MyStepDefs() {

        Then("I create user", () -> {

            reqres.createUserTest();

        });
        And("I get User Details", () -> {

            reqres.getUserDetailsTest();
        });
        Then("I update User Details", () -> {
            reqres.updateUserDetailsTest();
        });
        Then("I Delete User", () -> {

            reqres.deleteUserTest();

        });
    }

}
