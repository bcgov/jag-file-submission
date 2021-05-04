# new feature
# Tags: optional

Feature: Configured document types can be processed by AI reviewer

  Scenario: Validate form data can be extracted from valid document type
    Given user uploaded a "RCC" document type
    When document is processed
    Then document form data is extracted
    And delete the document

  Scenario: Validate invalid document type is not processed
    Given user uploaded a "AFF" document type
    Then document is not processed
    And delete the document
