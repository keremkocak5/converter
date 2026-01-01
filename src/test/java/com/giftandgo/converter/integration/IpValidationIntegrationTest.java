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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("wiremock")
public class IpValidationIntegrationTest extends TestBase {

    private static final String ENDPOINT = "/converter/v1/file";
    private static final String FILE_NAME = "input.txt";
    private static final String SUCCESS_FILE = "transport-success-file.txt";

    private static final String IP_ALLOWED = "1.1.1.1";
    private static final String IP_BLOCKED_COUNTRY = "2.2.2.2";
    private static final String IP_BLOCKED_ISP = "3.3.3.3";
    private static final String IP_API_ERROR = "4.4.4.4";
    private static final String IP_NULL_COUNTRY = "5.5.5.5";
    private static final String IP_CONNECTION_FAIL = "6.6.6.6";

    @Test
    public void shouldSucceedWhenIpDoesNotHaveRestrictions() {
        postFile(IP_ALLOWED)
                .then()
                .statusCode(200)
                .header("Content-Disposition", notNullValue());

        verifyIpApiCalled();
    }

    @Test
    public void shouldFailWhenIpCountryBlocked() {
        postFile(IP_BLOCKED_COUNTRY)
                .then()
                .statusCode(403)
                .contentType("application/problem+json")
                .body("title", equalTo("Country is restricted."))
                .body("errorCode", equalTo("I0002"));

        verifyIpApiCalled();
    }

    @Test
    public void shouldFailWhenIpISPBlocked() {
        postFile(IP_BLOCKED_ISP)
                .then()
                .statusCode(403)
                .contentType("application/problem+json")
                .body("title", equalTo("ISP is restricted."))
                .body("errorCode", equalTo("I0001"));

        verifyIpApiCalled();
    }

    @Test
    public void shouldFailWhenIpApiReturnsFailure() {
        postFile(IP_API_ERROR)
                .then()
                .statusCode(500)
                .contentType("application/problem+json")
                .body("title", equalTo("IP API could not resolve arguments."))
                .body("errorCode", equalTo("I0011"));

        verifyIpApiCalled();
    }

    @Test
    public void shouldFailWhenCountryIsNull() {
        postFile(IP_NULL_COUNTRY)
                .then()
                .statusCode(403)
                .contentType("application/problem+json")
                .body("title", equalTo("ISP is restricted."))
                .body("errorCode", equalTo("I0001"));

        verifyIpApiCalled();
    }

    @Test
    public void shouldFailWhenIpApiConnectionFails() {
        postFile(IP_CONNECTION_FAIL)
                .then()
                .statusCode(500)
                .contentType("application/problem+json")
                .body("title", equalTo("Could not connect to IP-API."))
                .body("errorCode", equalTo("I0010"));

        verifyIpApiCalled();
    }

    private io.restassured.response.Response postFile(String ip) {
        return given()
                .header("X-Forwarded-For", ip)
                .multiPart(
                        "file",
                        FILE_NAME,
                        readFile(SUCCESS_FILE),
                        MediaType.TEXT_PLAIN_VALUE
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post(ENDPOINT);
    }

    private void verifyIpApiCalled() {
        verify(getRequestedFor(urlPathMatching("/json/.*"))
                .withQueryParam(
                        "fields",
                        com.github.tomakehurst.wiremock.client.WireMock.equalTo(
                                "status,countryCode,isp,message"
                        )
                ));
    }

    private static byte[] readFile(String filename) {
        try (InputStream is = new ClassPathResource(filename).getInputStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read test file: " + filename, e);
        }
    }
}
