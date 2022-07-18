# sociely-backend

Simple social media service.

## Demo

Demo is available at [vemu.ddns.net/sociely](https://vemu.ddns.net/sociely/)

## How to run

1. Generate jar file: `./mvnw clean package -P prod -DskipTests`.
2. Run application: `java -jar ./target/sociely-*.jar`.

Remember to provide `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
, `SPRING_DATASOURCE_URL` as Environment Variables. Make sure you have installed Java 17.

## Docker

1. Generate docker image: `./mvnw spring-boot:build-image -P prod -DskipTests`.
2. Provide DB_PASSWORD env.
3. Run docker compose.

## Develop

`./mvnw install` then `./mvnw spring-boot:run` and open page at [localhost](http://127.0.0.1:8080)

## Technologies used:

* Spring Boot
* Thymeleaf
* Postgresql/H2
* Bootstrap
* Alpine.js
* Lombok
