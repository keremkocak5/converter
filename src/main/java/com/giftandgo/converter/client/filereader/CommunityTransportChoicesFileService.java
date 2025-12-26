package com.giftandgo.converter.client.filereader;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeContent;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.service.FileValidateble;
import com.giftandgo.converter.util.FileReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.giftandgo.converter.util.Constants.DELIMITER_PATTERN;

@Service
@RequiredArgsConstructor
class CommunityTransportChoicesFileService implements FileReadable {

    @Override
    public List<OutcomeContent> getValidatedFileContent(MultipartFile file, FileValidateble validator) {
        List<String[]> delimitedContent = getDelimitedContent(file);
        validator.validate(delimitedContent);
        return getParsedContent(delimitedContent);
    }

    private List<OutcomeContent> getParsedContent(List<String[]> delimitedParts) {
        try {
            return delimitedParts
                    .stream()
                    .map(delimitedPart -> new OutcomeContent(delimitedPart[2], delimitedPart[4], Double.valueOf(delimitedPart[6])))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            //log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE); // kerem buraya yeni bi exception ekle
        }
    }

    private List<String[]> getDelimitedContent(MultipartFile file) {
        try {
            return FileReaderService.read(file.getInputStream(), DELIMITER_PATTERN);
        } catch (Exception e) {
            //log.error("Cannot read file, {}", e); // kerem e mi?
            throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        }
    }

}
