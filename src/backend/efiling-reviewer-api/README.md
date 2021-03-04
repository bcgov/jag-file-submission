# efiling-reviewer-api

## Maven Profiles

### Openshift

Current image build in openshift runs with `openshift` profile by default.
This profile mostly enables splunk logging.

```bash
mvn install -P openshift
```

### Demo

This profile self isolate the application from any third individual dependencies

```bash
mvn install -P demo
```

## Configuration

You should use environment variables to configure the jag efiling api

| Environment Variable            | Type    | Description                                  | Notes                          |
| ------------------------------- | ------- | -------------------------------------------- | ------------------------------ |
| SERVER_PORT                     | Integer | web application server port                  | defaulted to `8080`            |
| DILIGEN_HEALTH_ENABLED:true}    | Boolean | if Diligen should be monitored on healt check| defaulted to `true`            |
| DILIGEN_BASE_PATH               | String  | Diligen api base path                        |                                |

## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
