# new feature
# Tags: optional

@frontend
Feature: Additional documents can be uploaded from EFiling hub

  Background:
    Given User uploads a successful document from parent app
  
  Scenario: Verify documents can be uploaded using the upload link
    When user uploads an additional document
    Then verify the document is uploaded
    Then verify duplicate uploaded filename error doesn't exist

  Scenario: Verify error on duplicate documents
    When user uploads the same document
    Then verify duplicate uploaded filename error exists
