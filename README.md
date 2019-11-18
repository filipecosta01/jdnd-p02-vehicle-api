# VehiclesAPI Project

Project repository for JavaND Project 2, where students implement a Vehicles API using Java and Spring Boot that can communicate with separate location and pricing services.

## How to run

```mvn clean package``` for the maven projects:
- boogle-maps
- eurekaserver
- pricing-service
- vehicles-api

Run the JAR files in the following order:
1. eurekaserver
2. boogle-maps
3. pricing-service
4. vehicles-api

To verify that everything is running correctly, go to:
[Local Swagger-UI](http://localhost:8080/swagger-ui.html)

## Instructions

Check each component to see its details and instructions. Note that all three applications
should be running at once for full operation. Further instructions are available in the classroom.

- [Vehicles API](vehicles-api/README.md)
- [Pricing Service](pricing-service/README.md)
- [Boogle Maps](boogle-maps/README.md)

## Dependencies

The project requires the use of Maven and Spring Boot, along with Java v8.
