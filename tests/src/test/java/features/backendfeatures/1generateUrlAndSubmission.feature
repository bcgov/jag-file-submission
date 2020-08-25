Feature: When user uploads the documents, id is authenticated user details, navigation urls are generated and user, submission package details can be retrieved

  #@backend
    Scenario: get the token
    Given bearer token is available
  @backend
  Scenario: Verify if a single document is uploaded, url is generated and package information can be retrieved for requests made with valid CSO account
    Given POST http request is made to "DOCUMENT_SUBMISSION" with valid existing CSO account guid and a single image file
    When status code is 200 and content type is verified
    Then verify submission id and document count is received
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls
    When status code is 200 and content type is verified
   # Then verify expiry date and eFiling url are returned with the CSO account guid and submission id
   # Given "SUBMISSION" id is submitted with GET http request
   # When status code is 200 and content type is verified
   # Then verify universal id, user details, account type and identifier values are returned and not empty
   # And verify success, error and cancel navigation urls are returned
  #  Given "SUBMISSION" id with filing package path is submitted with GET http request
  #  When status code is 200 and content type is verified
  #  Then verify court details and document details are returned and not empty
  #  Given "SUBMISSION" id with filename path is submitted with GET http request
  #  Then Verify status code is 200 and content type is not json

  #@backend
  Scenario: Verify if multiple document is uploaded, url is generated and package information can be retrieved for requests made with valid CSO account
    Given POST http request is made to "DOCUMENT_SUBMISSION" with valid existing CSO account guid and multiple file
    When status code is 200 and content type is verified
    Then verify submission id and document count is received
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls
    When status code is 200 and content type is verified
    Then verify expiry date and eFiling url are returned with the CSO account guid and submission id
    Given "SUBMISSION" id is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify universal id, user details, account type and identifier values are returned and not empty
    And verify success, error and cancel navigation urls are returned
    Given "SUBMISSION" id with filing package path is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify court details and document details are returned and not empty
    Given "SUBMISSION" id with filename path is submitted with GET http request
    Then Verify status code is 200 and content type is not json

  #@backend
  Scenario: Verify if a single document is uploaded, url is generated and package information can be retrieved for requests made with non existing valid CSO account
    Given POST http request is made to "DOCUMENT_SUBMISSION" with non existing CSO account guid and a single image file
    When status code is 200 and content type is verified
    Then verify submission id and document count is received
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls with non existing guid
    When status code is 200 and content type is verified
    Then verify expiry date and eFiling url are returned with non existing CSO account guid and submission id
    Given "SUBMISSION" id is submitted with non existing CSO account GET http request
    When status code is 200 and content type is verified
    Then verify accounts value is null but names and email details are returned
    And verify success, error and cancel navigation urls are returned
    Given "SUBMISSION" id with filing package path is submitted with non existing CSO account GET http request
    When status code is 200 and content type is verified
    Then verify court details and document details are returned and not empty
    Given "SUBMISSION" id with filename path is submitted with non existing CSO account GET http request
    Then Verify status code is 200 and content type is not json

  #@backend
  Scenario: Verify document is not supported for requests made with incorrect file type value
    Given POST http request is made to "DOCUMENT_SUBMISSION" with invalid file type and a single image file
    When status code is 400 and content type is not json
    Then verify response returns document required error and message

  #@backend
  Scenario: Verify secure url is not generated for requests made with invalid CSO account guid in the request header
    Given POST http request is made to "GENERATE_URL_API" with invalid CSO account guid in the header
    When status code is 403 and content type is verified
    Then verify response returns invalid role error and message

 # @backend
  Scenario: Verify secure url is not generated if path is incorrect
    Given POST http request is made to "INCORRECT_GENERATE_URL_API" with incorrect path
    When status code is 404 and content type is verified
    Then verify error message is present and message has no value

  #@backend
  Scenario: Verify secure url is not generated if path is invalid
    Given POST http request is made to "GENERATE_URL_API" with invalid path
    When status code is 404 and content type is verified
    Then verify error message is present and message has no value

  #@backend
  Scenario: Verify secure url is not generated if path is missing id parameter
    Given POST http request is made to "GENERATE_URL_API" without id in the path
    When status code is 405 and content type is verified
    Then verify error message is present and message has no value
