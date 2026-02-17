# new feature
# Tags: optional

Feature: Filing package details can be retrieved from CSO

  Scenario: Validate that filing packages details is retrieved
    Given user account information is authenticated
    When user submits request to get filing packages details
    Then valid package details are returned
    And valid court and documents details are returned
    And valid parties and payment detail are returned
