# new feature
# Tags: optional

Feature: Document properties can be updated

  Scenario: Validate document properties can be updated
    Given valid user account are authenticated
    When request is made to update document properties
    Then valid updated document properties are returned
