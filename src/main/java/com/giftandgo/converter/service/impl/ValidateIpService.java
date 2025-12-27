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

@Service
@RequiredArgsConstructor
public class ValidateIpService implements IpValidatable {

    private final IpDetailsRetrievable ipApiClient;
    private final ConversionLogPersistable conversionLogService;
    private final IpValidatorFactory ipValidatorFactory;

    @Override
    public void saveIpDetailsAndRunIpValidationRules(ConversionLog conversionLog, String ip) {
        IpDetails ipDetails = ipApiClient
                .getIpDetails("24.48.0.1") // kerem
                .orElseThrow(() -> new ConverterRuntimeException(ErrorCode.IP_API_RESOLVE_ERROR)); // kerem dikkat
        conversionLogService.update(conversionLog.setIpDetails(ipDetails.isp(), ipDetails.countryCode()));
        ipValidatorFactory.getValidators()
                .stream()
                .filter(rule -> !rule.isValid(ipDetails))
                .findFirst()
                .flatMap(Validatable::getErrorCode)
                .ifPresent(ConverterRuntimeException::new);
    }

}
