Feature: Access landing page and verify title

  Background:
    Given browser is initialized

  @frontend
  Scenario: Verify if the landing page is available
    Given user is navigated to the landing page
    Then page title is verified
    And browser is closed

