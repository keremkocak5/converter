package com.giftandgo.converter.service.impl;

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
    private final List<IpRestrictable> ipRestrictionRules;
    private final ConversionLogCRUD conversionLogService;

    @Override
    public String convertFile(String ip) {
        ConversionLog conversionLog = conversionLogService.create("uri", ip);
        IpDetails ipDetails = ipApiClient.getIpDetails("24.48.0.1"); // kerem dikkat
        try {
            ipRestrictionRules.forEach(ipRestrictionRule -> ipRestrictionRule.runRestrictionRule(ipDetails));
            conversionLogService.update(conversionLog.postProcess(1, ipDetails.isp(), ipDetails.country(), HttpStatus.OK.value()));
        } catch (ConverterRuntimeException e) {
            conversionLogService.update(conversionLog.postProcess(1, ipDetails.isp(), ipDetails.country(), e.getErrorCode().getHttpStatus().value()));
            throw e;
        } catch (Exception e) {
            conversionLogService.update(conversionLog.postProcess(1, HttpStatus.INTERNAL_SERVER_ERROR.value()));
            throw e;
        }
        return null;
    }

}
