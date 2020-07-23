[![Maintainability](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/maintainability)](https://codeclimate.com/github/bcgov/jag-file-submission/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/test_coverage)](https://codeclimate.com/github/bcgov/jag-file-submission/test_coverage) ![Cucumber Tests](https://github.com/bcgov/jag-file-submission/workflows/Cucumber%20Tests/badge.svg)

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
    │   │   |   ├── efiling-bom                 # pom Bill Of Materials
    │   │   |   ├── efiling-commons             # efiling soap client that submits packages to CSO
    │   │   |   ├── efiling-cso-starter         # efiling soap client that contains all soap implementations
    │   │   |   └── efiling-demo-starter        # efiling demo app that mocks all soap implementations
    │   └── frontend                            # frontend applications
    │       ├── efiling-frontend                # efiling frontend
    │       └── efiling-demo                    # efiling demo app frontend
    ├── COMPLIANCE.yaml                         #
    ├── CONTRIBUTING.md                         #
    ├── LICENSE                                 # Apache License
    └── README.md                               # This file.

## Apps

| Name                | description                                  | doc                                                      |
| ------------------- | -------------------------------------------- | -------------------------------------------------------- |
| backend             | all server side services                     | [README](src/backend/README.md)                          |
| efiling-api         | the main api for interating with the service | [README](src/backend/efiling-api/README.md)              |
| efiling-worker      | process submitted documents                  | [README](src/backend/efiling-worker/README.md)           |
| efiling-cso-starter | soap client implementations                  | [README](src/backend/libs/efiling-cso-starter/README.md) |
| efiling-worker      | process submitted documents                  | [README](src/backend/efiling-worker/README.md)           |
| frontend            | all client side applications                 | [README](src/frontend/README.md)                         |
| efiling-frontend    | the frontend for uploading documents         | [README](src/frontend/efiling-frontend/README.md)        |
| efiling-demo        | the frontend for demo application            | [README](src/frontend/efiling-demo/README.md)            |
| cucumber-tests      | automated tests for frontend and backend     | [README](tests/README.md)                                |

## Running the App

By default a demo mode is enabled.
if you want to integrate with the CSO application, first create a local `.env` at the root of the repository based off `.env.template`.

Change the `MVN_PROFILE` to `default`

Set the following environement variables:

```
MVN_PROFILE=default
CSO_ACCOUNTFACADE_URI=
CSO_ACCOUNTFACADE_USERNAME=
CSO_ACCOUNTFACADE_PASSWORD=
CSO_ROLEREGISTRY_USERNAME=
CSO_ROLEREGISTRY_PASSWORD=
CSO_ROLEREGISTRY_URI=
CSO_LOOKUPFACADE_USERNAME=
CSO_LOOKUPFACADE_PASSWORD=
CSO_LOOKUPFACADE_URI=
BCEID_LOOKUP_USERNAME=
BCEID_LOOKUP_PASSWORD=
BCEID_LOOKUP_URI=
```

run

```bash
docker-compose up -d --build
```

to get started, access the front end application [here](http://localhost:3001) and enter a user account and you will get redirected to the file upload.

You can get test accounts [here](https://bcgov.github.io/jag-file-submission/#/gettingStarted?id=test-accounts) when the app is running in demo mode.

![screen](docs/media/demoApp.png)

### List of services

#### efiling-frontend

React front end accessible at [http://localhost:3000](http://localhost:3000)

#### efiling-demo

React front end demo app accessible at [http://localhost:3001](http://localhost:3001)

#### efiling-api

Efiling Api check health at [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

#### redis

A [redis](https://redis.io/) instance exposed on port 6379

#### redis commander:

A [redis-commander](http://joeferner.github.io/redis-commander/) instance to query redis accessible at [http://localhost:8082](http://localhost:8082)

#### rabbitmq

A [rabbitmq](https://www.rabbitmq.com/) access the management console at [http://localhost:15672](http://localhost:15672)

#### postgres:

A [postgresql](https://www.postgresql.org/) to support keycloak

#### keycloak:

A [keycloak](https://www.keycloak.org/) instance accessible at [http://localhost:8081/auth](http://localhost:8081/auth)
