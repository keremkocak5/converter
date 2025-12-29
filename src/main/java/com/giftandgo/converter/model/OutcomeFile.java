package com.giftandgo.converter.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

import java.io.InputStream;

public record OutcomeFile(
        @Valid @NonNull @NotEmpty String fileName,
        @Valid @NonNull @NotEmpty InputStream inputStream
) {
}