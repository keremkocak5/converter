package com.giftandgo.converter.config;


import com.giftandgo.converter.controller.v1.FileConverterController;
import com.giftandgo.converter.service.FileConvertible;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FileConverterController.class)
@Import(ControllerAdvisor.class)
class ControllerAdvisorTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileConvertible fileConvertible;

    @Test
    void shouldReturn500WhenUnexpectedExceptionOccurs() throws Exception {
        byte[] content = "ok".getBytes();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        when(fileConvertible.convertFile(any(), any(), any()))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(multipart("/v1/file").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.detail")
                        .value("Internal Server Error"));
    }

}