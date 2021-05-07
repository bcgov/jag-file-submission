# new feature
# Tags: optional
@frontend
Feature: Configure document type and properties in AI reviewer admin client

  Scenario: Validate a new document type configuration can be added
    Given user adds and submits new document type configuration
    Then new document type configuration is created

  Scenario: Validate an existing document type configuration can be updated
    Given user updates and submits existing document type configuration
    Then document type configuration is updated

  Scenario: Validate an existing document type configuration can be deleted
    Given user deletes an existing document type configuration
    Then  document type configuration is removed from the admin client
