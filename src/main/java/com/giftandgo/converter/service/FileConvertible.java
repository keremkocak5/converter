package com.giftandgo.converter.service;

import com.giftandgo.converter.model.OutcomeFile;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileConvertible {

    OutcomeFile convertFile(@NonNull MultipartFile file, @NonNull String ip, @NonNull String uri);

}
