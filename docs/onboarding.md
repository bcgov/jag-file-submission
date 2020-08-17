# Onboarding

## Request Keycloak Client

- contact admin against repo for client creation
- provide secret and URL to requestor (KEY MUST BE KEPT PRIVATE)

## Request Keycloak Token

First, users will want to POST against the following URL: ${keycloakBaseUrl}/realms/${keycloakRealm}/protocol/openid-connect/token

- ${keycloakBaseUrl} will be the value https://sso-${ENVIRONEMENT}.pathfinder.gov.bc.ca/auth , where ${ENVIRONMENT} is either dev, test or prod
- ${keycloakRealm} will be provided to you by the admin after the keycloak client request has been fufilled

In the request body, the following information will be required

Example body: client_id=${keycloakClientId}&grant_type=client_credentials&client_secret=${keycloakClientSecret}

- client_id : Provided to the user by the admin after the keycloak client request has been fufilled
- grant_type : value will always be `client_credentials`
- client_secret : Provided to the user by the admin after the keycloak client request has been fufilled

Once the request for the keycloak token has been completed successfully, the user can begin to interface with the Efiling Hub [API](https://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/efiling-api/jag-efiling-api.yaml) and Frontend Applications 
