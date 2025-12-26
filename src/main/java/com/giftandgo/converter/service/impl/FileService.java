package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.FileContent;
import com.giftandgo.converter.service.DelimitedFileReadable;
import com.giftandgo.converter.service.FileSavable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.giftandgo.converter.util.Constants.DELIMITER_PATTERN;

@Service
@RequiredArgsConstructor
public class FileService implements FileSavable {

    private final DelimitedFileReadable delimitedFileReadable;

    @Override
    public void validateAndSaveFile(MultipartFile file) {
        List<String[]> delimitedParts = getDelimitedParts(file);
        FileContent fileContent = parseFile(delimitedParts);

    }

    private FileContent parseFile(List<String[]> delimitedParts) {
        return new FileContent(delimitedParts.)
    }

    private List<String[]> getDelimitedParts(MultipartFile file) {
        try {
            return delimitedFileReadable.read(file.getInputStream(), DELIMITER_PATTERN);
        } catch (Exception e) {
            //log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }

}
