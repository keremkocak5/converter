package com.giftandgo.converter.model;

import java.io.InputStream;

public record OutcomeFile(
        String fileName,
        InputStream inputStream
) {
}