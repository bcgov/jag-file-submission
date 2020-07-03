Feature: As a user I would like to have my account verified

  Background:
    Given user is on the landing page

  @frontend
  Scenario Outline: Verify valid CSO account guid redirects to eFiling form page
    When  user enters a valid existing CSO account guid "<validExistingCSOGuid>"
    Then eFiling frontend page is displayed and cancel button exists
    Examples:
      | validExistingCSOGuid |
      | validExistingCSOGuid |
      | validExistingCSOGuid |

  @frontend
  Scenario Outline: Verify non existing CSO account guid redirects to the eFiling form page
    When  user enters non existing CSO account guid "<nonExistingCSOGuid>"
    Then eFiling frontend page is displayed and cancel button exists
    Examples:
      | nonExistingCSOGuid |
      | nonExistingCSOGuid |

  @frontend
  Scenario Outline: Verify invalid CSO account guid without eFiling role returns error message
    When  user enters invalid CSO account guid without eFiling role "<invalidNoRoleGuid>"
    Then error message is displayed
    Examples:
      | invalidNoRoleGuid |
      | invalidNoRoleGuid |
