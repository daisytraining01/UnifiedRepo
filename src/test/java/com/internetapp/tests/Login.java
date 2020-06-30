package com.internetapp.tests;

import com.internetapp.pages.LoginPage;
import com.maveric.core.testng.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Login extends BaseTest {

    @Test(groups = {"web"})
    public void verifyLoginWithInvalidCredentials() {

        new LoginPage()
                .navigate("https://the-internet.herokuapp.com/login")
                .login("boo", "foo")
                .assertMessage("Your username is invalid!");
        //Assert.fail();
    }


    @Test(groups = {"web"})
    public void verifyLoginWithValidCredentials() {
        new LoginPage()
                .navigate("https://the-internet.herokuapp.com/login")
                .login("tomsmith", "SuperSecretPassword!")
                .assertMessage("You logged into a secure area!")
                .logout();

    }
}

