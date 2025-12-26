package com.giftandgo.converter.service;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileConvertable {

    String convertFile(@NonNull MultipartFile file, @NonNull String ip);

}
