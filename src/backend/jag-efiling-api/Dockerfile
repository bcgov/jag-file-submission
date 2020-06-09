#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.6.3-jdk-8 as build

ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=

ARG MVN_PROFILES
ARG SERVICE_NAME

ENV SERVICE_NAME=${SERVICE_NAME}
ENV MVN_PROFILES=${MVN_PROFILES}

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

RUN mvn -B clean package \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT} \
        -P ${MVN_PROFILES} 

#############################################################################################

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM openjdk:8-jdk-slim

# ARG MVN_PROFILES
ARG SERVICE_NAME

COPY --from=build ./target/${SERVICE_NAME}-*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
