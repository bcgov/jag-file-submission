# new feature
# Tags: optional

Feature: Configured document types can be processed by AI reviewer
  @ignore
  Scenario: Validate valid document type can be processed
    Given user uploaded a valid "RCC" document type
    When document is processed
    Then document form data is extracted
