package com.giftandgo.converter.controller.v1;

import com.giftandgo.converter.config.ControllerAdvisor;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.FileConvertable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static com.giftandgo.converter.util.Constants.MAX_FILE_SIZE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FileConverterControllerTest {

    @Mock
    private FileConvertable fileConverterService;

    @InjectMocks
    private FileConverterController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ControllerAdvisor()).build();
    }

    @Test
    void convertFileShouldReturnConvertedFile() throws Exception {
        byte[] content = "{\"status\":\"ok\"}".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "input.json", MediaType.APPLICATION_JSON_VALUE, content);

        OutcomeFile outcomeFile = new OutcomeFile("output.json", new ByteArrayInputStream(content));
        when(fileConverterService.convertFile(any(), anyString(), anyString())).thenReturn(outcomeFile);

        mockMvc.perform(multipart("/v1/file").file(file))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    byte[] responseBytes = result.getResponse().getContentAsByteArray();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    assert responseString.contains("{\"status\":\"ok\"}");
                });
    }

    @Test
    void convertFileShouldFailWhenFileIsMissing() throws Exception {
        mockMvc.perform(multipart("/v1/file"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void convertFileShouldFailWhenFileTooLarge() throws Exception {
        byte[] largeContent = new byte[(int) (MAX_FILE_SIZE + 1)];

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "large.txt",
                MediaType.TEXT_PLAIN_VALUE,
                largeContent
        );

        mockMvc.perform(multipart("/v1/file").file(file))
                .andExpect(status().isBadRequest());
    }

}
