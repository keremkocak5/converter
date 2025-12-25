package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.repository.ConversionLogRepository;
import com.giftandgo.converter.service.ConversionLogCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversionLogService implements ConversionLogCRUD {

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

    @Override
    @Transactional(readOnly = true)
    public List<ConversionLog> findAll() {
        return conversionLogRepository.findAll(); // this is a NO - NO!!
    }
}
