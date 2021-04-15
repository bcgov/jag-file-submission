# new feature
# Tags: optional

Feature: Create document type and properties configuration for AI reviewer

  @ignore
  Scenario: Validate document type can be configured
    Given user configures a document type
    When document type config is created
