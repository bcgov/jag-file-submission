[![Maintainability](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/maintainability)](https://codeclimate.com/github/bcgov/jag-file-submission/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/4078a74ee2bb4d400fd9/test_coverage)](https://codeclimate.com/github/bcgov/jag-file-submission/test_coverage) ![Cucumber Tests](https://github.com/bcgov/jag-file-submission/workflows/Cucumber%20Tests/badge.svg)

# jag-file-submission

Generic File Submission API (to be used by the Family Law Act Application at first)

## Summary

As a component of the Court Digital Transformation Strategy (CDTS), the eFiling hub uses modern, secure, scalable, microservice architecture and API first design to facilitate easy integration of other applications (i.e. Family Law Act application, Online Divorce Assistant, Representation Agreement app) with the current electronic filing services that are provisioned by the Court Services Online application.

The eFiling hub is a foundational component to enhance citizen experiences for the submission of court documents electronically, while streamlining backend court registry processes.

## Project Structure

    .
    ├── .github                                 # Contains GitHub Related sources
    ├── openshift                               # openshift templates and pipeline
    ├── src/                                    # application source files
    │   ├── backend                             # backend applications
    │   │   ├── efiling-api                     # efiling api
    │   │   ├── libs                            # backend libraries
    │   │   |   ├── efiling-bambora-api-client  # bamabora swagger for client generation
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

| Name                | Description                                  | Doc                                                      |
| ------------------- | -------------------------------------------- | -------------------------------------------------------- |
| backend             | all server side services                     | [README](src/backend/README.md)                          |
| efiling-api         | the main api for interating with the service | [README](src/backend/efiling-api/README.md)              |
| efiling-graphql     | the efiling-hub graphql for fast access      | [README](src/backend/efiling-graphql/README.md)          |
| efiling-cso-starter | soap client implementations                  | [README](src/backend/libs/efiling-cso-starter/README.md) |
| frontend            | all client side applications                 | [README](src/frontend/README.md)                         |
| efiling-frontend    | the frontend for uploading documents         | [README](src/frontend/efiling-frontend/README.md)        |
| efiling-demo        | the frontend for demo application            | [README](src/frontend/efiling-demo/README.md)            |
| cucumber-tests      | automated tests for frontend and backend     | [README](tests/README.md)                                |

## Running the App

By default a demo mode is enabled.

First create a local `.env` at the root of the repository based off [.env.template](.env.template). Below are the variables that need to be configured to get the application running in demo mode.

| Variable Name                   | Description                                                      | Example                    |
| ------------------------------- | ---------------------------------------------------------------- | -------------------------- |
| MVN_PROFILE                     | Set the front end application to be in demo mode or default mode | demo or default            |
| SERVER_PORT                     | Port that the API will run on                                    | 8080                       |
| KEYCLOAK_SSL_REQUIRED           | Configure whether to use SSL when communicating with Keycloak    | none                       |
| KEYCLOAK_RESOURCE               | The Keycloak resource that is used by the API                    | efiling-api                |
| KEYCLOAK_URL                    | The auth URL for Keycloak                                        | http://localhost:8081/auth |
| KEYCLOAK_REALM                  | The realm configured for your Keycloak instance                  | SpringBootKeycloak         |
| KEYCLOAK_CREDENTIALS_SECRET     | The secret generated in your Keycloak instance                   |                            |
| KEYCLOAK_AUTH_SERVER_URL        | The auth server URL for Keycloak                                 | http://localhost:8081/auth |
| BAMBORA_APIPASSCODE             | Passcode required to use the Bambora API                         |                            |
| BAMBORA_MERCHANTID              | Unique identifier requried to use the Bambora API                |                            |
| BAMBORA_PROFILE_URL             | URL of the Bambora profile                                       |                            |
| BAMBORA_HASHKEY                 | Specific key used with the merchant ID for generating links      |                            |
| BAMBORA_PROFILE_SERVICE_VERSION | Bambora profile service version                                  | 1.0                        |
| BAMBORA_URL_EXPIRY              | Bambora URL expiry time in minutes                               | 30                         |
| SFTP_KNOWNHOSTS                 | The directory with your knownhosts file                          | /users/YOURNAME/.ssh/      |
| SFTP_REMOTELOCATION             | Remote SFTP directory directory                                  | directory                  |
| SFTP_PRIVATE_KEY                | Private key for SFTP server                                      |                            |
| BCEID_SERVICE_URI               | URI for the BCEID service used by Keycloak                       |                            |
| BCEID_SERVICE_USERNAME          | Username for the BCEID service                                   |                            |
| BCEID_SERVICE_PASSWORD          | Password for the BCEID service                                   |                            |
| BCEID_SERVICE_ONLINE_SERVICE_ID | Unique service ID for BCEID online service                       |                            |

Configure Keycloak

```bash
docker-compose up -d --build keycloak
```

go to [Efiling-api credentials](http://localhost:8081/auth/admin/master/console/#/realms/SpringBootKeycloak/clients/b7fd5f2f-d047-4916-a35e-0f7c622dfb5d/credentials) and sign in. The default credentials to a new keycloak instance are:

    username: admin
    password: admin

After signing in, click Regenerate Secret. Copy the value of the secret to the `KEYCLOAK_CREDENTIALS_SECRET` in your `.env` file

Create a user [here](http://localhost:8081/auth/admin/master/console/#/create/user/SpringBootKeycloak)

Click on the Credentials tab

Set the password and set Temporary OFF

Click Reset Password

If you want to integrate with the CSO application change the `MVN_PROFILE` to `default` and fill out the other environment variables in the [.env.template](.env.template) file.

run

```bash
docker-compose up -d --build
```

To get started, access the front end application [here](http://localhost:3001) use the following username `bobross` and password `changeme`

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

#### keycloak:

A [keycloak](https://www.keycloak.org/) instance accessible at [http://localhost:8081/auth](http://localhost:8081/auth)

## Github action
