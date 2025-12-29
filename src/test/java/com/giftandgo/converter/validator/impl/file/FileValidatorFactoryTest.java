package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.validator.Validatable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileValidatorFactoryTest {

    @Test
    void getValidatorsShouldReturnValidatorsWhenFilterWorking() {
        Validatable<String[]> fileValidateAvgSpeed = new FileValidateAvgSpeed();
        Validatable<String[]> fileValidateDelimiterCount = new FileValidateDelimiterCount();
        Validatable<String[]> fileValidateTransport = new FileValidateTransport();

        FileValidatorFactory factory = new FileValidatorFactory(List.of(fileValidateAvgSpeed, fileValidateDelimiterCount, fileValidateTransport), List.of("TransportStrategy"));

        Object result = factory.getValidators();

        assertThat(result).hasSize(1).containsExactly(fileValidateTransport);
    }

    @Test
    void getValidatorsShouldReturnValidatorsWhenSortingWorking() {
        Validatable<String[]> fileValidateAvgSpeed = new FileValidateAvgSpeed();
        Validatable<String[]> fileValidateDelimiterCount = new FileValidateDelimiterCount();
        Validatable<String[]> fileValidateEmptyLine = new FileValidateEmptyLine();

        FileValidatorFactory factory = new FileValidatorFactory(List.of(fileValidateAvgSpeed, fileValidateDelimiterCount, fileValidateEmptyLine), List.of("AvgSpeedStrategy", "DelimiterCountStrategy", "EmptyLineStrategy"));

        Object result = factory.getValidators();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getValidationKey()).isEqualTo("EmptyLineStrategy");
        assertThat(result.get(1).getValidationKey()).isEqualTo("DelimiterCountStrategy");
        assertThat(result.get(2).getValidationKey()).isEqualTo("AvgSpeedStrategy");
    }

}