# new feature
# Tags: optional

Feature: Valid Url can be generated

  Scenario: Validate that application can generate urls (using an admin account)
    Given admin account is authenticated
    When user Submit a valid pdf document
    Then a valid transaction should be generated
    And user request a submission url
    Then a valid url should be returned
