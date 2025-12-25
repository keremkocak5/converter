package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.repository.ConversionLogRepository;
import com.giftandgo.converter.service.ConversionLogCRUD;
import com.giftandgo.converter.service.FileConvertable;
import com.giftandgo.converter.service.IpRestrictable;
import com.giftandgo.converter.service.IpTraceable;
import lombok.RequiredArgsConstructor;
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
        ConversionLog conversionLog = conversionLogService.createNewEntity("uri");
        IpDetails ipDetails = ipApiClient.getIpDetails("24.48.0.1"); // kerem dikkat
        ipRestrictionRules.forEach(ipRestrictionRule -> ipRestrictionRule.runRestrictionRule(ipDetails));
        conversionLogService.update(conversionLog);
        return null;
    }
}
