package com.giftandgo.converter.service;

import com.giftandgo.converter.validator.Validatable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface FileReadable<T> {

    T getValidatedFileContent(MultipartFile file, Set<Validatable<String[]>> validators);

}
