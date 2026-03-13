# new feature
# Tags: optional
@frontend
Feature: Submission history can be filtered based on the parent application code

  Scenario: Validate filtered packages by application code are displayed in submit history
    Given user is on submission history page with valid application code "FLA"
    When packages history list is populated

  Scenario: Validate filtered packages by application code are displayed in submit history
    Given user is on submission history page with invalid application code "RDD"
    When packages history list is not populated
