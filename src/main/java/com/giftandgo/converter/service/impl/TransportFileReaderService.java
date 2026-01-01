package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.TransportOutcomeContent;
import com.giftandgo.converter.validator.impl.file.TransportFileValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static com.giftandgo.converter.enums.ErrorCode.INCORRECT_FILE_FORMAT;

@Service
@RequiredArgsConstructor
class TransportFileReaderService extends FileReaderServiceTemplate<TransportOutcomeContent> {

    private final TransportFileValidatorFactory transportFileValidatorFactory;

    private static final String DELIMITER = "|";
    public static final Pattern DELIMITER_PATTERN = Pattern.compile(Pattern.quote(DELIMITER));
    public static final String OUTCOME_FILE_NAME = "OutcomeFile.json";

    @Override
    void validateContent(List<String[]> lines) {
        AtomicInteger lineNumber = new AtomicInteger();
        for (String[] line : lines) {
            int currentLine = lineNumber.incrementAndGet();
            transportFileValidatorFactory.getValidators()
                    .stream()
                    .sorted(Comparator.comparing(validator -> validator.getValidationPriority()))
                    .filter(validator -> !validator.getValidator().test(line, validator.getAssociatedColumn()))
                    .findAny()
                    .ifPresent(validator -> {
                        throw new ConverterRuntimeException(
                                INCORRECT_FILE_FORMAT,
                                validator.getColumnName(),
                                currentLine
                        );
                    });
        }
    }

    @Override
    TransportOutcomeContent getLineToOutputMapper(String[] delimitedPart) {
        return new TransportOutcomeContent(
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
