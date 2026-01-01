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
public class FileValidationIntegrationTest extends TestBase {

    private static final String ENDPOINT = "/converter/v1/file";
    private static final String IP = "1.1.1.1";
    private static final String FILE_NAME = "input.txt";

    @Test
    public void shouldSucceedWhenFileDoesNotHaveErrors() {
        postFile("transport-success-file.txt")
                .then()
                .statusCode(200)
                .header("Content-Disposition", notNullValue());
    }

    @Test
    public void shouldSucceedWhenFileHasEmptyLine() {
        postFile("transport-success-empty-line-file.txt")
                .then()
                .statusCode(200)
                .header("Content-Disposition", notNullValue());
    }

    @Test
    public void shouldSucceedWhenFileIsEmpty() {
        postFile("empty-file.txt")
                .then()
                .statusCode(200)
                .header("Content-Disposition", notNullValue());
    }

    @Test
    public void shouldFailWhenUUIDInvalid() {
        assertInvalidFile(
                "transport-uuid-invalid-file.txt",
                "UUID not valid at line 3"
        );
    }

    @Test
    public void shouldFailWhenIDInvalid() {
        assertInvalidFile(
                "transport-id-invalid-file.txt",
                "Id not valid at line 2"
        );
    }

    @Test
    public void shouldFailWhenAvgSpeedInvalid() {
        assertInvalidFile(
                "transport-avg-speed-invalid-file.txt",
                "Average Speed not valid at line 1"
        );
    }

    @Test
    public void shouldFailWhenLikesInvalid() {
        assertInvalidFile(
                "transport-like-invalid-file.txt",
                "Likes not valid at line 3"
        );
    }

    @Test
    public void shouldFailWhenMaxSpeedInvalid() {
        assertInvalidFile(
                "transport-max-speed-invalid-file.txt",
                "Top Speed not valid at line 3"
        );
    }

    @Test
    public void shouldFailWhenNameInvalid() {
        assertInvalidFile(
                "transport-name-invalid-file.txt",
                "Name not valid at line 1"
        );
    }

    @Test
    public void shouldFailWhenTransportInvalid() {
        assertInvalidFile(
                "transport-transport-invalid-file.txt",
                "Transport not valid at line 2"
        );
    }

    @Test
    public void shouldFailWhenDelimiterCountInvalid() {
        assertInvalidFile(
                "transport-delimiter-incorrect-file.txt",
                "Delimiter Count not valid at line 2"
        );
    }

    /* ---------- helpers ---------- */

    private io.restassured.response.Response postFile(String resourceFile) {
        return given()
                .header("X-Forwarded-For", IP)
                .multiPart(
                        "file",
                        FILE_NAME,
                        readFile(resourceFile),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post(ENDPOINT);
    }

    private void assertInvalidFile(String file, String expectedDetail) {
        postFile(file)
                .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("detail", equalTo(expectedDetail))
                .body("errorCode", equalTo("I0020"));
    }

    private static byte[] readFile(String filename) {
        try (InputStream is = new ClassPathResource(filename).getInputStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read test file: " + filename, e);
        }
    }
}
