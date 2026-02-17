# new feature
# Tags: optional

@frontend
Feature: Rejected documents uploaded from Parent App show rejected banner/sidecard

  Scenario: Verify rejected banner and sidecard shows
    Given User uploads a rejected document from parent app
    Then Package information is displayed
    And Rejected Document banner exists
    And Rejected Document sidecard exists

  Scenario: Verify rejected banner and sidecard doesn't shows
    Given User uploads a successful document from parent app
    Then Package information is displayed
    And Rejected Document banner doesn't exist
    And Rejected Document sidecard doesn't exist
