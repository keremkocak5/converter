package com.giftandgo.converter.service;

import com.giftandgo.converter.validator.Validatable;
import org.springframework.web.multipart.MultipartFile;

public interface FileReadable<T> {

    T getValidatedFileContent(MultipartFile file, Validatable validator);

}
