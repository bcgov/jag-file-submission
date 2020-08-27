Feature: When user with non existing guid uploads the documents, id is authenticated user details,
  navigation urls are generated and user, submission package details can be retrieved

  @backend
  Scenario: Verify if a single document is uploaded, url is generated and package information can be retrieved for requests made with non existing valid CSO account
    Given POST http request is made to "DOCUMENT_SUBMISSION" with non existing CSO account guid and a single pdf file
    When status code is 200 and content type are verified
    Then verify submission id and document count are received
    Given POST http request is made to "GENERATE_URL_API" with client application, court details and navigation urls with non existing guid
    When status code is 200 and content type are verified
    Then verify expiry date and eFiling url are returned with non existing CSO account guid and submission id
    Given "SUBMISSION" id is submitted with non existing CSO account GET http request
    When status code is 200 and content type are verified
    Then verify universal id, account type and identifier values are returned
    And verify success, error and cancel navigation urls are also returned
    Given "SUBMISSION" id with filing package path is submitted with non existing CSO account GET http request
    When status code is 200 and content type are verified
    Then verify court details and document details are returned
    Given "SUBMISSION" id with filename path is submitted with non existing CSO account GET http request
    Then Verify status code is 200 and content type is not json
