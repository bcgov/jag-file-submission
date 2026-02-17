# new feature
# Tags: optional

@frontend
Feature: User uploads files with the same name from parent app

  Scenario: User uploads files with the same name from parent app
    Given User uploads duplicate documents from parent app
    Then Package information is displayed
    And Duplicate Document banner exists

  Scenario: User uploads files with different names from parent app
    Given User uploads different documents from parent app
    Then Package information is displayed
    And Duplicate Document banner doesn't exist
