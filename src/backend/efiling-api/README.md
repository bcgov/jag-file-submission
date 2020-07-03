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

| Environment Variable | Type | Description | Notes |
| --- | --- | --- | --- |
| SERVER_PORT | Integer | web application server port | defaulted to `8080` |
| DEMO_MODE | Boolean | A flag to turn on/off demo mode | defaulted to `false` |
| REDIS_HOST | String | Redis storage host | defaulted to `localhost` |
| REDIS_PORT | Integer | Redis storage port | defaulted to `6379` |
| REDIS_PASSWORD | String | Redis storage password | Defaulted to `admin` |
| NAVIGATION_BASE_URL | String | The base path of the secure file upload | not set by default |
| CACHE_TTL | Integer | The default time to live for the cache in ms | Defaulted to `600000` (10 min) |
| CSO_ACCOUNTFACADE_USERNAME | String | CSO account facade username | not set by default |
| CSO_ACCOUNTFACADE_PASSWORD | String | CSO account facade password | not set by default |
| CSO_ACCOUNTFACADE_URI | String | CSO account facade URI | not set by default |
| CSO_ROLEREGISTRY_USERNAME | String | role registry username | not set by default |
| CSO_ROLEREGISTRY_PASSWORD | String | role registry password | not set by default |
| CSO_ROLEREGISTRY_URI | String | role registry URI | not set by default |
| BCEID_LOOKUP_USERNAME | String | bceid lookup username | not set by default |
| BCEID_LOOKUP_PASSWORD | String | bceid lookup password | not set by default |
| BCEID_LOOKUP_URI | String | bceid lookup URI | not set by default |

## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
