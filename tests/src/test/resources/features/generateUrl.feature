# new feature
# Tags: optional

Feature: A description

  Scenario Outline: Validate that application can generate urls (using an admin account)
    Given admin account "<username>":"<password>" that authenticated with keycloak at "<keycloakhost>" on realm "<keycloakrealm>"
    When user Submit a valid pdf document
    Then a valid transaction should be generated
    And user request a submission url
    Then a valid url should be returned
    Examples:
      | username | password | keycloakhost | keycloakrealm |
      | bobross  | changeme | http://localhost:8081 | Efiling-Hub |
