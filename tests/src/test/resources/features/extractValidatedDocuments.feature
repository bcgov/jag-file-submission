# new feature
# Tags: optional

Feature: Configured document types can be processed by AI reviewer
  @ignore
  Scenario: Validate valid document type can be processed
    Given user uploaded a "RCC" document type
    When document is processed
    Then document form data is extracted
  @ignore
  Scenario: Validate invalid document type is not processed
    Given user uploaded a "AFF" document type
    Then document is not processed
