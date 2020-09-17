@backend
Feature: Payment card can be updated
   @demo
   @backend
   Scenario: Verify payment card can be updated
   ## generate update-card call ##
   Given POST http request is made to "PAYMENT_API" with internalClientNumber and redirect Url details
   When status 200 is correct and content type are verified
   Then verify response returns bambora Url
