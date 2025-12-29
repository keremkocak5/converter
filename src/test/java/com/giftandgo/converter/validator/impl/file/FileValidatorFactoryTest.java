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

        List<Validatable<String[]>> result = factory.getValidators();

        assertThat(result).hasSize(1).containsExactly(fileValidateTransport);
    }

    // kerem sortlamayi da test et

}