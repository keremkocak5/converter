package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.util.FileReadWriteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
abstract class FileReaderServiceTemplate<T> implements FileReadable<OutcomeFile> {

    abstract void validateContent(List<String[]> lines);

    abstract Pattern getDelimiterPattern();

    abstract T mapLine(String[] delimitedPart);

    abstract String getFileName();

    @Override
    public OutcomeFile getValidatedFileContent(MultipartFile file) {
        List<String[]> lines = getDelimitedContent(file);
        validateContent(lines);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileReadWriteUtil.write(outputStream, getParsedContent(lines));

        return new OutcomeFile(
                getFileName(),
                new ByteArrayInputStream(outputStream.toByteArray())
        );
    }

    private List<T> getParsedContent(List<String[]> delimitedParts) {
        try {
            return delimitedParts.stream()
                    .map(this::mapLine)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Cannot read file.", e);
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }

    private List<String[]> getDelimitedContent(MultipartFile file) {
        try {
            return FileReadWriteUtil.read(
                    file.getInputStream(),
                    getDelimiterPattern()
            );
        } catch (Exception e) {
            log.error("Cannot write file.", e);
            throw new ConverterRuntimeException(ErrorCode.CANNOT_WRITE_FILE);
        }
    }
}
