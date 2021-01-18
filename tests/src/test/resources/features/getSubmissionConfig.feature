# new feature
# Tags: optional

Feature: File Submission configuration can be retrieved

  Scenario Outline: Validate that configuration details of a submission is retrieved
    Given valid admin account "<username>":"<password>" that authenticated
    When user submits request to get submission configuration
    Then a valid config information is returned
    Examples:
      | username | password |
      | bobross  | changeme |
