# new feature
# Tags: optional

Feature: Additional documents can be uploaded from EFiling hub

  @frontend
  Scenario: Verify documents can be uploaded using the upload link
    Given user is on the eFiling submission page
    When user uploads an additional document
    Then verify the document is uploaded
