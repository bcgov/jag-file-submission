Feature: New CSO accounts can be created if a BCeID profile is not associated with a CSO account

   @backend
   Scenario: Verify a CSO account can be created successfully
   ## Create CSO account ##
   Given POST http request is made to "CSO_ACCOUNT_API" with a valid request body
   When status is 201 and content type is verified
   Then verify response returns clientId, accountId and internalClientNumber

  @backend
  Scenario: Verify a CSO account details can be retrieved successfully
   ## Get CSO account details ##
   Given GET http request is made to "CSO_ACCOUNT_API"
   When status is 200 and content type is verified
   Then verify response returns clientId, accountId and internalClientNumber

  @backend
  Scenario: Verify a CSO account internal client number can be updated successfully
   ## Update CSO account internal client number ##
    Given PUT http request is made to "CSO_ACCOUNT_API" with a valid request body
    When status is 200 and content type is verified
    Then verify response returns clientId, accountId and internalClientNumber is updated

  @backend
  Scenario: Verify a BCeID account details can be retrieved successfully
   ## Get BCeID account details ##
    Given GET request is made to "BCEID_ACCOUNT_API"
    When status is 200 and content type is verified
    Then verify response returns firstName, lastName and middleName

  @backend
  Scenario: Verify a CSO account cannot be created for requests made with incorrect path
    ## Create CSO account ##
    Given POST http request is made to "INCORRECT_CREATE_CSO_ACCOUNT_API" with incorrect path value
    When status is 404 and content type is verified
    Then verify response body has error, status and an empty message
