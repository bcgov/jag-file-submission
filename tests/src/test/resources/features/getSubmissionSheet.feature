# new feature
# Tags: optional

Feature: Submission sheet for package can be retrieved

  Scenario: Validate that submission sheet is retrieved
    Given user account is authenticated
    When user submits request to get submission sheet
    Then valid submission sheet is returned
