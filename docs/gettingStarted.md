# Getting started

## Introduction

Ready for integrating your application with bcgov efiling service and allow your users to efile document ?

This guide will provide you with all information you need to get started.

## Test accounts

Use the following accounts to test the application

| Account Guid                         | Description                                              |
| ------------------------------------ | -------------------------------------------------------- |
| 77da92db-0791-491e-8c58-1a969e67d2fa | An account with a linked CSO account and an efiling role |
| 77da92db-0791-491e-8c58-1a969e67d2fb | An account with a linked CSO account and no efiling role |
| 88da92db-0791-491e-8c58-1a969e67d2fb | An account without CSO account but a fake bceid profile  |

## Quick start

Generate a secure url by calling the generateUrl endpoint

```bash

curl --location --request POST 'http://localhost:8080/document/generateUrl' \
--header 'Content-Type: application/json' \
--data-raw '{
  "documentMetadata": {
    "type": "string",
    "subType": "string",
    "documentAccess": {
      "url": "string",
      "verb": "GET",
      "headers": {
        "additionalProp1": "string",
        "additionalProp2": "string",
        "additionalProp3": "string"
      }
    }
  },
  "navigation": {
    "success": {
      "url": "string"
    },
    "error": {
      "url": "string"
    },
    "cancel": {
      "url": "string"
    }
  }
}'

```

## Api Documentation

E-filing-api is documented using [openapi](http://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/efiling-api/jag-efiling-api.yaml) and we have a [postman collection](https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/jag-efiling-api/src/test/jag-efiling-api.postman_collection.json) that you can use to test the e-filing-api

## Diagram flow

<div style="width: 640px; height: 480px; margin: 10px; position: relative;"><iframe allowfullscreen frameborder="0" style="width:640px; height:480px" src="https://app.lucidchart.com/documents/embeddedchart/fb8a218a-99b6-4285-a653-93a6271f6de8" id="wLo6x541WcTP"></iframe></div>
