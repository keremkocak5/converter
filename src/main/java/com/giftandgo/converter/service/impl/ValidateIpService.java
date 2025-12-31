package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.ConversionLogPersistable;
import com.giftandgo.converter.service.IpDetailsRetrievable;
import com.giftandgo.converter.service.IpValidatable;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.ip.IpValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ValidateIpService implements IpValidatable {

    private final IpDetailsRetrievable ipApiClient;
    private final ConversionLogPersistable conversionLogService;
    private final IpValidatorFactory ipValidatorFactory;

    @Override
    public void saveIpDetailsAndRunIpValidationRules(ConversionLog conversionLog, String ip) {
        List<Validatable<IpDetails>> validators = ipValidatorFactory.getValidators();
        if (validators.size() == 0) {
            return; // do not call api if no validators are set.
        }
        IpDetails ipDetails = ipApiClient
                .getIpDetails(ip)
                .orElseThrow(() -> new ConverterRuntimeException(ErrorCode.IP_API_RESOLVE_ERROR));
        conversionLogService.update(conversionLog.setIpDetails(ipDetails.isp(), ipDetails.countryCode()));
        validators.stream()
                .filter(rule -> !rule.isValid(ipDetails))
                .findFirst()
                .flatMap(Validatable::getErrorCode)
                .ifPresent(errorCode -> {
                    throw new ConverterRuntimeException(errorCode);
                });
    }

}
