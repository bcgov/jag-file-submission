# new feature
# Tags: optional

Feature: Filing package information can be retrieved

  Scenario Outline: Validate that filing package information is retrieved
    Given valid user account "<username>":"<password>" is authenticated
    When user submits request to get filing package information
    Then a valid filing package information is returned
    Examples:
      | username | password |
      | bobross  | changeme |
