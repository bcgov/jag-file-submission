Feature: When user uploads the documents, id is authenticated user details, navigation urls are generated and user,
  submission package details can be retrieved

  @backend
  Scenario: Verify if a single document is uploaded, url is generated and package information can be retrieved for requests made with valid CSO account
    ## Call to upload the document ##
    Given POST http request is made to "DOCUMENT_SUBMISSION" with valid existing CSO account guid and a single pdf file
    When status code is 200 and content type is verified
    Then verify submission id and document count is received
    ## Call to generate url ##
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls
    When status code is 200 and content type is verified
    Then verify expiry date and eFiling url are returned with the CSO account guid and submission id
    ## Call to get submission config ##
    Given user token is retrieved from the frontend
    Then "SUBMISSION" id is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify clientAppName and csoBaseUrl values are returned and not empty
    And verify success, error and cancel navigation urls are returned
    ## Call to get filing package info ##
    Given "SUBMISSION" id with filing package path is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify court details and document details are returned and not empty
    ## Call to get document wth filename ##
    Given "SUBMISSION" id with filename path is submitted with GET http request
    Then Verify status code is 200 and content type is not json

  @backend
  Scenario: Verify if multiple document is uploaded, url is generated and package information can be retrieved for requests made with valid CSO account
    ## Call to upload the document ##
    Given POST http request is made to "DOCUMENT_SUBMISSION" with valid existing CSO account guid and multiple file
    When status code is 200 and content type is verified
    Then verify submission id and document count is received
    ## Call to generate url ##
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls
    When status code is 200 and content type is verified
    Then verify expiry date and eFiling url are returned with the CSO account guid and submission id
    ## Call to get user details ##
    Given user token is retrieved from the frontend
    Then "SUBMISSION" id is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify clientAppName and csoBaseUrl values are returned and not empty
    And verify success, error and cancel navigation urls are returned
    ## Call to get filing package info ##
    Given "SUBMISSION" id with filing package path is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify court details and document details are returned and not empty

   ## Below commented out test steps will be used when the endpoints are implemented on demo mode ##

  ## Call to submit document ##
  #  Given "SUBMISSION" id with submit path is submitted with POST http request
  #  Then Verify status code is 200 and content type is not json
  #  And packageRef is returned

  @backend
  Scenario: Verify if court location information can be retrieved
    ## Call to upload the document ##
    Given POST http request is made to "DOCUMENT_SUBMISSION" with valid existing CSO account guid and a single pdf file
    When status code is 200 and content type is verified
    Then verify submission id and document count is received
    ## Call to generate url ##
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls
    When status code is 200 and content type is verified
    Then verify expiry date and eFiling url are returned with the CSO account guid and submission id
    ## Call to get court location ##
    Given "COURTS_API" without court level type is submitted with GET http request
    When status code is 200 and content type is verified
    Then validate the court location details
