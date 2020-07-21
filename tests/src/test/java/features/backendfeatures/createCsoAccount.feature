Feature: New CSO accounts can be created if a BCeID profile is not associated with a CSO account

   @cso
   Scenario: Verify a CSO account can be created successfully for requests made with valid request body
   Given POST http request is made to "CREATE_CSO_ACCOUNT_API" with a valid request body
   When status is 201 and content type is verified
   Then verify response returns names, email and accounts with type and identifiers
  @cso
  Scenario: Verify a CSO account cannot be created for requests made with incorrect account type
    Given POST http request is made to "CREATE_CSO_ACCOUNT_API" with incorrect account type
    When status is 400 and content type is verified
    Then verify response body has error, status and an empty message
  @cso
  Scenario: Verify a CSO account cannot be created for requests made with incorrect path
    Given POST http request is made to "INCORRECT_CREATE_CSO_ACCOUNT_API" with incorrect path value
    When status is 404 and content type is verified
    Then verify response body has error, status and an empty message
