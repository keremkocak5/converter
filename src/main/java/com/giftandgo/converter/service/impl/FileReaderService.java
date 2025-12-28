package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeContent;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.util.FileReadWriteUtil;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.file.FileValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.giftandgo.converter.util.Constants.DELIMITER_PATTERN;


@Service
@RequiredArgsConstructor
class FileReaderService implements FileReadable<OutcomeFile> {

    private final FileValidatorFactory fileValidatorFactory;

    @Override
    public OutcomeFile getValidatedFileContent(MultipartFile file) {
        List<String[]> delimitedContents = getDelimitedContent(file);
        validateContent(delimitedContents);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileReadWriteUtil.write(outputStream, getParsedContent(delimitedContents));
        return new OutcomeFile(
                "OutcomeFile.json",
                new ByteArrayInputStream(outputStream.toByteArray())
        );
    }

    private void validateContent(List<String[]> delimitedContents) {
        AtomicInteger lineNumber = new AtomicInteger();
        for (String[] delimitedContent : delimitedContents) {
            lineNumber.incrementAndGet();
            fileValidatorFactory.getValidators()
                    .stream()
                    .filter(validator -> !validator.isValid(delimitedContent))
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
            //log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE); // kerem buraya yeni bi exception ekle
        }
    }

    private List<String[]> getDelimitedContent(MultipartFile file) {
        try {
            return FileReadWriteUtil.read(file.getInputStream(), DELIMITER_PATTERN);
        } catch (Exception e) {
            //log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }

}
