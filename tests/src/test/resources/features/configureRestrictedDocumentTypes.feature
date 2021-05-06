# new feature
# Tags: optional

Feature: Configure and Manage restricted document types in AI reviewer

  Scenario: Validate a new restricted document type can be added
    Given user adds a new restricted document type
    When restricted document type config is created
    Then document details can be retrieved using id

  Scenario: Validate an existing restricted document type can be updated
    Given user updates an existing restricted document type
    When document details are updated
    Then updated document details can be retrieved by id

  Scenario: Validate an existing restricted document type can be deleted
    Given user deletes an existing restricted document type using id
    Then requested document is deleted

  Scenario: Validate all restricted document types can be retrieved
    Given user requests to get all existing restricted document types
    Then all restricted document types details can be retrieved
    And documents can be deleted
