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

Start by uploading document(s) to the efiling api

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

Then generate a unique url to redirect the users

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
  "navigation": {
    "success": {
      "url": "http//somewhere.com"
    },
    "error": {
      "url": "http//somewhere.com"
    },
    "cancel": {
      "url": "http//somewhere.com"
    }
  },
  "clientApplication": {
    "displayName": "your application name"
  },
  "filingPackage": {
    "court": {
      "location": "XXXX",
      "level": "X",
      "courtClass": "X",
      "division": "X"
    },
    "documents": [
      {
        "name": "test.pdf",
        "type": "XXX"
      }
    ]
  }
}
```

The `navigation` object represents a list of possible returns to your application based on the status of the document e-filing

the `clientAppplication` object represents how your application is labelled in e-filing-hub

the `filingPackage` object represents the court information about the submited package

the `documents` array represents the previously uploaded document

on submission you will receive the following response:

```json
{
  "expiryDate": 1597944879789,
  "efilingUrl": "a link where the user should be redirected"
}
```

Redirect the user to `efilingUrl` and we will handle the rest for you.

## Test accounts

Use the following accounts to test the application

| Account Guid                         | Description                                                    |
| ------------------------------------ | -------------------------------------------------------------- |
| 77da92db-0791-491e-8c58-1a969e67d2fa | Vivian Brown has a linked CSO account and file role            |
| 77da92db-0791-491e-8c58-1a969e67d2fb | Lynda Ridge has a linked CSO account but no file role          |
| 88da92db-0791-491e-8c58-1a969e67d2fb | Bob Ross doesn't have a CSO account but he has a bceid profile |

## Api Documentation

E-filing-api is documented using [openapi](http://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/efiling-api/jag-efiling-api.yaml) and we have a [postman collection](https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/jag-efiling-api/src/test/jag-efiling-api.postman_collection.json) that you can use to test the e-filing-api

## Diagram flow

<div style="width: 640px; height: 480px; margin: 10px; position: relative;"><iframe allowfullscreen frameborder="0" style="width:640px; height:480px" src="https://app.lucidchart.com/documents/embeddedchart/fb8a218a-99b6-4285-a653-93a6271f6de8" id="wLo6x541WcTP"></iframe></div>
