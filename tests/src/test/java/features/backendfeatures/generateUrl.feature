Feature: Secure url can be generated as per the api contract

  @backend
  Scenario: Verify if the secure url is generated
    Given user calls "GENERATEURLAPI" with POST http request
    When status code is 200 and content type is verified
    Then verify response returns "EFILINGURL" and expiry date
  @backend
  Scenario: Verify secure url not generated if url is incorrect
    Given user calls incorrect "GENERATEURLAPI" with POST http request
    When status is 404 and content type is verified
    Then verify response payload objects are correct
  @backend
  Scenario: Verify if the eFilingUrl token is valid
    Given user calls "GETCONFIGURATION" with GET http request
    When status code is 200 and content type is verified
    Then verify response body matches the POST request
