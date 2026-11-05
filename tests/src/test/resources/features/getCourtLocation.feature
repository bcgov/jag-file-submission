# new feature
# Tags: optional

Feature: Court location information can be retrieved

  Scenario: Validate that court location information is retrieved
    Given valid user account information is authenticated
    When user submits request to get court location information
    Then a valid court location information is returned
