package com.giftandgo.converter.client;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.IpDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpApiClientTest {

    @InjectMocks
    private IpApiClient ipApiClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @Test
    void getIpDetailsShouldReturnIpDetailsWhenRestCallSucceeds() {
        when(restClient.get()
                .uri("/json/{ip}?fields=status,countryCode,isp,message", "1.2.3.4")
                .retrieve()
                .body(IpApiResponse.class))
                .thenReturn(new IpApiResponse("success", "UK", "VIRGIN", null));

        Optional<IpDetails> result = ipApiClient.getIpDetails("1.2.3.4");

        assertTrue(result.isPresent());
        assertThat(result.get().countryCode(), is("UK"));
        assertThat(result.get().isp(), is("VIRGIN"));
    }

    @Test
    void getIpDetailsShouldReturnEmptyWhenApiReturnsFailure() {
        when(restClient.get()
                .uri("/json/{ip}?fields=status,countryCode,isp,message", "1.2.3.4")
                .retrieve()
                .body(IpApiResponse.class))
                .thenReturn(new IpApiResponse("fail", "UK", "VIRGIN", "some message"));

        Optional<IpDetails> result = ipApiClient.getIpDetails("1.2.3.4");

        assertTrue(result.isEmpty());
    }

    @Test
    void getIpDetailsShouldThrowConverterRuntimeExceptionWhenRestClientFails() {
        when(restClient.get()
                .uri("/json/{ip}?fields=status,countryCode,isp,message", "1.2.3.4")
                .retrieve()
                .body(IpApiResponse.class))
                .thenThrow(new RestClientException("Connection failed"));

        assertThrows(RestClientException.class, () -> ipApiClient.getIpDetails("1.2.3.4"));
    }

    @Test
    void recoverShouldThrowConverterRuntimeException() {
        RestClientException ex = new RestClientException("fail");
        ConverterRuntimeException exception = assertThrows(ConverterRuntimeException.class,
                () -> ipApiClient.recover(ex, "1.2.3.4"));

        assertThat(exception.getErrorCode(), is(ErrorCode.IP_API_CONNECTION_ERROR));
    }
}
