# new feature
# Tags: optional

Feature: Document types can be looked up by providing court details

  Scenario: Validate that document type is retrieved with description
    Given user credentials are authenticated
    When user submits request to get document types
    Then valid document type and description is returned
