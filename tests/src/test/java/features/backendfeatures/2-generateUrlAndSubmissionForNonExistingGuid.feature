Feature: When user with non existing guid uploads the documents, id is authenticated user details,
  navigation urls are generated and user, submission package details can be retrieved

  @backend
  Scenario: Verify if a single document is uploaded, url is generated and package information can be retrieved for
  requests made with non existing valid CSO account
    ## Call to upload the document ##
    Given POST http request is made to "DOCUMENT_SUBMISSION" with non existing CSO account guid and a single pdf file
    When status code is 200 and content type are verified
    Then verify submission id and document count are received
    ## Call to generate url ##
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls with non existing guid
    When status code is 200 and content type are verified
    Then verify expiry date and eFiling url are returned with non existing CSO account guid and submission id
    ## Call to get submission config ##
    Given token is retrieved from the frontend
    Then "SUBMISSION" id is submitted with non existing CSO account GET http request
    When status code is 200 and content type are verified
    Then verify clientAppName and csoBaseUrl values are returned
    And verify success, error and cancel navigation urls are also returned
    ## Call to get filing package info ##
    Given "SUBMISSION" id with filing package path is submitted with non existing CSO account GET http request
    When status code is 200 and content type are verified
    Then verify court details and document details are returned
    ## Call to get document wth filename ##
    Given "SUBMISSION" id with filename path is submitted with non existing CSO account GET http request
    Then Verify status code is 200 and content type is octet-stream
