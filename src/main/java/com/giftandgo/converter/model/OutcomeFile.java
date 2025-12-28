package com.giftandgo.converter.model;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

import java.io.InputStream;

public record OutcomeFile(
        @NonNull @NotEmpty String fileName,
        @NonNull @NotEmpty InputStream inputStream
) {
}