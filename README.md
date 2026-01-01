# File Converter

The GiftAndGo Converter Service is a Spring Boot application that accepts a transport data file, validates its content
and the caller’s IP address, and returns a processed outcome file when all validations succeed. :ship:

## Starting the app

Make sure you have Java 17 and Maven installed on your device. Then run the following command at the root folder (where
pom.xml resides) of your project.

```
mvn spring-boot:run
```

## Accessing the APIs

#### Swagger is enabled: 🚀

http://localhost:8080/converter/swagger-ui/index.html

#### In-Memory Relational Database endpoint:

http://localhost:8080/converter/h2-console/

username: sa

password: leave empty

## Assumptions

- If empty Country or Isp are returned from the IP-API client, this will not be considered a blocker, and validations
  will pass.
- Max file size 1MB.
- Name, Likes or Transport fields in the file cannot be empty nor greater than 100 characters. No validations for
  non-ISO-8 characters applied for the sake of simplicity.
- ID field regex is "^(\\d+)X\\1D\\d+$" .

## Room for Improvement

- More wiremock tests could be added for different exception case combinations
- The app stops processing the file as soon as a invalid record is found. This helps thread and memory economy, since
  unnecessary resources are not consumed for a possibly unlimited number of problematic records in the file. However,
  this may not be desireable from UX standpoint.
- No containerization included.
- Execution time is logged in the db, tough more precise chronographs may also be considered.