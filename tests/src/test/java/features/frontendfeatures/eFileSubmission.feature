Feature: As a user my account should be validated and provided with an option to either cancel or proceed with E-File submission

  Background:
    Given user is on the landing page

  @frontend
  Scenario Outline: Verify user with valid CSO account guid can cancel a E-File submission
    When user enters a valid existing CSO account guid "<validExistingCSOGuid>"
    Then eFile submission page is displayed and user clicks the cancel button
    Then user confirms the cancellation in the confirmation window
    And  user is navigated to the cancel page
    Then user clicks Return Home button to navigate to the landing page

    Examples:
      | validExistingCSOGuid |
      | validExistingCSOGuid |

  @frontend
  Scenario Outline: Verify user with valid CSO account guid is able to proceed with E-File submission if cancel button is clicked
    When user enters a valid existing CSO account guid "<validExistingCSOGuid>"
    Then eFile submission page is displayed and user clicks the cancel button
    Then user clicks resume E-File submission in the confirmation window
    And the user stays on the E-File submission page

    Examples:
      | validExistingCSOGuid |
      | validExistingCSOGuid |

  @frontend
  Scenario Outline: Verify user with non existing CSO account guid can cancel CSO account creation after accepting the user agreement
    When user enters non existing CSO account guid "<nonExistingCSOGuid>"
    Then eFile submission page with user agreement is displayed
    And user accepts agreement and clicks cancel button
    Then user confirms the cancellation in the confirmation window
    And  user is navigated to the cancel page
    Then user clicks Return Home button to navigate to the landing page

    Examples:
      | nonExistingCSOGuid |
      | nonExistingCSOGuid |

  @frontend
  Scenario Outline: Verify user with non existing CSO account guid is able to proceed with E-File submission if cancel button is clicked
    When user enters non existing CSO account guid "<nonExistingCSOGuid>"
    Then eFile submission page with user agreement is displayed
    And user accepts agreement and clicks cancel button
    Then user clicks resume E-File submission in the confirmation window
    And the user stays on the E-File submission page

    Examples:
      | nonExistingCSOGuid |
      | nonExistingCSOGuid |

  @frontend
  Scenario Outline: Verify invalid CSO account guid without eFiling role returns error message
    When  user enters invalid CSO account guid without eFiling role "<invalidNoFilingRoleGuid>"
    Then error message is displayed

    Examples:
      | invalidNoFilingRoleGuid |
      | invalidNoFilingRoleGuid |

  @frontend
  Scenario Outline: Verify there are no broken links in the page
    When  user enters a valid existing CSO account guid "<validExistingCSOGuid>"
    Then eFile submission page is displayed and user clicks the cancel button
    Then user clicks resume E-File submission in the confirmation window
    And verify there are no broken links in the page

    Examples:
      | validExistingCSOGuid |
      | validExistingCSOGuid |
