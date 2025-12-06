package com.example.demo;

import com.example.demo.service.isrv.ICreateFactSrv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com"})
public class AssessmentApplication {

    @Autowired
    private ICreateFactSrv dataPreparation;

    public static String impressionsFile;
    public static String clicksFile ;
    public static String outputPath ;

    public static String useDataBase;
    public static void main(String[] args) {
        if (args.length < 3) {
            System.exit(1);
        }
        impressionsFile = args[0];
        clicksFile = args[1];
        outputPath = args[2];
        useDataBase = args[3];
        SpringApplication.run(AssessmentApplication.class, args);

    }
    @PostConstruct
    public void init() {
        dataPreparation.transferDataToFact(impressionsFile,clicksFile,outputPath,useDataBase);
    }

}