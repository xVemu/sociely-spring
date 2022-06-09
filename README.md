# sociely-backend
Simple social media service.

## Demo
Demo is avaible at [sociely.herkouapp](https://sociely.herokuapp.com/)

## How to run
Download latest [jar](https://github.com/xVemu/sociely/releases/latest/download/sociely.jar) file from releases and run it `java -jar sociely.jar`. Remember to provide `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME`, `JDBC_DATABASE_PASSWORD` as Environment Variables. Make sure you have installed JRE 17.

## Develop
`./mvnw install` then `./mvnw spring-boot:run` and open page at [localhost](http://127.0.0.1:8080)

## Technologies used:
* Spring Boot
* Thymeleaf
* Postgresql/H2
* Bootstrap
* Alpine.js
* Lombok
