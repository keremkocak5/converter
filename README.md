# File Converter

The GiftAndGo Converter Service is a Spring Boot application that accepts a transport data file, validates its content
and the caller’s IP address, and returns a processed outcome file when all validations succeed. :ship:

## Starting the app

Make sure you have Java 17 and Maven installed on your device. Then run the following command at the root folder (where
pom.xml resides) of your project.

```bash
mvn spring-boot:run
```

## Accessing the Application

#### Swagger is enabled: 🚀

http://localhost:8080/converter/swagger-ui/index.html

#### In-Memory Relational Database endpoint:

http://localhost:8080/converter/h2-console/

username: sa

password: leave empty

## API Specification

### Convert File

**Endpoint**

```
POST /converter/v1/file
```

**Headers**

```
X-Forwarded-For: <client-ip>
Content-Type: multipart/form-data
```

**Request**

- Multipart field: `file` (text/plain)

**Response (200 OK)**

- Converted file as attachment
- Header:

```
Content-Disposition: attachment; filename=OutcomeFile.json
```

---

## Error Responses

All errors follow **RFC 7807 – Problem Details for HTTP APIs**.

### Example – Validation Error

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "UUID not valid at line 3",
  "instance": "/converter/v1/file",
  "errorCode": "I0020"
}
```

### Example – IP Restriction

```json
{
  "type": "about:blank",
  "title": "Forbidden",
  "status": 403,
  "detail": "ISP is restricted.",
  "instance": "/converter/v1/file",
  "errorCode": "I0001"
}
```

#### Error Codes

| Code  | Description                              | HTTP |
|------|------------------------------------------|------|
| I0001 | ISP is restricted                        | 403  |
| I0002 | Country is restricted                    | 403  |
| I0010 | IP API connection error                  | 500  |
| I0011 | IP API resolve error                     | 500  |
| I0013 | Cannot read file                         | 400  |
| I0014 | Cannot write file                        | 500  |
| I0020 | Incorrect file format                    | 400  |
| I0000 | Internal server error                    | 500  |

---

## IP Validation

- The app connects to IP-API client for IP related validations.
- IP validations can be enabled and disabled in a fine-grained fashion using application properties: simply add the
  validation names to feature.flag.ip.validation.strategies property.
  eg:
   ```
   feature.flag.ip.validation.strategies=ValidateISPStrategy,ValidateCountryStrategy
   ```
- For local testing purposes, the IP validations are disabled (but can be enabled as described above).
- If no validations are set, the app will skip IP-API call.
- If a new IP validation service will be introduced to replace IP-API, please create a new package-private client which
  implements IpDetailsRetrievable.
- If new IP validation rules to be introduced, all you have to do is to create a new validator which implements
  Validatable interface; please check "validator" package for examples.

## File Validation

- File validations can be enabled and disabled by setting feature.flag.file.validation.enabled property true or false:
  eg:
   ```
  feature.flag.file.validation.enabled=true
   ```
- If existing columns are changed, or new columns are introduced to TransportationFile template, TransportFileValidator
  enum should be edited to reflect those changes.
- If the structure of the output file has to be changed, please edit TransportFileReaderService class.
- If a new file type, such as, say, FavoriteMusicBands is required to be processed, create a new FileReader service
  which implements FileReaderServiceTemplate.

#### File Validation Rules

Each line must contain **7 pipe-delimited columns**:

| Column | Name         | Validator |
|------|--------------|-----------|
| 0 | UUID | UUIDValidator |
| 1 | ID | Regex pattern |
| 2 | Name | Not empty, < 100 |
| 3 | Likes | Not empty, < 100 |
| 4 | Transport | Not empty, < 100 |
| 5 | Avg Speed | Double |
| 6 | Max Speed | Double |

Validation stops at the **first failing rule per line**.

- ID field regex is "^(\\d+)X\\1D\\d+$" .
- Max file size 1MB.
- No validations for non-ISO-8 characters applied for the sake of simplicity.

## Testing

### Unit Tests

- Validators
- Factories
- Services
- Error handling

### Integration Tests

- Full API flow
- File validation scenarios
- IP validation scenarios
- WireMock-based IP API simulation

Run tests:

```bash
mvn test
```

## Assumptions

- If empty Country or Isp are returned from the IP-API client, this will not be considered a blocker, and validations
  will pass.

## Room for Improvement

- More wiremock tests could be added for different exception case combinations
- The app stops processing the file as soon as a invalid record is found. This helps thread and memory economy, since
  unnecessary resources are not consumed for a possibly unlimited number of problematic records in the file. However,
  this may not be desireable from UX standpoint.
- No containerization included.
- Execution time is logged in the db, tough more precise chronographs may also be considered.
- This app supports delimited text files. Different file types such as CSV will require further abstractions.
- No profiles such as dev, uat or PROD are specified for the sake of simplicity.
- Retry mechanism for the client can be made parametric (avoided for the sake of simplicity).
