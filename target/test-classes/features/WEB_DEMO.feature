@web
Feature: Demo Feature

  Background: login
    Given I am on the Login page "https://the-internet.herokuapp.com/login"

  Scenario: User Should Not be able to login with invalid credentials
    Then I enter username "boo" and password "foo"
    And I Assert the Message "Your username is invalid!"

  Scenario: User Should be able to login with valid credentials
    Then I enter username "tomsmith" and password "SuperSecretPassword!"
    And I Assert the Message "You logged into a secure area!"
    Then I logout of the application



