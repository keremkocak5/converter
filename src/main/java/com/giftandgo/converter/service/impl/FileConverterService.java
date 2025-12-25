package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.ConversionLogCRUD;
import com.giftandgo.converter.service.FileConvertable;
import com.giftandgo.converter.service.IpRestrictable;
import com.giftandgo.converter.service.IpTraceable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileConverterService implements FileConvertable {

    private final IpTraceable ipApiClient;
    private final ConversionLogCRUD conversionLogService;
    private final List<IpRestrictable> ipRestrictionRules;

    @Override
    public String convertFile(String ip) {
        long startMoment = System.nanoTime();
        ConversionLog conversionLog = conversionLogService.create("uri", ip);
        try {
            getIpDetailsAndRunRestrictionRules(conversionLog);
            // do file operations
            conversionLogService.update(conversionLog.setResults(System.nanoTime() - startMoment, HttpStatus.OK.value()));
        } catch (ConverterRuntimeException e) {
            conversionLogService.update(conversionLog.setResults(System.nanoTime() - startMoment, e.getErrorCode().getHttpStatus().value()));
            throw e;
        }
        return "Your file is ready.";
    }

    private void getIpDetailsAndRunRestrictionRules(ConversionLog conversionLog) {
        IpDetails ipDetails = ipApiClient.getIpDetails("24.48.0.1").orElseThrow(() -> new ConverterRuntimeException(ErrorCode.IP_API_RESOLVE_ERROR)); // kerem dikkat
        conversionLogService.update(conversionLog.setIpDetails(ipDetails.isp(), ipDetails.country()));
        ipRestrictionRules.stream()
                .filter(rule -> rule.isRestricted(ipDetails))
                .findAny()
                .ifPresent(rule -> {
                    throw new ConverterRuntimeException(ErrorCode.RESTRICTED_IP);
                });
    }

}
