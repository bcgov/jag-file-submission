[![Maintainability](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/maintainability)](https://codeclimate.com/github/bcgov/jag-file-submission/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/test_coverage)](https://codeclimate.com/github/bcgov/jag-file-submission/test_coverage)
# jag-file-submission

Generic File Submission API (to be used by the Family Law Act Application at first)

## Project Structure

    .
    ├── .github                             # Contains GitHub Related sources
    ├── openshift                           # openshift templates and pipeline
    ├── src/                                # application source files
    │   ├── backend                         # backend applications
    │   │   ├── jag-efiling-api             # efiling api
    │   │   ├── libs                        # backend libraries
    │   │   └── efilling-facade-client      # efiling soap client	
    │   └── efiling-frontend                # efiling frontend
    ├── COMPLIANCE.yaml                     #
    ├── CONTRIBUTING.md                     #
    ├── LICENSE                             # Apache License
    └── README.md                           # This file.

## Apps

| Name             | description                                  | doc                                             |
| ---------------- | -------------------------------------------- | ----------------------------------------------- |
| backend          | all server side services                     | [README](src/backend/README.md) |
| jag-efiling-api  | the main api for interating with the service | [README](src/backend/jag-efiling-api/README.md) |
| efiling-frontend | the frontend for uploading documents         | [README](src/efiling-frontend/README.md)        |

## Running the App

From the jag-file-submission directory, run:

### `docker-compose up -d --build`

Currently the docker image created will create a Redis container that the backend Spring Boot API interacts with.

To confirm that your docker container is working you can perform a GET request to ```http://localhost:8080/actuator``` to see all available endpoints, or a GET request to ```http://localhost:8080/actuator/health``` to see the application status.

It will also build the image and fires up the frontend container. The frontend application will run and be available on port 3000.

### `docker-compose stop`

Stops the container and the frontend application from being run/served.
