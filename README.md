# jag-file-submission
Generic File Submission API (to be used by the Family Law Act Application at first)

## Project Structure

    .
    ├── .github                             # Contains GitHub Related sources
    ├── openshift                           # openshift templates and pipeline
    ├── src/                                # application source files
    │   └── backend                         # backend applications
    │       └── jag-efiling-api             # efiling api 
    ├── COMPLIANCE.yaml                     # 
    ├── CONTRIBUTING.md                     # 
    ├── LICENSE                             # Apache License
    └── README.md                           # This file.


## Apps

| Name | description | doc |
| --- | --- | --- |
| jag-efiling-api | the main api for interating with the service | [README](src/backend/jag-efiling-api/README.md) |
