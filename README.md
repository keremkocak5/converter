# :rowboat: :car: File Converter :bullettrain_front: :bike: :helicopter: 

The **GiftAndGo Converter Service** is a Spring Boot application that accepts a transport data file, validates its
content
*and* the caller’s IP address, and returns a processed outcome file when all validations succeed. Smooth, safe, and fast
⚡

---

## 🚀 Starting the App

Make sure you have **Java 17** and **Maven** installed.

From the project root (where `pom.xml` lives), run:

```bash
mvn spring-boot:run
```

Easy as that 😄

---

## 🌐 Accessing the Application

### Swagger UI (enabled by default)

```
http://localhost:8080/converter/swagger-ui/index.html
```

### In‑Memory Database (H2 Console)

```
http://localhost:8080/converter/h2-console/
```

Credentials:

- **Username:** sa
- **Password:** *(leave empty)*

---

## 📡 API Specification

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

- Multipart field: `file` (`text/plain`)

**Successful Response (200 OK)** 🎉

- Converted file returned as an attachment
- Response header:

```
Content-Disposition: attachment; filename=OutcomeFile.json
```

---

## ❌ Error Responses

All errors follow **RFC 7807 – Problem Details for HTTP APIs**.

### Validation Error Example

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

### IP Restriction Example

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

### 🧾 Error Codes

| Code  | Description                       | HTTP |
|------|-----------------------------------|------|
| I0001 | ISP is restricted                | 403  |
| I0002 | Country is restricted            | 403  |
| I0010 | IP API connection error           | 500  |
| I0011 | IP API resolve error              | 500  |
| I0013 | Cannot read file                  | 400  |
| I0014 | Cannot write file                 | 500  |
| I0020 | Incorrect file format             | 400  |
| I0000 | Internal server error             | 500  |

---

## 🌍 IP Validation

- The application integrates with an **IP‑API client** for IP-based validation.
- Validation strategies are configurable via application properties:

```properties
feature.flag.ip.validation.strategies=IpValidateByIsp,IpValidateByCountry
```

- If no strategies are configured, **the IP‑API call is skipped entirely** 🚀
- For local development, IP validations are disabled by default.
- To replace the IP‑API provider, simply implement `IpDetailsRetrievable`.
- To add new IP rules, create a new validator implementing `Validatable`.

---

## 📄 File Validation

- Enable or disable file validation using:

```properties
feature.flag.file.validation.enabled=true
```

- Validation rules are defined in the `TransportFileValidator` enum.
- Output structure is controlled by `TransportFileReaderService`.
- To support new file types, extend `FileReaderServiceTemplate`.

### 📑 File Validation Rules

Each line must contain **7 pipe (`|`)‑delimited columns**:

| Column | Name        | Rule |
|------|-------------|------|
| 0 | UUID | Valid UUID |
| 1 | ID | Regex pattern |
| 2 | Name | Not empty, < 100 chars |
| 3 | Likes | Not empty, < 100 chars |
| 4 | Transport | Not empty, < 100 chars |
| 5 | Avg Speed | Double |
| 6 | Max Speed | Double |

📌 Validation stops at the **first failing rule per line**.

Additional notes:

- ID regex: `^(\d+)X\1D\d+$`
- Max file size: **1MB**
- Non‑ASCII characters are not validated (by design).

---

## 🧪 Testing

### Unit Tests

- Validators
- Factories
- Services
- Error handling

### Integration Tests

- End‑to‑end API flow
- File validation scenarios
- IP validation scenarios
- WireMock‑based IP‑API simulation 🧰

Run all tests:

```bash
mvn test
```

---

## 🤔 Assumptions

- Empty **country** or **ISP** values returned from IP‑API are *not* considered blockers.
- The system favors fast‑fail validation over collecting all possible errors.

---

## 🔧 Room for Improvement

- More WireMock scenarios for edge cases
- Optional accumulation of all validation errors (UX improvement)
- Docker / container support 🐳
- More precise execution timing metrics
- Support for additional file formats (CSV, JSON, etc.)
- Environment‑specific profiles (dev / uat / prod)
- Configurable retry strategy for external clients

---

Made with ☕, Spring Boot, and a bit of validation magic ✨
