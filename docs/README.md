# jag-file-submission

Generic File Submission API (to be used by the Family Law Act Application at first)

## Introduction

Are you ready to integrate your application with the BC Gov E-filing Hub in order to allow your users to e-file documents?

This guide will provide you with all the required information you need to get started.

### Request a profile

> Follow the following instructions in [Getting started - Onboarding](onboarding.md)

### Start Integrating

Get an oauth token from our Keycloak Server using the following curl command:

```bash
curl --location --request POST 'keycloak_url' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=[your client id]' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'client_secret=[your client secret]'
```

> the secret should be kept safe and not exposed to the public

Start by uploading document(s) to the **eFiling Hub** API:

```bash
curl --location --request POST '[filing-hub-url]/submission/documents' \
--header 'X-Transaction-Id: ca09e538-d34e-11ea-87d0-0242ac130003' \
--header 'X-User-Id: [X-User-Id]' \
--header 'Content-Type: multipart/form-data' \
--header 'Authorization: Bearer [bearer_token]' \
--form 'files=test.pdf'
```

the response includes a submission id that you will use to generate a redirect url

```json
{
  "submissionId": "5e9492cf-e87e-48b5-ba55-0c198d8edde5",
  "received": 1
}
```

Then generate a unique url to redirect the users to the **eFiling Hub**

```bash
curl --location --request POST '[filing-hub-url]/submission/5e9492cf-e87e-48b5-ba55-0c198d8edde5/generateUrl' \
--header 'X-Transaction-Id: ca09e538-d34e-11ea-87d0-0242ac130003' \
--header 'X-User-Id: [X-User-Id]' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [bearer_token]' \
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
      "fileNumber": "string"
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
        "roleType": "ABC",
        "firstName": "string",
        "middleName": "string",
        "lastName": "string"
      }
    ],
    "organizationParties": [
      {
        "roleType": "ABC",
        "name": "string"
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

#### navigation

The `navigation` object represents a list of possible returns to your application based on the status of the document e-filing.

#### clientApplication

The `clientApplication` object represents how your application is labelled in **efiling hub**.

#### filingPackage

The `filingPackage` object represents the court information about the submitted package along with the document(s) info.

##### court

The `court` object represents the court details.

##### Properties

| name       | type   | required | description                                                                   |
| ---------- | ------ | -------- | ----------------------------------------------------------------------------- |
| location   | string | true     | Court House where the package is submitted to                                 |
| level      | string | true     | Court level, can be `Supreme` of `Provincial`                                 |
| courtClass | string | true     | Court Classification see [Court Classification](data.md#Court-Classification) |
| division   | string | true     | R for Criminal, I for Civil see [Court Division](data.md#Court-Division)      |
| fileNumber | string | false    | The court file number, leave blank for new Package Submission                 |

##### Documents

The `documents` array represents the previously uploaded documents.

###### Properties

| name                     | type    | required | description                                                                                |
| ------------------------ | ------- | -------- | ------------------------------------------------------------------------------------------ |
| name                     | string  | true     | the document name, must be the same name as uploaded previously                            |
| type                     | string  | true     | the type of document see [Document Type Codes](data.md#Document-Type-Codes)                |
| isAmendment              | boolean | true     | if the document is an amendment                                                            |
| isSupremeCourtScheduling | boolean | true     | if the document is directed to Supreme Court Scheduling                                    |
| data                     | object  | false    | A non defined json object representing the form data of the document                       |
| md5                      | string  | false    | The md5 hash value of the document content, used to validate the integrity of the document |

##### parties

The `parties` array represents a list of individual parties for the submission

##### Properties


| name       | type   | required | description                                                                                     |
| ---------- | ------ | -------- | ----------------------------------------------------------------------------------------------- |
| roleType   | string | true     | the party role, see [Party Role](data.md#Party-Role)                                            |
| firstName  | string | true     | the party first name                                                                            |
| middleName | string | false    | the party middle name                                                                           |
| lastName   | string | true     | the party last name                                                                             |

##### organizationParties

The `organizationParties` array represents a list of organization parties for the submission

##### Properties


| name       | type   | required | description                                                                                     |
| ---------- | ------ | -------- | ----------------------------------------------------------------------------------------------- |
| roleType   | string | true     | the party role, see [Party Role](data.md#Party-Role)                                            |
| name       | string | true     | the organization name                                                                            |

### Package Review

The efiling Package Review screen shows the details of a submitted package.

https://dev.justice.gov.bc.ca/efilinghub/packagereview/:packageId?returnUrl=&returnAppName=&defaultTab=

The returnUrl parameter is optional, but if an encoded URL is supplied, the
page will render with a button that will return the user to the given URL
(i.e., parent application).

The returnAppName parameter is optional, but if an encoded name of the Parent Application
is specified, the "Return to Parent App" button will be named accordingly.

The defaultTab parameter is optional, but if an encoded defaultTab
is specified, the Package Review will display that tab by default.

### Api Documentation

eFiling API is documented using [openapi](http://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/efiling-api/jag-efiling-api.yaml) and we have a [postman collection](https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/jag-efiling-api/src/test/jag-efiling-api.postman_collection.json) that you can use to test the eFiling API

#### Answers to FAQ:

##### What should be used for `roleType` and `partyType` in parties?

The default values to be used for `roleType` is CLA and for `partyType` it is IND. Can refer to the roles in the code tables for more details.

#### Are the various types of documents the ones present in this list? -  https://bcgov.github.io/jag-file-submission/#/data?

Yes. This is our list of various document types that can be submitted.
For FLA, the document types are as follows: Application to Obtain an Order (APO) , Notice of Motion (NM), Affidavit (AFF), and Electronic Filing Statement (EFS- Provincial).

##### For the parties info, would this be the applicant and respondent?

Yes. Applicant and respondent is correct for the parties.

##### For documents, what kind of information should/can be included in the following fields?

`Data`: A copy of the data collected on parent app side. JSON object containing the party info, potential issue, acts, things associated with divorce, etc.

`MD5`: Computed hash value of the document content, it is used as a checksum to validate the document integrity.

`isAmendment`: This marks if the document is an amendment.

`isSupremeCourtScheduling`: This marks if the document is for supreme court scheduling. The Supreme Court Scheduling will not apply initially as we are dealing with Provincial Court, but in the future Supreme Court will be applicable.

##### What should the value of `clientAppName` be?

This field signifies the name of the parent application integrating with the eFiling hub and will show up on the eFiling hub, so please use a full form name such as `Family Law Act Application`.

##### For the court information, how should the fields be determined for this section?

Parent app should know level and class. Court location can be provided through the code tables.

#### Response

on submission you will receive the following response:

```json
{
  "expiryDate": 1597944879789,
  "efilingUrl": "a link where the user should be redirected"
}
```

Redirect the user to `efilingUrl` and we will handle the rest for you.
