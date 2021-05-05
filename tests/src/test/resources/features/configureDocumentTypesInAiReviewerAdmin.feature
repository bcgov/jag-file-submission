# new feature
# Tags: optional

Feature: Configure document type and properties in AI reviewer admin client

  Scenario: Validate a new document type configuration can be added
    Given user adds a new document type configuration
    When document type config is submitted
    Then new document type configuration is created

  Scenario: Validate an existing document type configuration can be updated
    Given user updates an existing document type configuration
    When updated document configuration details are submitted
    Then document type configuration is updated

  Scenario: Validate an existing document type configuration can be deleted
    Given user deletes an existing document type configuration
    Then  document type configuration is removed from the admin client
