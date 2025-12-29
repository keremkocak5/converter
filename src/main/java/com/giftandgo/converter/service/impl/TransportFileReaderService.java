package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeContent;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.file.FileValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static com.giftandgo.converter.util.Constants.DELIMITER_PATTERN;
import static com.giftandgo.converter.util.Constants.OUTCOME_FILE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
class TransportFileReaderService extends FileReaderServiceTemplate<OutcomeContent> {

    private final FileValidatorFactory fileValidatorFactory;

    @Override
    void validateContent(List<String[]> lines) {
        AtomicInteger lineNumber = new AtomicInteger();
        for (String[] line : lines) {
            int currentLine = lineNumber.incrementAndGet();
            fileValidatorFactory.getValidators()
                    .stream()
                    .filter(validator -> !validator.isValid(line))
                    .findAny()
                    .flatMap(Validatable::getErrorCode)
                    .ifPresent(errorCode -> {
                        throw new ConverterRuntimeException(
                                errorCode,
                                currentLine
                        );
                    });
        }
    }

    @Override
    OutcomeContent mapLine(String[] delimitedPart) {
        return new OutcomeContent(
                delimitedPart[2],
                delimitedPart[4],
                Double.parseDouble(delimitedPart[6])
        );
    }

    @Override
    Pattern getDelimiterPattern() {
        return DELIMITER_PATTERN;
    }

    @Override
    String getFileName() {
        return OUTCOME_FILE_NAME;
    }
}
