# new feature
# Tags: optional

Feature: Additional documents can be uploaded from EFiling hub

  Background:
    Given user is on the eFiling submission page

  Scenario: Verify documents can be uploaded using the upload link
    When user uploads an additional document
    Then verify the document is uploaded
