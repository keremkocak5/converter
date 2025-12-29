package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.validator.Validatable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransportationFileValidatorFactoryTest {

    @Test
    void getValidatorsShouldReturnValidatorsWhenFilterWorking() {
        Validatable<String[]> fileValidateAvgSpeed = new FileValidateAvgSpeed();
        Validatable<String[]> fileValidateDelimiterCount = new FileValidateDelimiterCount();
        Validatable<String[]> fileValidateTransport = new FileValidateTransport();

        TransportationFileValidatorFactory factory = new TransportationFileValidatorFactory(List.of(fileValidateAvgSpeed, fileValidateDelimiterCount, fileValidateTransport), List.of("TransportStrategy"));

        List<Validatable<String[]>> result = factory.getValidators();

        assertThat(result).hasSize(1).containsExactly(fileValidateTransport);
    }

    @Test
    void getValidatorsShouldReturnValidatorsWhenSortingWorking() {
        Validatable<String[]> fileValidateAvgSpeed = new FileValidateAvgSpeed();
        Validatable<String[]> fileValidateDelimiterCount = new FileValidateDelimiterCount();
        Validatable<String[]> fileValidateEmptyLine = new FileValidateEmptyLine();

        TransportationFileValidatorFactory factory = new TransportationFileValidatorFactory(List.of(fileValidateAvgSpeed, fileValidateDelimiterCount, fileValidateEmptyLine), List.of("AvgSpeedStrategy", "DelimiterCountStrategy", "EmptyLineStrategy"));

        List<Validatable<String[]>> result = factory.getValidators();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getValidationKey()).isEqualTo("EmptyLineStrategy");
        assertThat(result.get(1).getValidationKey()).isEqualTo("DelimiterCountStrategy");
        assertThat(result.get(2).getValidationKey()).isEqualTo("AvgSpeedStrategy");
    }

}