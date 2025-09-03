# new feature
# Tags: optional
#
#@frontend
#Feature: Rush processing screen
#
#  Scenario: Verify the rush option appears on the Package Confirmation screen
#    Given User uploads a successful document from parent app
#    And Package information is displayed
#    Then Rush radio options are available
#    And Rush sidecard is not visible
#
#  Scenario: Verify the rush sidecard appears on the Package Confirmation screen
#    Given User uploads a successful document from parent app
#    And Package information is displayed
#    When Rush radio option Yes is selected
#    Then Rush sidecard is visible
#
#  Scenario: Proceed to Rush Processing from Package Confirmation screen
#    Given User uploads a successful document from parent app
#    And Package information is displayed
#    And Rush radio option Yes is selected
#    And Continue to payment button is enabled
#    When User clicks Continue
#    Then Rush modal is displayed
#    Then Rush model is closed
#    And Rush Details screen is displayed
#
