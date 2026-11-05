# new feature
# Tags: optional

Feature: File Submission configuration can be retrieved

  Scenario: Validate that configuration details of a submission is retrieved
    Given valid admin account that authenticated
    When user submits request to get submission configuration
    Then a valid config information is returned
