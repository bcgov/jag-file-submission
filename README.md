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

| Name | description | doc |
| --- | --- | --- |
| jag-efiling-api | the main api for interating with the service | [README](src/backend/jag-efiling-api/README.md) |
| efiling-frontend | the frontend for uploading documents | [README](src/efiling-frontend/README.md) |

## Docker

From the jag-file-submission directory, run ```docker-compose up```

Currently the docker image created will create a Redis container that the backend Spring Boot API interacts with.

To confirm that your docker container is working you can perform a GET request to ```http://localhost:8080/actuator``` to see all available endpoints, or
a GET request to ```http://localhost:8080/actuator/health``` to see the application status.