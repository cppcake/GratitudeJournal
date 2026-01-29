# GratitudeJournal

A web service to keep track of your well being and the things you are grateful for, written in Java using the [Spring Framework](https://spring.io/projects/spring-framework).

## Features

- [Fully documented API](https://cppcake.github.io/GratitudeJournal-Swagger-Page/) to track your well being and the things you are grateful for
- Support for multiple users
- Easy setup using Maven and Docker
- Unit-Tests for every Endpoint using Testcontainers

## Documentation

The API documentation can be found [here](https://cppcake.github.io/GratitudeJournal-Swagger-Page/). A webpage containing the javadoc documentation will be available soon.

## Requirements

The following software is necessary to build and run the service:
- Maven
- Java 25
- Docker

## Getting started

> Note:  First be sure to meet the [requirements](#requirements).

 First set the database password and root password. To do so it is recommended to create an `.env` file in the root directory of the project and populate it with the secret credentials of your choosing:

```
MYSQL_PASSWORD=PASSWORD-HERE
MYSQL_ROOT_PASSWORD=ROOT-PASSWORD-HERE
```

Afterwards the service can simply be started using

```sh
mvn spring-boot:run
```

## Spring components

The project depends on the following Spring components:

- Spring Web
- Spring HATEOAS
- Spring Data JPA
- MySQL Driver
- Docker Compose Support (to quickly set up a database)
- Testcontainers (for testing)
- HTTP Client (for testing)

## Testing

The tests can simply be run by using
 
```sh
mvn test
```

Thanks to [Testcontainers](#spring-components), the tests run inside their own container and do not interfere with the service or future tests.

## Security

It is strongly recommended to use this service only in a trusted environment as it lacks authentication as of now. This is because, apart from serving as a learning experience for me, the project is meant to be used locally.

## AI Usage

AI-Tools were only used to review the code and docs, retrieve information on how to use some components and for debugging purposes. The code is entirely written by Afeef Neiroukh and partly inspired by the [Spring Guides Collection](https://spring.io/guides), especially the guide [Building REST services with Spring](https://spring.io/guides/tutorials/rest).