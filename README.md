[![Maintainability](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/maintainability)](https://codeclimate.com/github/bcgov/jag-file-submission/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/test_coverage)](https://codeclimate.com/github/bcgov/jag-file-submission/test_coverage)

# jag-file-submission

Generic File Submission API (to be used by the Family Law Act Application at first)

## Project Structure

    .
    ├── .github                                 # Contains GitHub Related sources
    ├── openshift                               # openshift templates and pipeline
    ├── src/                                    # application source files
    │   ├── backend                             # backend applications
    │   │   ├── efiling-api                     # efiling api
    │   │   ├── efiling-worker                  # file submission worker
    │   │   ├── libs                            # backend libraries
    │   │   |   ├── efiling-submission-client   # efiling soap client that submits packages to CSO
    │   │   |   ├── efiling-lookup-client       # efiling soap client that looksup required info for submission
    │   │   |   └── efiling-status-client       # efiling soap client for checking status of a submitted package    
    │   └── frontend                            # frontend applications
    │       ├── efiling-frontend                # efiling frontend
    │       └── efiling-demo                    # efiling demo app frontend
    │       └── shared-components               # shared bcgov themed component library
    ├── COMPLIANCE.yaml                         #
    ├── CONTRIBUTING.md                         #
    ├── LICENSE                                 # Apache License
    └── README.md                               # This file.

## Apps

| Name | description | doc |
| -------------------- | -------------------------------------------- | ---------------------------------------------------- |
| backend | all server side services | [README](src/backend/README.md) |
| efiling-api | the main api for interating with the service | [README](src/backend/efiling-api/README.md) |
| efiling-demo-backend | a demo backend that emulates a client | [README](src/backend/efiling-backend-demo/README.md) |
| efiling-worker | process submitted documents | [README](src/backend/efiling-worker/README.md) |
| frontend | all client side applications | [README](src/frontend/README.md) |
| efiling-frontend | the frontend for uploading documents | [README](src/frontend/efiling-frontend/README.md) |
| efiling-demo | the frontend for demo application | [README](src/frontend/efiling-demo/README.md) |
| shared-components | shared bcgov themed component library | [README](src/frontend/shared-components/README.md) |
| frontend-tests | automated tests for the frontend | [README](tests/README.md) |

## Running the App

run

```bash
docker-compose up -d --build
```

Currently the docker image created will create a Redis container that the backend Spring Boot API interacts with.

To confirm that your docker container is working you can perform a GET request to `http://localhost:8080/actuator` to see all available endpoints, or a GET request to `http://localhost:8080/actuator/health` to see the application status.

It will also build the image and fires up the frontend container. The efiling-frontend application will run and be available on port 3000. The efiling-demo application will run and be available on port 3001.

```bash
docker-compose stop
```

Stops the container and the frontend application from being run/served.
