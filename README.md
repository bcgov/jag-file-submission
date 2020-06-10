# jag-file-submission

Generic File Submission API (to be used by the Family Law Act Application at first)

## Project Structure

    .
    ├── .github                             # Contains GitHub Related sources
    ├── openshift                           # openshift templates and pipeline
    ├── src/                                # application source files
    │   └── backend                         # backend applications
    │       └── jag-efiling-api             # efiling api
    │   └── efiling-frontend                # efiling frontend
    ├── COMPLIANCE.yaml                     #
    ├── CONTRIBUTING.md                     #
    ├── LICENSE                             # Apache License
    └── README.md                           # This file.

## Apps

| Name             | description                                  | doc                                             |
| ---------------- | -------------------------------------------- | ----------------------------------------------- |
| jag-efiling-api  | the main api for interating with the service | [README](src/backend/jag-efiling-api/README.md) |
| efiling-frontend | the frontend for uploading documents         | [README](src/efiling-frontend/README.md)        |

## Running the App

In order to run the application, run:

### `docker-compose up -d --build`

Builds the image and fires up the container. Just running this single command will install all required dependencies and start up the app.

The frontend application will run and be available on port 3000.

### `docker-compose stop`

Stops the container and the frontend application from being run/served.
