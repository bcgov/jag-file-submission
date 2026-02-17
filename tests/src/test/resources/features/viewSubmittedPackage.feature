## new feature
## Tags: optional

@frontend
Feature: Recently submitted package details can be viewed

  Background:
    Given user is on package review page with package id
    When package details are populated

  Scenario: Validate document details are correct in submitted package
    Then documents details are available in Documents tab
#    And document can be downloaded

  Scenario: Validate organization and Individual party type are supported
    Then Individual and Organization party type details are correct in parties tab

  Scenario: Validate Filing Comments are correct in submitted package
    Then comments are available in Filing Comments tab

  Scenario: Validate payment status is correct in submitted package
    Then payment status information is correct

  Scenario: Validate user can preview packages in CSO and return to Efiling hub
    Then user can navigate to "wherearemypackage" page and return to Efiling hub
