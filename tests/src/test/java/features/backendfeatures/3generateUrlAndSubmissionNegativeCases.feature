Feature: When user uploads the incorrect filetype, has a invalid cso account then correct error messages are returned

  @backend
  ## Call to upload the document ##
  Scenario: Verify document is not supported for requests made with incorrect file type value
    Given POST http request is made to "DOCUMENT_SUBMISSION" with invalid file type and a single image file
    When status code is 400 and content type is not json
    Then verify response returns document required error and message

  @backend
  ## Call to generate url ##
  Scenario: Verify secure url is not generated for requests made with invalid CSO account guid in the request header
    Given POST http request is made to "GENERATE_URL_API" with invalid CSO account guid in the header
    When error status code is 403 and content type is verified
    Then verify response returns invalid role error and message

  @backend
  Scenario: Verify secure url is not generated if path is incorrect
    Given POST http request is made to "INCORRECT_GENERATE_URL_API" with incorrect path
    When error status code is 404 and content type is verified
    Then verify error message is present and message has no value

   @backend
  Scenario: Verify secure url is not generated if path is invalid
    Given POST http request is made to "GENERATE_URL_API" with invalid path
    When error status code is 404 and content type is verified
    Then verify error message is present and message has no value

  @backend
  Scenario: Verify secure url is not generated if path is missing id parameter
    Given POST http request is made to "GENERATE_URL_API" without id in the path
    When error status code is 405 and content type is verified
    Then verify error message is present and message has no value
