Feature: As a user my account should be validated and be able to proceed with E-File submission

  Background:
    Given user is on the eFiling submission page

  @frontend
    ## Creates new CSO account. Redis need to be rebuilt to run this test##
  Scenario: Verify user with non existing CSO account guid is able to create a CSO account
    And user accepts agreement and clicks cancel button
    Then user clicks resume E-File submission in the confirmation window
    And the user stays on the E-File submission page
    Then user clicks on create CSO account
    And the CSO account is created successfully

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
