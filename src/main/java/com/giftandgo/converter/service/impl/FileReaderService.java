package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeContent;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.util.FileReadWriteUtil;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.file.TransportationFileValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.giftandgo.converter.util.Constants.DELIMITER_PATTERN;
import static com.giftandgo.converter.util.Constants.OUTCOME_FILE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
class FileReaderService implements FileReadable<OutcomeFile> {

    private final TransportationFileValidatorFactory transportationFileValidatorFactory;

    @Override
    public OutcomeFile getValidatedFileContent(MultipartFile file) {
        List<String[]> lines = getDelimitedContent(file);
        validateContent(lines);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileReadWriteUtil.write(outputStream, getParsedContent(lines));
        return new OutcomeFile(
                OUTCOME_FILE_NAME,
                new ByteArrayInputStream(outputStream.toByteArray())
        );
    }

    private void validateContent(List<String[]> lines) {
        AtomicInteger lineNumber = new AtomicInteger();
        for (String[] line : lines) {
            lineNumber.incrementAndGet();
            transportationFileValidatorFactory.getValidators()
                    .stream()
                    .filter(validator -> !validator.isValid(line))
                    .findAny()
                    .flatMap(Validatable::getErrorCode)
                    .ifPresent(errorCode -> {
                        throw new ConverterRuntimeException(errorCode, lineNumber.get());
                    });
        }
    }

    private List<OutcomeContent> getParsedContent(List<String[]> delimitedParts) {
        try {
            return delimitedParts
                    .stream()
                    .map(delimitedPart -> new OutcomeContent(delimitedPart[2], delimitedPart[4], Double.parseDouble(delimitedPart[6])))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Cannot read file. ", e);
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }

    private List<String[]> getDelimitedContent(MultipartFile file) {
        try {
            return FileReadWriteUtil.read(file.getInputStream(), DELIMITER_PATTERN);
        } catch (Exception e) {
            log.error("Cannot write file. ", e);
            throw new ConverterRuntimeException(ErrorCode.CANNOT_WRITE_FILE);
        }
    }

}
