package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConversionLog;

public interface ConversionLogPersistable {

    ConversionLog create(String uri, String ip);

    ConversionLog update(ConversionLog conversionLog);

}
