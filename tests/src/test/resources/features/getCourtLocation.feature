# new feature
# Tags: optional

Feature: Court location information can be retrieved

  Scenario Outline: Validate that court location information is retrieved
    Given valid user account "<username>":"<password>" information is authenticated
    When user submits request to get court location information
    Then a valid court location information is returned
    Examples:
      | username | password |
      | bobross  | changeme |
