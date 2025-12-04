package com.example.demo.service.implsrv;

public interface ICalculateMeasure {

    Object calculateMeasure(Object data);
    Object createOutput(Object data);
    boolean writeFile(Object data, String outputFilePath, String fileName);
    boolean cal(Object data, String outputFilePath, String fileName);
}
