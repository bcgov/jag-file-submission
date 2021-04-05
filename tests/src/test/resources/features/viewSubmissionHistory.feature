## new feature
## Tags: optional

@frontend
Feature: Submission history of recently packages can be viewed

  Scenario: Validate correct packages are displayed in submit history
    Given user is on submission history page
    When packages history is populated
    Then user can search for a package id "1"
