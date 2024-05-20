# Financial Service Application

This Financial Service Application is a Spring Boot application that simulates financial operations like managing accounts, 
performing transactions, and converting currencies using real-time exchange rates from an external [EXCHANGE RATES API](https://apilayer.com/marketplace/exchangerates_data-api)

## Exchange Rates API
External API /latest endpoint is used for exchange rate retrieval.
The endpoint returns exchange rate data in real-time for all available currencies or for a specific set.
Provide your API KEY to `exchange.rate.api=` in the [application.properties](https://github.com/AnzelikaGorbaca/fintech/blob/master/financial-service/src/main/resources/application.properties) file

## Main Technologies and Methods Used

- **Java 17:** Core programming language.
- **Spring Boot:** Framework used for building the RESTful Web Service.
- **Maven:** Dependency management and project build tool.
- **H2 Database:** In-memory database for development and testing purposes.
- **Hibernate:** ORM tool for database interaction.
- **Lombok:** Library to reduce boilerplate code.
- **JaCoCo:** Java Code Coverage library.
- **Spring Data JPA**: Repository abstractions for data access.
- **Spring Web MVC**: Model-View-Controller architecture for building web applications.
- **Retry Mechanism:** Using Spring Retry to handle transient errors when calling external services.
- **RestTemplate**: Synchronous client to perform HTTP requests.
- **Caffeine Cache**: High-performance caching library for Java.
- **Lombok**: Reduces boilerplate code.
- **JUnit 5:** Framework used for unit and integration testing.
- **Mockito:** Mocking framework for unit tests.


## API Usage

Postman collection is attached to test the API endpoints. 
You can import this collection into Postman to interact with the API.

### Using the Postman Collection

1. Download and install [Postman](https://www.postman.com/downloads/).
2. Open Postman and import the collection:
   - Click on `Import`.
   - Choose the [postman/Financial_Service_API_Collection.json](https://github.com/AnzelikaGorbaca/fintech/blob/master/financial-service/postman/Financial_Service_API_Collection.json) from this repository.
   - You should now see the collection in your Postman sidebar.

## Pre-configured SQL Data Script

The application uses an `H2` in-memory database initialized with a set of predefined SQL scripts. 
These scripts populate the database with sample data when the application starts up.
Data includes clients, accounts, and transactions used for demonstration and for running sample requests.

## Setting Up and Running the Application

For setting up and running the application please refer to service [README.md](https://github.com/AnzelikaGorbaca/fintech/blob/master/financial-service/README.md)


