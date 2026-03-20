# new feature
# Tags: optional

Feature: User account is validated and redirected to E-Filing hub

  @frontend
  Scenario: Validate user with valid BCEID or BCSC and CSO accounts is redirected E-Filing hub
    Given User uploads a successful document from parent app
    Then Package information is displayed
    And Continue to payment button is enabled
