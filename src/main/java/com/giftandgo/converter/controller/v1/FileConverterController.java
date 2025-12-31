package com.giftandgo.converter.controller.v1;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.FileConvertible;
import com.giftandgo.converter.util.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.giftandgo.converter.util.Constants.MAX_FILE_SIZE;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
@Validated
public class FileConverterController {

    private final FileConvertible fileConverterService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert a file and return output file")
    public ResponseEntity<Resource> convertFile(@Valid @NonNull @RequestParam("file") MultipartFile file,
                                                @Valid @NonNull HttpServletRequest request) {
        checkFileSize(file);
        OutcomeFile outcomeFile = fileConverterService.convertFile(file, IpUtil.getClientIp(request), request.getRequestURI());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + outcomeFile.fileName())
                .body(new InputStreamResource(outcomeFile.inputStream()));
    }

    private static void checkFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ConverterRuntimeException(ErrorCode.FILE_SIZE);
        }
    }

}
