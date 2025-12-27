package com.giftandgo.converter.service;

import com.giftandgo.converter.model.ConvertedFile;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileConvertable {

    ConvertedFile convertFile(@NonNull MultipartFile file, @NonNull String ip);

}
