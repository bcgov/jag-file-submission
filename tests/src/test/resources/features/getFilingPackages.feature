# new feature
# Tags: optional
@demo
Feature: Filing packages history can be retrieved from CSO

  Scenario: Validate that filing packages history is retrieved
    Given user id is authenticated
    When user submits request to get filing packages history
    Then valid packages history is returned
    And valid document and court details are returned
    And valid parties, org and payment details are returned
    Then rush processing and supporting documents are returned
