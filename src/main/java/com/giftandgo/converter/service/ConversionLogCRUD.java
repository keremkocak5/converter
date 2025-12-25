package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConversionLog;

import java.util.List;

public interface ConversionLogCRUD {

    ConversionLog createNewEntity(String uri);

    ConversionLog update(ConversionLog conversionLog);

    List<ConversionLog> findAll();

}
