package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConversionLog;

public interface IpValidatable {

    void saveIpDetailsAndRunIpValidationRules(ConversionLog conversionLog, String ip);

}
