package com.giftandgo.converter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class IpApiGatewayConfig {

    @Value("${rest.ipapi.timeout.connect}")
    private int connectTimeout;

    @Value("${rest.ipapi.timeout.read}")
    private int readTimeout;

    @Value("${rest.ipapi.url}")
    private String ipApiUrl;

    @Bean
    public RestClient ipApiRestClient() {
        return RestClient
                .builder()
                .requestFactory(clientHttpRequestFactory())
                .baseUrl(ipApiUrl)
                .build();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofMillis(connectTimeout))
                .withReadTimeout(Duration.ofMillis(readTimeout)));
    }
}
