# new feature
# Tags: optional

Feature: User account is validated and redirected to E-Filing hub

@frontend
  Scenario: Validate user with valid BCEID and CSO accounts is redirected E-Filing hub
    Given user is on the eFiling submission page
    Then Package information is displayed
    And continue to payment button is enabled
