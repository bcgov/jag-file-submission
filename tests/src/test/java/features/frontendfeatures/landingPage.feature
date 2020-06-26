Feature: User can navigate to the efiling landing page

  Background:
    Given user is on the landing page

  @frontend
  Scenario: Verify valid account guid redirects efiling form page
    When  user enters a valid account guid "enter valid guid"
    Then eFiling frontend page is displayed and cancel button exists
  @frontend
  Scenario: Verify non existing account guid redirects to the efiling form page
    When  user enters non existing account guid "123"
    Then eFiling frontend page is displayed and cancel button exists
  @frontend
  Scenario: Verify existing account guid with conflicts returns error message
    When  user enters invalid account guid "enter invalid guid"
    Then error message is displayed
