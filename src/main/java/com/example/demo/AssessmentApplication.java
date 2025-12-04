package com.example.demo;

import com.example.demo.service.dto.ClickDto;
import com.example.demo.service.dto.FactAppCountry;
import com.example.demo.service.dto.FactAppCountryTopNAdvertiser;
import com.example.demo.service.dto.ImpressionDto;
import com.example.demo.service.implsrv.ICalculateMeasure;
import com.example.demo.service.isrv.*;
import com.example.demo.service.isrv.calculate.CalculateMeasureFirst;
import com.example.demo.service.isrv.calculate.CalculateMeasureSecond;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssessmentApplication {


    public static void main(String[] args) {
        SpringApplication.run(AssessmentApplication.class, args);
        if (args.length < 3) {
            System.exit(1);
        }
        String impressionsFile = args[0];
        String clicksFile = args[1];
        String outputPath = args[2];

        FileSrv<ImpressionDto> impressionReader = new FileSrv<>(new ObjectMapper());
        FileSrv<ClickDto> clickReader = new FileSrv<>(new ObjectMapper());
        FileSrv<FactAppCountry> factReaderFileUtil = new FileSrv<>(new ObjectMapper());
        FileSrv<FactAppCountryTopNAdvertiser> factTopNAdvertiserFileUtil = new FileSrv<>(new ObjectMapper());
        ICalculateMeasure calculateMeasureFirst = new CalculateMeasureFirst(factReaderFileUtil);
        ICalculateMeasure calculateMeasureSecond = new CalculateMeasureSecond(factTopNAdvertiserFileUtil);
        CreateFact dataPreparation = new CreateFact(impressionReader,clickReader, calculateMeasureFirst, calculateMeasureSecond);
        dataPreparation.transferDataToFact(impressionsFile,clicksFile,outputPath);
    }
}