package com.giftandgo.converter.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Rule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestBase {

    @Rule
    public WireMockRule mockRule = new WireMockRule(8100);

    @Before
    public void init() {
        mockRule.stubFor(
                get(urlPathEqualTo("/json"))
                        .atPriority(10)
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBody("{status: 'ok'}")));

        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}