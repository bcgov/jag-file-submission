@frontend
Feature: As a user my account should be validated and be able to proceed with E-File submission

  Background:
    Given user is on the landing page

  @frontend
    ## Creates new CSO account. Redis need to be rebuilt to run this test##
  Scenario: Verify user with non existing CSO account guid can cancel CSO account creation after accepting the user agreement
    When user enters a valid existing CSO account guid and uploads a document
    Then eFile submission page with user agreement is displayed
    Then verify there are no broken links in the page
    And user accepts agreement and clicks cancel button
    Then user confirms the cancellation in the confirmation window
    And  user is navigated to the cancel page
    Then user clicks Return Home button to navigate to the landing page

  @frontend
    ## Creates new CSO account. Redis need to be rebuilt to run this test##
  Scenario: Verify user with non existing CSO account guid is able to create account if cancel button is clicked
    When user enters a valid existing CSO account guid and uploads a document
    Then eFile submission page with user agreement is displayed
    And user accepts agreement and clicks cancel button
    Then user clicks resume E-File submission in the confirmation window
    And the user stays on the E-File submission page
    Then user clicks on create CSO account
    And the CSO account is created successfully
    Then verify there are no broken links in the page
  @demo
   @frontend
  Scenario: Verify user with valid CSO account guid can upload a document for E-File submission
    When user enters a valid existing CSO account guid and uploads a document
    Then user can upload an additional document
    And submit and verify the document is uploaded
    Then verify there are no broken links in the page
    Then user clicks continue payment button
  @demo
  @frontend
  Scenario: Verify user with valid CSO account guid can upload a document for E-File submission
    When user enters a valid existing CSO account guid and uploads a document
    Then user can upload an additional document
    And delete the selected additional document
    Then verify there are no broken links in the page
    Then user clicks cancel upload button
