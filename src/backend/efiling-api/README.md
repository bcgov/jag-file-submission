# efiling-api

## Maven Profiles

### Openshift

Current image build in openshift runs with `openshift` profile by default.
This profile mostly enables splunk logging.

```bash
mvn install -P openshift
```

### Demo

This profile self isolate the application from any third party dependencies

```bash
mvn install -P demo
```

## Configuration

You should use environment variables to configure the jag efiling api

| Environment Variable        | Type    | Description                                  | Notes                          |
| --------------------------- | ------- | -------------------------------------------- | ------------------------------ |
| SERVER_PORT                 | Integer | web application server port                  | defaulted to `8080`            |
| DEMO_MODE                   | Boolean | A flag to turn on/off demo mode              | defaulted to `false`           |
| REDIS_HOST                  | String  | Redis storage host                           | defaulted to `localhost`       |
| REDIS_PORT                  | Integer | Redis storage port                           | defaulted to `6379`            |
| REDIS_PASSWORD              | String  | Redis storage password                       | Defaulted to `admin`           |
| NAVIGATION_BASE_URL         | String  | The base path of the secure file upload      | not set by default             |
| CACHE_TTL                   | Integer | The default time to live for the cache in ms | Defaulted to `600000` (10 min) |
| CSO_ACCOUNTFACADE_USERNAME  | String  | CSO account facade username                  | not set by default             |
| CSO_ACCOUNTFACADE_PASSWORD  | String  | CSO account facade password                  | not set by default             |
| CSO_ACCOUNTFACADE_URI       | String  | CSO account facade URI                       | not set by default             |
| CSO_ROLEREGISTRY_USERNAME   | String  | role registry username                       | not set by default             |
| CSO_ROLEREGISTRY_PASSWORD   | String  | role registry password                       | not set by default             |
| CSO_ROLEREGISTRY_URI        | String  | role registry URI                            | not set by default             |
| FILING_STATSFACADE_USERNAME | String  | status facade username                       | not set by default             |
| FILING_STATSFACADE_PASSWORD | String  | status facade password                       | not set by default             |
| FILING_STATSFACADE_URI      | String  | status facade URI                            | not set by default             |
| CSOWS_USERNAME              | String  | csows username                               | not set by default             |
| CSOWS_PASSWORD              | String  | csows password                               | not set by default             |
| CSOWS_URI                   | String  | csows URI                                    | not set by default             |
| FILING_FACADE_USERNAME      | String  | filing facade username                       | not set by default             |
| FILING_FACADE_PASSWORD      | String  | filing facade password                       | not set by default             |
| FILING_FACADE_URI           | String  | filing facade URI                            | not set by default             |
| CSO_SERVICEFACADE_USERNAME  | String  | service facade username                      | not set by default             |
| CSO_SERVICEFACADE_PASSWORD  | String  | service facade password                      | not set by default             |
| CSO_SERVICEFACADE_URI       | String  | service facade URI                           | not set by default             |
| BCEID_LOOKUP_USERNAME       | String  | bceid lookup username                        | not set by default             |
| BCEID_LOOKUP_PASSWORD       | String  | bceid lookup password                        | not set by default             |
| BCEID_LOOKUP_URI            | String  | bceid lookup URI                             | not set by default             |
| KEYCLOAK_AUTH_SERVER_URL    | String  | The keycloak auth server URL                 | not set by default             |
| KEYCLOAK_REALM              | String  | The keycloak realm name                      | not set by default             |
| KEYCLOAK_RESOURCE           | String  | The keycloak resource name                   | not set by default             |
| KEYCLOAK_CREDENTIALS_SECRET | String  | The keycloak Credentials Secrets             | not set by default             |

## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
