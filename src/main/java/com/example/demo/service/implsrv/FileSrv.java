package com.example.demo.service.implsrv;

import com.example.demo.service.dto.IBaseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class FileSrv<T extends IBaseDto> {

    public final ObjectMapper objectMapper;

    public FileSrv(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<T> readFile(String filePath, Class<T> clazz) throws IOException {
        List<T> retVal ;
        List<T> rawImpressions = objectMapper.readValue(
                new File(filePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
        );
        retVal = rawImpressions.stream().filter(T::validation).collect(Collectors.toList());
        return retVal;
    }
    public boolean writeFile(List<T> data,String outputFilePath,String fileName) {
        boolean retVal = !CollectionUtils.isEmpty(data);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File outputFile = null;
        try {
            if(retVal){
                outputFile = new File(outputFilePath+ fileName);
                File parentDir = outputFile.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    retVal = parentDir.mkdirs();
                    if (!retVal) {
                        log.warn("Failed to create directory: " + parentDir.getPath());
                    }
                }
            }
            if(retVal){
                objectMapper.writeValue(outputFile, data);
                log.info("Successfully wrote " + data.size() +" records to JSON file: " + outputFilePath);
            }
        } catch (IOException e) {
            log.error("Error writing JSON file: " + e.getMessage());
            retVal = false;
            e.printStackTrace();

        }
        return retVal;
    }


}
