# new feature
# Tags: optional

Feature: Document can be retrieved from the Filing package

  Scenario: Validate that document is retrieved from Filing package
    Given user is authenticated
    When user submits request to get document
    Then valid document is returned
