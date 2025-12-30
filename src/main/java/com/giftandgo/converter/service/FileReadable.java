package com.giftandgo.converter.service;

import com.giftandgo.converter.model.OutcomeFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileReadable {

    OutcomeFile getValidatedFileContent(MultipartFile file);

}
