package com.giftandgo.converter.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("wiremock")
public class IpValidationIntegrationTest extends TestBase {

    @Test
    public void shouldSucceedWhenIpDoesNotHaveRestrictions() {
        given()
                .header("X-Forwarded-For", "1.1.1.1")
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(200)
                .header("Content-Disposition", notNullValue());
    }

    @Test
    public void shouldFailWhenIpCountryBlocked() {
        given()
                .header("X-Forwarded-For", "2.2.2.2")
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(403)
                .contentType("application/problem+json")
                .body("title", equalTo("Country is restricted."))
                .body("errorCode", equalTo("I0002"));
    }

    @Test
    public void shouldFailWhenIpISPBlocked() {
        given()
                .header("X-Forwarded-For", "3.3.3.3")
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(403)
                .contentType("application/problem+json")
                .body("title", equalTo("ISP is restricted."))
                .body("errorCode", equalTo("I0001"));
    }

    @Test
    public void shouldFailWhenResultNotSuccess() {
        given()
                .header("X-Forwarded-For", "4.4.4.4")
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(500)
                .contentType("application/problem+json")
                .body("title", equalTo("IP API could not resolve arguments."))
                .body("errorCode", equalTo("I0011"));
    }


    @Test
    public void shouldSucceedWhenCountryNull() {
        given()
                .header("X-Forwarded-For", "5.5.5.5")
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(403)
                .contentType("application/problem+json")
                .body("title", equalTo("ISP is restricted."))
                .body("errorCode", equalTo("I0001"));
    }

    @Test
    public void shouldFailWhenConnectionFail() {
        given()
                .header("X-Forwarded-For", "6.6.6.6")
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(500)
                .contentType("application/problem+json")
                .body("title", equalTo("Could not connect to IP-API."))
                .body("errorCode", equalTo("I0010"));
    }

    private static byte[] readFile(String filename) {
        byte[] fileContent;
        try {
            try (InputStream is = new ClassPathResource(filename).getInputStream()) {
                fileContent = is.readAllBytes();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }
}