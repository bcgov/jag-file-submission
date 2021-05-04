# new feature
# Tags: optional

Feature: Configure document type and properties for AI reviewer

  Scenario: Validate a new document type can be configured
    Given user configures a new document type
    When document type config is created
    Then document type configuration can be retrieved

  Scenario: Validate an existing configured document type can be updated
    Given user updates an existing configured document type
    When document configuration details are updated
    Then updated document type configuration can be retrieved

  Scenario: Validate an existing configured document type can be deleted
    Given user deletes an existing configured document type using id
    Then requested document type configuration is deleted
