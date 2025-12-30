package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConversionLog;

public interface ConversionLogPersistable {

    ConversionLog create(String uri, String ip);

    void update(ConversionLog conversionLog);

}
