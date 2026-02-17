# new feature
# Tags: optional
@demo
Feature: List of countries can be looked up

  Scenario: Validate that list of countries can be retrieved
    Given validate user is authenticated
    When user submits request to get countries
    Then a valid country code and description is returned
