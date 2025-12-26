package com.giftandgo.converter.util;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static com.giftandgo.converter.util.Constants.SINGLETON_OBJECT_MAPPER;

@Slf4j
@UtilityClass
public class FileReadWriteUtil {

    public static List<String[]> read(InputStream inputStream, Pattern pattern) {
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

    public void exportToFile(Path path, Object data) {
        try (OutputStream os = Files.newOutputStream(path)) {
            write(os, data);
        } catch (Exception e) {
            throw new RuntimeException("Export failed", e);
        }
    }

    private <T> void write(OutputStream outputStream, T object) {
        try {
            SINGLETON_OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(outputStream, object);
        } catch (Exception e) {
            log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);        // kerem hata degissin
        }
    }

}
