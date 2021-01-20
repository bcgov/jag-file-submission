# new feature
# Tags: optional

Feature: Document can be retrieved using file name

  Scenario: Validate that document can be retrieved using file name
    Given valid admin user account is authenticated
    When user submits request to get document using filename
    Then a valid document is returned
