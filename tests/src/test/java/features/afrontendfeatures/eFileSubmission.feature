Feature: As a user my account should be validated and be able to proceed with E-File submission

  Background:
    Given user is on the eFiling submission page

  @frontend
  Scenario: Verify user with valid CSO account guid can upload a document for E-File submission
    When user can upload an additional document
    And submit and verify the document is uploaded
    Then user clicks continue payment button

  @frontend
  Scenario: Verify user can remove the selected document without uploading
    When user can upload an additional document
    And delete the selected additional document
    Then user clicks cancel upload button
