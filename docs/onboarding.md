# Getting Started - Onboarding

## Request Keycloak Client

Create a Github [issue](https://github.com/bcgov/jag-file-submission/issues/new?assignees=akroon3r%2C+alexjoybc&labels=Onboarding&template=onboarding-request.md&title=%5BONBOARDING%5D) to begin the onboarding process to our application.

On completion of the Github issue, the user will be provided [OpenId Connect](https://openid.net/connect/) information such  as *client_id*, *realm*, *client_secret*. All of which are required to be kept secured at all times.

## Request Keycloak Token

First, users will want to POST against the following URL: ${keycloakBaseUrl}/realms/${keycloakRealm}/protocol/openid-connect/token

- ${keycloakBaseUrl} will be the value https://sso-${ENVIRONMENT}.pathfinder.gov.bc.ca/auth, where ${ENVIRONMENT} is either dev, test or prod
- ${keycloakRealm} will be provided to you by the admin after the keycloak client request has been fufilled

In the request body, the following information will be required

Example body: client_id=${keycloakClientId}&grant_type=client_credentials&client_secret=${keycloakClientSecret}

- client_id : Provided to the user by the admin after the keycloak client request has been fufilled
- grant_type : value will always be `client_credentials`
- client_secret : Provided to the user by the admin after the keycloak client request has been fufilled

Once the request for the keycloak token has been completed successfully, the user can begin to interface with the Efiling Hub [API](https://editor.swagger.io/?url=https://raw.githubusercontent.com/bcgov/jag-file-submission/master/src/backend/efiling-api/jag-efiling-api.yaml) and Frontend Applications. The two API endpoints that a parent application will interface with are the `/submission/documents` and `/submission/${submissionId}/generateUrl`.

The following is a sample curl command for generating a token from any keycloak environment:

``` bash
curl --location --request POST 'https://[keycloak auth endpoint]/auth/realms/[keycloak-realm]/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=[your client id]' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'client_secret=[your client secret]'
```
