package com.giftandgo.converter.controller.v1;

import com.giftandgo.converter.service.impl.FileConverterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/log")
@RequiredArgsConstructor
public class ConversionLogController {

    private final FileConverterService fileConverterService;

    @GetMapping
    @Operation(summary = "Get all conversion logs")
    public String getConversionLog() {
        return fileConverterService.convertFile("request.getRemoteAddr()");
    }

}
