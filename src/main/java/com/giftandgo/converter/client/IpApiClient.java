package com.giftandgo.converter.client;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.IpDetailsRetrievable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class IpApiClient implements IpDetailsRetrievable {

    private final RestClient ipApiRestClient;
    private final String URI = "/json/{ip}?fields=status,countryCode,isp,message";
    private final String SUCCESS = "success";

    @Override
    @Retryable(retryFor = org.springframework.web.client.RestClientException.class, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2))
    public Optional<IpDetails> getIpDetails(String ip) {
        IpApiResponse response = ipApiRestClient
                .get()
                .uri(URI, ip)
                .retrieve()
                .body(IpApiResponse.class);
        if (!SUCCESS.equalsIgnoreCase(response.status())) {
            log.info("IP-API returned failure for ip {}: {}", ip, response.message());
            return Optional.empty();
        }
        return Optional.of(new IpDetails(response.countryCode(), response.isp()));
    }

    @Recover
    public Optional<IpDetails> recover(org.springframework.web.client.RestClientException ex, String ip) {
        log.error("IP-API failed after retries for ip {}", ip, ex);
        throw new ConverterRuntimeException(ErrorCode.IP_API_CONNECTION_ERROR);
    }
}