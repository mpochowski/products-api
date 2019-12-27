# Products REST API Service

Products API is a REST API server that provides methods to allow user register and purchase products.

The API runs using in-memory database.

### Quick start

Run from project main directory:

``mvn clean spring-boot:run``

Navigate in your browser to:

``http://localhost:8080/swagger-ui.html``

Use any endpoint using Swagger console, but please note:
- register endpoint is open for anyone
- add product endpoint requires ROLE_ADMIN
- there is initial admin user created with ROLE_ADMIN

To login using initial admin user type in following in popup login box:
1. username: ``1-godaddy``
2. password: ``pass``

Database console can be accessed via link:
``http://localhost:8080/h2-console``

### Features

1. Brand concept: each brand has it's own set of users and products; 
2. User concept:
- allow user to register within brand;
- allow user to login (basic auth) using format "brandId-username" and "password", for example "1-john" and "pass";
- allow user to retrieve their details;
3. Basket feature:
- users can create new baskets and retrieve basket by ID, one user can own many baskets;
- basket implements IN_PROCESS, IN_CHECKOUT and COMPLETED states;
- basket can freely move from IN_PROCESS to IN_CHECKOUT and other way around;
- once basket is COMPLETED, it cannot be move back to IN_PROCESS or IN_CHECKOUT;
- there is fake payment and product provisioning when basket is moved to COMPLETED;
- if basket is IN_PROCESS, products can be added and removed from it;
4. Products feature:
- users who own ROLE_ADMIN can define new products;
 
### Launch configuration details.

The project requires Java 13 installed.

All commands require bash console navigated to main project directory.

If maven is not installed, wrapper and scripts are provided to launch the project.

For example on Mac:

``sh mvnw [commands]``

From Windows CMD:

``./mvnw.cmd [commands]``

If maven is installed, the commands can be simplified using `mvn [commands]`.

To run project:

``mvn clean spring-boot:run``

**Once application is running, navigate to:** 
http://localhost:8080/swagger-ui.html

**Application should display Swagger (REST API framework) page, that exposes information about available endpoints and allows user to trigger endpoints from swagger page.**

To run all tests:

``mvn clean test``

### Project structure

The project consists of following components:

1. ``./.mvn`` - contains maven wrapper for running application without maven installed
2.  ``./src/main/java/com/godaddy/uk/productsapi/configurations`` - contains server configuration beans
3.  ``./src/main/java/com/godaddy/uk/productsapi/controllers`` - contains server controllers (entry points)
4.  ``./src/main/java/com/godaddy/uk/productsapi/exceptions`` - contains exceptions handled by the api
5.  ``./src/main/java/com/godaddy/uk/productsapi/models`` - contains application models
6.  ``./src/main/java/com/godaddy/uk/productsapi/models/api`` - contains models that are exposed via api
7.  ``./src/main/java/com/godaddy/uk/productsapi/models/database`` - contains database models (entities)
8.  ``./src/main/java/com/godaddy/uk/productsapi/repositories`` - contains repositories interfaces to access database
9.  ``./src/main/java/com/godaddy/uk/productsapi/services`` - contains application business logic
10.  ``./src/main/java/com/godaddy/uk/productsapi/transformers`` - contains utilities to convert between database and api models
11.  ``./src/main/java/com/godaddy/uk/productsapi/utilities`` - contains general utilities not related to business logic 
12.  ``./src/main/java/com/godaddy/uk/productsapi/ProductsApiApplication.java`` - application entry point
13.  ``./src/main/java/resources/application.yml`` - defines application properties
14.  ``./src/main/java/resources/data.sql`` - defines application initial data
15.  ``./src/main/test/resources/features`` - contains cucumber tests written using gherkin
16.  ``./src/main/test/resources/data.sql`` - contains initial data for tests 
17.  ``./src/main/test/com/godaddy/uk/productsapi/cucumber`` - contains cucumber step definitions, configuration and utilities (glue) such as http client
18. ``./src/test/java/com/godaddy/uk/productsapi/services`` - contains unit tests for business logic
19. ``./src/test/java/com/godaddy/uk/productsapi/utilities`` - contains unit tests for utilities
 

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/maven-plugin/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)