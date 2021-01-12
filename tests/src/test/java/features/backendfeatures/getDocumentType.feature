Feature: Document type can be retrieved

   @backend
   Scenario: Verify document type can be retrieved successfully
      ## Lookup api call ##
      Given user JWT token is retrieved from the frontend
      Then Get http request is made to "LOOK_UP_API" with court level and class details
      When response code 200 and content type are verified
      Then verify response returns documentType and description
