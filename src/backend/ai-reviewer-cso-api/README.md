# ai-reviewer-cso-api

## Maven Profiles

Require to run locally:

```bash
mvn install -P ai-reviewer-cso
```

## Configuration

You should use environment variables to configure the jag ai-reviewer-cso-api

| Environment Variable            				  | Type    | Description                                  | Notes                          |
| ----------------------------------------------- | ------- | -------------------------------------------- | ------------------------------ |
| SERVER_PORT                     				  | Integer | web application server port                  | defaulted to `8080`            |


## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
