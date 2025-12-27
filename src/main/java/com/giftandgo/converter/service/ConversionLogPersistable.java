package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConversionLog;

import java.util.List;

public interface ConversionLogPersistable {

    ConversionLog create(String uri, String ip);

    ConversionLog update(ConversionLog conversionLog);

    List<ConversionLog> findAll();

}
