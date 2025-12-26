package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.FileContent;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.giftandgo.converter.util.Constants.DELIMITER_PATTERN;

@RequiredArgsConstructor
@Service
public class FileConverterService implements FileConvertable {

    private final IpTraceable ipApiClient;
    private final ConversionLogCRUD conversionLogService;
    private final DelimitedFileReadable fileReaderService;
    private final Set<IpRestrictable> ipRestrictionRules;

    @Override
    public String convertFile(@NonNull MultipartFile file, @NonNull String ip) {
        long startMoment = System.nanoTime();
        ConversionLog conversionLog = saveConversionLog(ip);
        try {
            saveIpDetailsAndRunRestrictionRules(conversionLog);
            saveFile(validateFile(readFile(file)));
            saveExecutionResults(startMoment, conversionLog, HttpStatus.OK); // kerem bu created olsun
        } catch (ConverterRuntimeException e) {
            saveExecutionResults(startMoment, conversionLog, e.getErrorCode().getHttpStatus());
            throw e;
        }
        return "Your file is ready. Filename is kerem";
    }

    private FileContent validateFile(List<String[]> readFile) {
        return new FileContent(UUID.randomUUID(), "", "", "", "", Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private List<String[]> readFile(MultipartFile file) {
        try {
            return fileReaderService.read(file.getInputStream(), DELIMITER_PATTERN);
        } catch (Exception e) {
            //log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }

    private void saveIpDetailsAndRunRestrictionRules(ConversionLog conversionLog) {
        IpDetails ipDetails = ipApiClient
                .getIpDetails("24.48.0.1")
                .orElseThrow(() -> new ConverterRuntimeException(ErrorCode.IP_API_RESOLVE_ERROR)); // kerem dikkat
        conversionLogService.update(conversionLog.setIpDetails(ipDetails.isp(), ipDetails.country()));
        ipRestrictionRules.stream()
                .filter(rule -> rule.isRestricted(ipDetails))
                .findAny()
                .ifPresent(restrictionExists -> {
                    throw new ConverterRuntimeException(ErrorCode.RESTRICTED_IP);
                });
    }

    private ConversionLog saveConversionLog(String ip) {
        return conversionLogService.create("uri", ip);
    }

    private void saveFile(FileContent fileContent) {
    }

    private void saveExecutionResults(long startMoment, ConversionLog conversionLog, HttpStatus httpStatus) {
        conversionLogService.update(conversionLog.setExecutionResults(System.nanoTime() - startMoment, httpStatus.value()));
    }

}
