# Financial Service Application

This Financial Service Application is a Spring Boot application that simulates financial operations like managing accounts, performing transactions, and converting currencies using real-time exchange rates from an external API.
It uses an in-memory H2 database and Caching mechanism.

## Technologies and Methods Used

- **Java 17**
- **Spring Boot**: Simplifies the bootstrapping and development of new Spring applications.
- **Maven**: Dependency management and project build tool.
- **H2 Database**: In-memory database for rapid development and testing.
- **JPA (Hibernate)**: Java Persistence API using Hibernate to interact with the database.
- **Spring Data JPA**: Repository abstractions for data access.
- **Spring Web MVC**: Model-View-Controller architecture for building web applications.
- **RestTemplate**: Synchronous client to perform HTTP requests.
- **Caffeine Cache**: High performance caching library for Java.
- **Spring Security**: Authentication and access-control framework.
- **Lombok**: Reduces boilerplate code.
- **JUnit and Mockito**: Testing frameworks for unit tests.

## Setting Up and Running the Application

### Prerequisites

- **Java JDK 17**: Ensure Java 17 is installed by running `java -version`.
- **Maven**: Ensure Maven is installed by running `mvn -v`.

### Database Setup

The application uses an H2 in-memory database:

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User Name**: `sa`
- **Password**: (empty)

