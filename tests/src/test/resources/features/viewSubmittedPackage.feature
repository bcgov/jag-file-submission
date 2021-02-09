# new feature
# Tags: optional

Feature: Recently submitted package details can be viewed

  Scenario: Validate submitted package details are correct
    Given user is on package review page with package id 1
    When package details are populated
    Then documents details are available in Documents tab
    And comments are available in Filing Comments tab
