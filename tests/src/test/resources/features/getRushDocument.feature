# new feature
# Tags: optional

Feature: Rush document can be retrieved from the Filing package

  Scenario: Validate that Rush document is retrieved from Filing package
    Given user access is authenticated
    When user submits request to get a rush document
    Then valid Rush document is returned
