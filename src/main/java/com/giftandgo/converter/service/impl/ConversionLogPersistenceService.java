package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.repository.ConversionLogRepository;
import com.giftandgo.converter.service.ConversionLogPersistable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversionLogPersistenceService implements ConversionLogPersistable {

    private final ConversionLogRepository conversionLogRepository;

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public ConversionLog create(String uri, String ip) {
        return conversionLogRepository.save(new ConversionLog(uri, ip));
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public ConversionLog update(ConversionLog conversionLog) {
        return conversionLogRepository.save(conversionLog);
    }

}
