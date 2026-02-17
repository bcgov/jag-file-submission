# new feature
# Tags: optional

Feature: Payment processing service can be created

  Scenario: Validate payment processing service can be created
    Given valid admin user is authenticated
    When request is posted to submit to create service
    Then payment processing is created
