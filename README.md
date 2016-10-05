# Spring demo application

## Running Locally

* Start up the Service using the following command

```
./gradlew bootRun
```

* A very basic UI to exercise the API is available at http://localhost:8080

* This repo is primarily for testing out the [Jenkins pipeline](https://github.com/iflowfor8hours/jenkins2-pipeline-demo) that uses it, very little else useful.

* Run inside a docker container using the following:

```
./mvnw package -DskipTests
docker run -v $HOME/src/sample-spring-cloud-svc-ci:/opt/repo  -p 8080:8080 openjdk:latest java -jar /opt/repo/target/sample-spring-cloud-svc-ci-1.0.0-SNAPSHOT.jar
```
