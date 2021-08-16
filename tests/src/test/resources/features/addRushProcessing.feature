# new feature
# Tags: optional
@demo
Feature: Rush processing data point can be added to the package

  Scenario: Validate the rush processing data point can be added to the package
    Given valid user is authenticated
    When request is posted to submission to add rush processing
    Then rush processing is created
