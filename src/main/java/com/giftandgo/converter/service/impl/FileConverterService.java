package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.OutcomeContent;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.ConversionLogPersistable;
import com.giftandgo.converter.service.FileConvertable;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.service.IpValidatable;
import com.giftandgo.converter.util.FileReadWriteUtil;
import com.giftandgo.converter.validator.impl.file.FileValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileConverterService implements FileConvertable {

    private final ConversionLogPersistable conversionLogService;
    private final FileReadable<List<OutcomeContent>> fileReadable;
    private final FileValidatorFactory fileValidatorFactory;
    private final IpValidatable validateIpService;

    @Override
    public OutcomeFile convertFile(@NonNull MultipartFile file, @NonNull String ip) {
        long startMoment = System.nanoTime();
        ConversionLog conversionLog = saveConversionLog(ip);
        try {
            validateIpService.saveIpDetailsAndRunValidationRules(conversionLog, ip);
            OutcomeFile outcomeFile = getConvertedFile(file);
            saveExecutionResults(startMoment, conversionLog, HttpStatus.OK); // kerem bu created olsun
            return outcomeFile;
        } catch (ConverterRuntimeException e) {
            saveExecutionResults(startMoment, conversionLog, e.getErrorCode().getHttpStatus());
            throw e;
        }
    }

    private OutcomeFile getConvertedFile(MultipartFile file) {
        List<OutcomeContent> parsedContent = fileReadable.getValidatedFileContent(file, fileValidatorFactory.getValidator());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileReadWriteUtil.write(outputStream, parsedContent);
        return new OutcomeFile(
                "OutcomeFile.json",
                new ByteArrayInputStream(outputStream.toByteArray())
        );
    }

    private ConversionLog saveConversionLog(String ip) {
        return conversionLogService.create("uri", ip);
    }

    private void saveExecutionResults(long startMoment, ConversionLog conversionLog, HttpStatus httpStatus) {
        conversionLogService.update(conversionLog.setExecutionResults(System.nanoTime() - startMoment, httpStatus.value()));
    }

}
