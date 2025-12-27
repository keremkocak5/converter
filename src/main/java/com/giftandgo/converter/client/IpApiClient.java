package com.giftandgo.converter.client;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.IpDetailsRetrievable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Optional<IpDetails> getIpDetails(String ip) {
        try {
            IpApiResponse ipApiResponse = ipApiRestClient
                    .get()
                    .uri(URI, ip)
                    .retrieve()
                    .body(IpApiResponse.class);
            if (!SUCCESS.equalsIgnoreCase(ipApiResponse.status())) {
                log.info("IpApiClient could not find details for ip {}, error {}.", ip, ipApiResponse.message());
                return Optional.empty();
            }
            return Optional.of(new IpDetails(ipApiResponse.countryCode(), ipApiResponse.isp()));
        } catch (Exception e) {
            log.error("IpApiClient failed for ip {}.", ip);
            log.error("IpApiClient failed.", e);
            throw new ConverterRuntimeException(ErrorCode.IP_API_CONNECTION_ERROR);
        }
    }

}
