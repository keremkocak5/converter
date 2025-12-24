package com.giftandgo.converter.controller.v1;

import com.giftandgo.converter.service.impl.FileConverterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
public class FileConverterController {

    private final FileConverterService fileConverterService;

    @PostMapping
    @Operation(summary = "Convert a file and get output filename")
    public String convertFile(HttpServletRequest request) {
        return fileConverterService.convertFile(request.getRemoteAddr());
    }

}
