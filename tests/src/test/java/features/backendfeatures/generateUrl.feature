Feature: Secure url can be generated as per the api schema

  @backend
  Scenario: Verify if the secure url is generated
    Given user calls "GENERATEURLAPI" with POST http request
    When status code is 200 and content type is verified
    Then verify response returns "EFILINGURL" and expiry date

  @backend
  Scenario: Verify if the user detail can be retrieved
    Given user calls "USERDETAIL" with GET http request
    When status code is 200 and content type is verified
    Then verify response body has account and role information
  @backend
  Scenario: Verify secure url not generated if url is incorrect
    Given user calls incorrect "GENERATEURLAPI" with POST http request
    When status is 404 and content type is verified
    Then verify error message is present with empty message
