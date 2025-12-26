package com.giftandgo.converter.controller.v1;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.service.FileConvertable;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.giftandgo.converter.util.Constants.VALID_FILE_FORMAT;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
@Validated
public class FileConverterController {

    private final FileConvertable fileConverterService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert a file and get output filename")
    public String convertFile(@Valid @NonNull @RequestParam("file") MultipartFile file,
                              @Valid @NonNull HttpServletRequest request) {
        if (!VALID_FILE_FORMAT.equals(file.getContentType())) {
            throw new ConverterRuntimeException(ErrorCode.INVALID_FILE_FORMAT);
        }
        return fileConverterService.convertFile(file, request.getRemoteAddr());
    }

}
