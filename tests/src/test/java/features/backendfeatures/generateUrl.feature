Feature: User id is authenticated and user details are retrieved with generated navigation urls (success, cancel, error)

  @backend
  Scenario: Verify secure url is generated for requests made with valid CSO account guid in the request header
    Given POST http request is made to "GENERATE_URL_API" with valid existing CSO account guid in header
    When status code is 200 and content type is verified
    Then verify response returns "EFILING_URL" and expiry date
    Given "SUBMISSION" id is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify account type and identifier values are returned and other user details are empty
    And verify success, error and cancel navigation urls are returned

  @backend
  Scenario: Verify secure url is generated for requests made with non existing CSO account guid in the request header
    Given POST http request is made to "GENERATE_URL_API" with non existing CSO account guid in the header
    When status code is 200 and content type is verified
    Then verify response returns "EFILING_URL" and expiry date
    Given "SUBMISSION" id is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify accounts value is null but names and email details are returned
    And verify success, error and cancel navigation urls are returned

  @backend
  Scenario: Verify secure url is not generated for requests made with invalid CSO account guid in the request header
    Given POST http request is made to "GENERATE_URL_API" with invalid CSO account guid in the header
    When status code is 403 and content type is verified
    Then verify response returns invalid role error and message

  @backend
  @negative
  Scenario: Verify secure url is not generated if path is incorrect
    Given POST http request is made to "INCORRECT_GENERATE_URL_API" with incorrect path
    When status code is 404 and content type is verified
    Then verify error message is present and message has no value

  @backend
  @negative
  Scenario: Verify secure url is not generated if path is invalid
    Given POST http request is made to "GENERATE_URL_API" with invalid path
    When status code is 405 and content type is verified
    Then verify error message is present and message has no value
