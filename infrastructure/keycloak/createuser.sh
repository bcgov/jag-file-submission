#!/bin/bash

sleep 1m

KEYCLOAK_REALM_URL="$KEYCLOAK_URL/auth/admin/realms/Efiling-Hub"
USERNAME="bobross"
ADMIN_GROUP="efiling-admin"
EARLY_GROUP="efiling-early-adopters"

# Get admin user token
ADMIN_TOKEN=$(curl -s --location --request POST "$KEYCLOAK_URL/auth/realms/master/protocol/openid-connect/token" \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --header 'Cookie: 8a49b93d4f41531bc1d1ef85f2b65254=2fb8f4c2fe6843d186204f21cc45dd4b' \
    --data-urlencode 'client_id=admin-cli' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'username=admin' \
    --data-urlencode 'password=admin' | jq -r '.access_token')

if [ -z "$ADMIN_TOKEN" ]
then
    echo "Error connecting to keycloak"
    exit 1
fi

echo "Successfully connected to keycloak"

# Create efiling-hub user
CREATE_USER_STATUS=$(curl -s -o /dev/null -w '%{http_code}' \
    --request POST "$KEYCLOAK_REALM_URL/users" \
    --header "Authorization: Bearer $ADMIN_TOKEN" \
    --header 'Content-Type: application/json' \
    --data-raw '{
                    "firstName": "bob",
                    "lastName": "ross",
                    "email": "bobross@paintit.com",
                    "enabled": "true",
                    "username": "bobross",
                    "attributes": {
                        "universal-id": ["77da92db-0791-491e-8c58-1a969e67d2fa"]
                    },
                    "credentials": [
                        {
                            "type": "password",
                            "temporary": false,
                            "value": "changeme"
                        }
                    ]}')

if [ $CREATE_USER_STATUS -eq 201 ]; then
    echo "user created"
elif [ $CREATE_USER_STATUS -eq 409 ]; then
    echo "user already exists"
else
    echo "Error connecting to keycloak"
    exit 2
fi


# Get user id
USER_ID=$(curl -s --location --request GET "$KEYCLOAK_REALM_URL/users?username=$USERNAME" \
--header "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id') 

if [ -z "$USER_ID" ]; then
    echo "user bob ross does not exists"
    exit 3
fi

# Get group id
ADMIN_GROUP_ID=$(curl -s --location --request GET "$KEYCLOAK_REALM_URL/groups?search=$ADMIN_GROUP" \
--header "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id') 

if [ -z "$ADMIN_GROUP_ID" ]; then
    echo "admin group id does not exist"
    exit 4
fi

# Add user to efiling-admin group
ADD_TO_ADMIN_STATUS=$(curl -s -o /dev/null -w '%{http_code}' \
    --request PUT "$KEYCLOAK_REALM_URL/users/$USER_ID/groups/$ADMIN_GROUP_ID" \
    --header "Authorization: Bearer $ADMIN_TOKEN" \
    --header 'Content-Type: application/json')

if [ $ADD_TO_ADMIN_STATUS -eq 204 ]; then
    echo "user added to admin group"
else
    echo "Error adding user to admin group"
    exit 5
fi

# Get early-adopters id
EARLY_GROUP_ID=$(curl -s --location --request GET "$KEYCLOAK_REALM_URL/groups?search=$EARLY_GROUP" \
--header "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id') 

if [ -z "$EARLY_GROUP_ID" ]; then
    echo "early adopters group id does not exist"
    exit 4
fi

# Add user to efiling-early-adopters group
ADD_TO_EARLY_STATUS=$(curl -s -o /dev/null -w '%{http_code}' \
    --request PUT "$KEYCLOAK_REALM_URL/users/$USER_ID/groups/$EARLY_GROUP_ID" \
    --header "Authorization: Bearer $ADMIN_TOKEN" \
    --header 'Content-Type: application/json')

if [ $ADD_TO_EARLY_STATUS -eq 204 ]; then
    echo "user added to efiling-early-adopters group"
else
    echo "Error adding user to efiling-early-adopters group"
    exit 5
fi

exit 0
