package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.ConversionLogPersistable;
import com.giftandgo.converter.service.FileConvertible;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.service.IpValidatable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.giftandgo.converter.util.Constants.TIME_LAPSED_MILLIS;

@RequiredArgsConstructor
@Service
public class FileConverterService implements FileConvertible {

    private final ConversionLogPersistable conversionLogService;
    private final FileReadable fileReaderService;
    private final IpValidatable validateIpService;

    @Override
    public OutcomeFile convertFile(@NonNull MultipartFile file, @NonNull String ip, @NonNull String uri) {
        long startMoment = System.nanoTime();
        ConversionLog conversionLog = saveConversionLog(uri, ip);
        try {
            validateIpService.saveIpDetailsAndRunIpValidationRules(conversionLog, ip);
            OutcomeFile outcomeFile = fileReaderService.getValidatedFileContent(file);
            saveExecutionResults(startMoment, conversionLog, HttpStatus.OK);
            return outcomeFile;
        } catch (ConverterRuntimeException e) {
            saveExecutionResults(startMoment, conversionLog, e.getErrorCode().getHttpStatus());
            throw e;
        }
    }

    private ConversionLog saveConversionLog(String uri, String ip) {
        return conversionLogService.create(uri, ip);
    }

    private void saveExecutionResults(long startMoment, ConversionLog conversionLog, HttpStatus httpStatus) {
        conversionLogService.update(conversionLog.setExecutionResults(TIME_LAPSED_MILLIS.apply(startMoment), httpStatus.value()));
    }

}
