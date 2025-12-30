package com.giftandgo.converter.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Rule;
import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestBase {

    @Rule
    public WireMockRule mockRule = new WireMockRule(8100);

    @Before
    public void init() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        mockRule.stubFor(
                get(urlPathEqualTo("/json/1.1.1.1"))
                        .atPriority(10)
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(new IpApiResponse("success", "UK", "SOMEISP", ""))))
        );

        mockRule.stubFor(
                get(urlPathEqualTo("/json/2.2.2.2"))
                        .atPriority(10)
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(new IpApiResponse("success", "CA", "SOMEISP", ""))))
        );

        mockRule.stubFor(
                get(urlPathEqualTo("/json/3.3.3.3"))
                        .atPriority(10)
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(new IpApiResponse("success", "UK", "AZURE", ""))))
        );

        mockRule.stubFor(
                get(urlPathEqualTo("/json/4.4.4.4"))
                        .atPriority(10)
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(new IpApiResponse("failure", "", "", "bad news"))))
        );

        RestAssured.baseURI = "http://localhost:8081";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private record IpApiResponse(String status, String countryCode, String isp, String message) {
    }

}