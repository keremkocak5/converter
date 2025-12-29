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
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest extends TestBase {

    @Test
    public void shouldSucceed() {
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