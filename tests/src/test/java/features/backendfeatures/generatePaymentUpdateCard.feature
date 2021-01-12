Feature: Payment card can be updated

   @backend
   Scenario: Verify payment card can be updated
      ## generate update-card call ##
      Given user jwt is retrieved from the frontend
      Then POST http request is made to "PAYMENT_API" with internalClientNumber and redirect Url details
      When status 200 is correct and content type are verified
      Then verify response returns bambora Url
