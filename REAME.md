# Account Services REST API Exercise

## Table of contents
* [General Info](#general-info)
* [Technologies](#technologies)
* [Notes](#implementation-notes)
* [References](#references)

## General Info
These APIs expose account services that will enable the consumer to create, access and update accounts in addition to transfer funds between them.



## Technologies
Project is created with:
* Java
* Eclipse
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Spring HATEOAS
* H2
* JUnit 5
* Mockito
* Git

## Implementation notes:
- Ideally the account number field should have had a creation pattern that considers factors like; customer Unique Number, account type, account currency, account sequence, ect, 
and should have been different from the ID field, but for DEMO purposes this has been rules out.
- The exposed APIs are:
  - see all accounts at *get(/accounts)*
  - see an account at *get(/accounts/{accountNumber})*
  - create an account at *post(/accounts)*
  - update an account at *put(/accounts/{accountNumber})*
  - transfer between accounts at *post(/transfer)*
- The transfer between account results into a new transaction posted in the transaction table, Ideally the transaction would have more variable to discribe it, this includes but not limited to; transaction code, transaction narratives, transaction reference number, etc.
- transfers are perform at *post(/transfers)*, passing as argument a 'TransferBetweenAccountsReq' in json
- when trying an illegal transaction or when trying to use an account that doesn't exist, the system returns an error message in json format
- The different currency concept including exchange rates has been ruled out. 

## Copyright

- **year**: 2021
- **project name**: accountservices
- **author**: Muhammad Attia
- **mail**: ghandour_bme@hotmail.com

## References
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Unit Testing with Spring Boot](https://reflectoring.io/unit-testing-spring-boot/)
- [Testing MVC Web Controllers with Spring Boot and @WebMvcTest](https://reflectoring.io/spring-boot-web-controller-test/)
- [Testing JPA Queries with Spring Boot and @DataJpaTest](https://reflectoring.io/spring-boot-data-jpa-test/)
- [Integration Tests with Spring Boot and @SpringBootTest](https://reflectoring.io/spring-boot-test/)
- [Mocking with (and without) Spring Boot](https://reflectoring.io/spring-boot-mock/)
- [Testing a HATEOAS service](https://lankydan.dev/2017/09/18/testing-a-hateoas-service)
- [Global exception handling with @ControllerAdvice](https://lankydan.dev/2017/09/12/global-exception-handling-with-controlleradvice)

