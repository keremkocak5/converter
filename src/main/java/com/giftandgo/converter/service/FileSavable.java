package com.giftandgo.converter.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileSavable {

    void validateAndSaveFile(MultipartFile file);

}
