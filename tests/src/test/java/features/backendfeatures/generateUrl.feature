Feature: Secure url can be generated as per the api schema

  @backend
  Scenario: Verify if the secure url is generated
    Given user calls "GENERATE_URL_API" with POST http request
    When status code is 200 and content type is verified
    Then verify response returns "EFILING_URL" and expiry date
  @backend
  Scenario: Verify if the user detail can be retrieved
    Given user calls "SUBMISSION" with GET http request
    When status code is 200 and content type is verified
    Then verify response body has account and redirect Urls
  @backend
  @negative
  Scenario: Verify secure url not generated if path is incorrect
    Given user calls incorrect "INCORRECT_GENERATE_URL_API" with POST http request
    When status is 404 and content type is verified
    Then verify error message is present and message has no value
  @backend
  @negative
  Scenario: Verify secure url not generated if path is invalid
    Given user calls invalid "GENERATE_URL_API" with POST http request
    When status is 405 and content type is verified
    Then verify error message is present and message has no value

