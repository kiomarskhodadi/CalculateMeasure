package com.example.demo.service.isrv;

public interface ICalculateMeasure {

    Object calculateMeasure(Object data);
    Object createOutput(Object data);
    boolean writeFile(Object data, String outputFilePath, String fileName);
    Object cal(Object data);
}
