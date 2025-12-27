package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConversionLog;

public interface IpValidatable {

    void saveIpDetailsAndRunValidationRules(ConversionLog conversionLog, String ip);

}
