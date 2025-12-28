package com.giftandgo.converter.controller.v1;

import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.FileConvertable;
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

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
@Validated
public class FileConverterController {

    private final FileConvertable fileConverterService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert a file and get output")
    public ResponseEntity<Resource> convertFile(@Valid @NonNull @RequestParam("file") MultipartFile file,
                                                @Valid @NonNull HttpServletRequest request) {
        OutcomeFile outcomeFile = fileConverterService.convertFile(file, IpUtil.getClientIp(request), request.getRequestURI());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, // neden bu kerem
                        "attachment; filename=" + outcomeFile.fileName())
                .body(new InputStreamResource(outcomeFile.inputStream()));
    }

}
