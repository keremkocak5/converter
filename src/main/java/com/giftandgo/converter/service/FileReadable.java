package com.giftandgo.converter.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileReadable<T> {

    T getValidatedFileContent(MultipartFile file, FileValidateble validator);

}
