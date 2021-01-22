# new feature
# Tags: optional

Feature: Additional documents can be uploaded after the initial upload

  Scenario: Validate additional documents can be uploaded
    Given valid admin user credentials are authenticated
    When request is made to upload additional documents
    Then valid document count is returned
