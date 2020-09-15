# Getting started

## Introduction

Ready for integrating your application with bcgov efiling service and allow your users to efile document ?

This guide will provide you with all information you need to get started.

### Request a profile

> Follow the following instructions in [Getting started - Onboarding](onboarding.md)

### Start Integrating

Get an oauth token from our Keycloak Server using the following curl

```bash
curl --location --request POST 'url' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=[your client id]' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'client_secret=[your client secret]'
```

> the secret should be kept safe and not exposed to the public

Start by uploading document(s) to the **efiling hub** api

```bash
curl --location --request POST 'http://fla-nginx-proxy-qzaydf-dev.pathfinder.gov.bc.ca/api/submission/documents' \
--header 'X-Transaction-Id: ca09e538-d34e-11ea-87d0-0242ac130003' \
--header 'Content-Type: multipart/form-data' \
--header 'Authorization: Bearer [your bearer token]' \
--form 'files=test.pdf'
```

the response includes a submission id that you will use to generate a redirect url

```json
{
  "submissionId": "5e9492cf-e87e-48b5-ba55-0c198d8edde5",
  "received": 1
}
```

Then generate a unique url to redirect the users to the **efiling hub**

```bash
curl --location --request POST 'http://fla-nginx-proxy-qzaydf-dev.pathfinder.gov.bc.ca/api/submission/5e9492cf-e87e-48b5-ba55-0c198d8edde5/generateUrl' \
--header 'X-Transaction-Id: ca09e538-d34e-11ea-87d0-0242ac130003' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [your bearer token]' \
--data-raw 'the json payload bellow'
```

payload:

```json
{
  "clientAppName": "string",
  "filingPackage": {
    "court": {
      "location": "string",
      "level": "P",
      "courtClass": "F",
      "division": "I",
      "fileNumber": "string",
      "participatingClass": "string"
    },
    "documents": [
      {
        "name": "string",
        "type": "ABP",
        "isAmendment": true,
        "isSupremeCourtScheduling": true,
        "data": {},
        "md5": "string"
      }
    ],
    "parties": [
      {
        "partyType": "IND",
        "roleType": "ABC",
        "firstName": "string",
        "middleName": "string",
        "lastName": "string"
      }
    ]
  },
  "navigationUrls": {
    "success": "string",
    "error": "string",
    "cancel": "string"
  }
}
```

### Generate URL Payload Details

The `navigation` object represents a list of possible returns to your application based on the status of the document e-filing.
The `clientAppplication` object represents how your application is labelled in **efiling hub**.
The `filingPackage` object represents the court information about the submited package along with the document(s) info.
The `documents` array represents the previously uploaded document.

Answers to FAQ:

1) What should be used for `roleType` and `partyType` in parties?

The default values to be used for `roleType` is CLA and for `partyType` it is IND.

2) Are the various types of documents the ones present in the list in https://bcgov.github.io/jag-file-submission/#/data?

Yes. This is our list of various document types that can be submitted.

3) For the parties info, would this be the applicant and respondent?

NOT SURE

4) For documents, what kind of information should/can be included in the following fields?

Data:  

MD5: 

isAmendment: This marks if the document is an amendment and is for use only by documents uploaded via the eFiling hub.

5) What should the value of `clientAppName` be?

This field signifies the name of the parent application integrating with the eFiling hub and will show up on the eFiling hub, so please use a full form name such as `Family Law Act Application`.

6) For the court information, how should the fields be determined for this section?

NOT SURE

### Response

on submission you will receive the following response:

```json
{
  "expiryDate": 1597944879789,
  "efilingUrl": "a link where the user should be redirected"
}
```

Redirect the user to `efilingUrl` and we will handle the rest for you.

## Api Documentation

E-filing-api is documented using [openapi](http://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/efiling-api/jag-efiling-api.yaml) and we have a [postman collection](https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/jag-efiling-api/src/test/jag-efiling-api.postman_collection.json) that you can use to test the e-filing-api
