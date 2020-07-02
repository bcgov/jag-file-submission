#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.6.3-jdk-8 as build

ARG MVN_PROFILE
ARG SERVICE_NAME

ENV SERVICE_NAME=${SERVICE_NAME}

COPY . .

RUN mvn -B clean package \
        -P libs

WORKDIR ${SERVICE_NAME}

RUN mvn -B clean package \
        -P ${MVN_PROFILE}

#############################################################################################

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM openjdk:8-jdk-slim

# ARG MVN_PROFILES
ARG SERVICE_NAME

COPY --from=build ./${SERVICE_NAME}/target/${SERVICE_NAME}-*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
