package com.giftandgo.converter.model;

import java.io.InputStream;

public record ConvertedFile(
        String fileName,
        InputStream inputStream
) {
}