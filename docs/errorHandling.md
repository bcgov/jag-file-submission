# Error Handling

This documentation defines how errors may be handled by the eFiling Hub; it is based on the User Flow diagram.

## Incorrect Navigation

1. Navigating to the site without a submission ID or with an invalid submission ID will result in rendering an error message.

## Initial Package Verification

1. Package information from Parent App is not sufficient - does not include document descriptions, valid Parent App return link etc.
   - System returns error to Parent App
   - Parent App action TDB

## User Account Verification/Setup

1. User does not have an eFiling role

   - Parent app may hide button/option to use eFiling (Check for eFiling role on authentication?)
   - Parent app may display error message. E.g. "You do not have a valid CSO account and are unable to use the eFiling Hub to file documents at this time"

2. User does not authorize creation of CSO account (Does not tick the "I Accept" checkbox)

   - "Create CSO Account" button is in disabled state with roll-over/tap text "Please accept the Service Agreement to continue"

3. CSO Account cannot be created (may be multiple reasons)
   - User is returned to Parent App with error message. E.g. "CSO Account required for eFiling could not be created at this time. Please try again later"

## Package Confirmation

- No known error potential at this step. Initial Package Verification error handling should catch error states.

## Document Upload

1. Document fails upload because size is over 10mb

   - eFiling Hub displays error message. E.g. “Document size is limited to 10mb. Please reduce the size of your documents (_tips on how to reduce size?_) or visit (_information on process?_) to learn how to file larger documents”

2. Document fails to upload because extention type is not supported

   - eFiling Hub displays error message. E.g. "Documents with the following extension type are not supported: exe"

3. Document fails to upload to staging area

   - eFiling Hub displays error message. E.g. "Document upload failed. Please try again"

4. Document fails to upload to staging area more than 3 times
   - TBD

## Payment Review

1. Registering a new card with Bambora fails

   - Bambora app handles validation failure. User stays on Bambora until they cancel.

2. If user cancels on Bambora

   - eFiling Hub displays the alert "You do not have a valid Credit Card registered with your CSO Account. Register a card now to continue.

3. If a user tries to "Register a card now" more than 5 times
   - (_do we want to treat this an card validation attempt security event?_) TBD

## Package Submission

1. User does not accept the "I have reviewed the information" prompt (Does not tick the "I Agree" checkbox)

   - "Submit" button is in disabled state with roll-over/tap text "Please review your submission and Agree to continue"

2. Submit documents to CSO failed because CSO is down

   - System tried again with loading indicator displayed in eFiling Hub

3. Submit documents to CSO failed more than 3 times

   - User is returned to Parent App with error message. E.g. "Package filing failed. Please try again later"

4. CSO fee processing failed
   - TBD

## Rush/Urgent Submission

- TBD

## Cancel

1. User clicks "Cancel" button
   - User is prompted to confirm, and if yes, is returned to Parent App with message. E.g. "Your eFiling submission has been cancelled"
