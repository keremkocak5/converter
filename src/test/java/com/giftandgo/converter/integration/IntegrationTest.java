package com.giftandgo.converter.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest extends TestBase {

    @Test
    public void shouldSucceedWhenFileDoesNotHaveErrors() {
        stubFor(get(urlMatching("/json")).willReturn(okJson("{\"status\":\"ok\"}")));

        given()
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
        // kerem buraya kontrol ekle
        // kerem empty line validatorunu kaldir
    }

    @Test
    public void shouldSucceedWhenFileDoesNotHaveErrorsButEmptyLine() {
        stubFor(get(urlMatching("/json")).willReturn(okJson("{\"status\":\"ok\"}")));

        given()
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-success-empty-line-file.txt"),
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
    public void shouldSucceedWhenFileDoesNotHaveErrorsButEmptyFile() {
        stubFor(get(urlMatching("/json")).willReturn(okJson("{\"status\":\"ok\"}")));

        given()
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("empty-file.txt"),
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
    public void shouldNotSucceedWhenUUIDInvalid() {
        stubFor(get(urlMatching("/json")).willReturn(okJson("{\"status\":\"ok\"}")));

        given()
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-uuid-invalid-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("detail", equalTo("UUID not valid at line 3"))
                .body("errorCode", equalTo("I0021"));
    }

    @Test
    public void shouldNotSucceedWhenIDInvalid() {
        stubFor(get(urlMatching("/json")).willReturn(okJson("{\"status\":\"ok\"}")));

        given()
                .multiPart(
                        "file",
                        "input.txt",
                        readFile("transport-id-invalid-file.txt"),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/converter/v1/file")
                .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("detail", equalTo("ID not valid at line 2"))
                .body("errorCode", equalTo("I0022"));
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