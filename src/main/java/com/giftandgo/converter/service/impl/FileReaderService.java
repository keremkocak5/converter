package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.service.DelimitedFileReadable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FileReaderService implements DelimitedFileReadable {

    @Override
    public List<String[]> read(InputStream inputStream, Pattern pattern) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .filter(l -> !l.isBlank())
                    .map(l -> pattern.split(l))
                    .toList();
        } catch (Exception e) {
            log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }
}
