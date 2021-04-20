# new feature
# Tags: optional

Feature: Auto processing flag can be set based on the document type validation

  @ignore
  Scenario: Validate matching document type can be flagged
    Given user uploaded a valid RCC document with "RCC" document type
    When document event is retrieved by document id
    Then document type validation flag is set for processing

  @ignore
  Scenario: Validate matching document type can be flagged
    Given user uploaded a invalid RCC document with "RCC" document type
    When document event is retrieved by document id
    Then document type validation flag is not set and provides error details
