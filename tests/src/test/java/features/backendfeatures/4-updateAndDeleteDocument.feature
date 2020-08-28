Feature: User can upload additional document and delete the documents

  @backendd
  Scenario: Verify additional document can be uploaded, retrieved and deleted
     ## Call to upload the document ##
    Given initial document is posted to "DOCUMENT_SUBMISSION" with valid existing CSO account guid and a single pdf file
    When validate status code is 200 and content type
    Then verify submission id and document count is returned
    ## Call to generate url ##
    Given POST request is made to "GENERATE_URL_API" with client application, court details and navigation urls
    When validate status code is 200 and content type
    Then verify expiry date and eFiling url are returned with the submission id
    ## Call to get user details ##
    Given "SUBMISSION" id is submitted with GET request
    When validate status code is 200 and content type
    Then verify universal id, user details and account type values are returned and not empty
    And verify navigation urls are returned
    ## Call to update document ##
    Given second document is posted to "DOCUMENT_SUBMISSION"
    When validate status code is 200 and content type
    Then verify submission id and document count is returned
    ## Call to get updated document wth filename ##
    Given "SUBMISSION" id with filename is submitted with GET http request
#   Then validate status code is 200 and content type is not json
    ## Call to delete document ##
    Given "SUBMISSION" id is submitted with DELETE http request
    When validate status code is 200 and content type
    ## Call to get updated document wth filename ##
    Given "SUBMISSION" id with filename is submitted with GET http request
#   Then validate status code is 404 and content type is json

 # @backend
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
    Given "SUBMISSION" id is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify universal id, user details, account type and identifier values are returned and not empty
    And verify success, error and cancel navigation urls are returned
    ## Call to get filing package info ##
    Given "SUBMISSION" id with filing package path is submitted with GET http request
    When status code is 200 and content type is verified
    Then verify court details and document details are returned and not empty
    ## Call to get document wth filename ##
    Given "SUBMISSION" id with filename path is submitted with GET http request
   # Then Verify status code is 200 and content type is not json
