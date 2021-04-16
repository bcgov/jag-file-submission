# new feature
# Tags: optional

Feature: A description

  @ignore
  Scenario: Validate matching document type can be flagged
    Given user uploaded a valid RCC document with "RCC" document type
    When document event is retrieved by document id
    Then document type validation flag is set for processing
