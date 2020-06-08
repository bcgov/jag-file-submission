# Getting started

## Quick start

Generate a secure url by calling the generateUrl endpoint

```bash

curl --location --request POST 'http://localhost:8080/document/generateUrl' \
--header 'Content-Type: application/json' \
--data-raw '{
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

E-filing-api is documented using [openapi](http://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/jag-efiling-api/jag-efiling-api.yaml)

## Diagram flow

<div style="width: 640px; height: 480px; margin: 10px; position: relative;"><iframe allowfullscreen frameborder="0" style="width:640px; height:480px" src="https://app.lucidchart.com/documents/embeddedchart/fb8a218a-99b6-4285-a653-93a6271f6de8" id="wLo6x541WcTP"></iframe></div>
