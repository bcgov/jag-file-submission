FROM ubuntu:18.04

ARG KEYCLOAK_URL
ARG DOCKERIZE_VERSION=v0.6.1

ENV KEYCLOAK_URL=$KEYCLOAK_URL

RUN apt-get update && \
    apt-get install -y curl jq wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists

RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz && \
    tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz && \
    rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY createuser.sh /tmp/createuser.sh
RUN chmod +x /tmp/createuser.sh

CMD ["/tmp/createuser.sh"]
