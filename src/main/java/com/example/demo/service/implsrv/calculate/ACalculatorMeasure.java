package com.example.demo.service.implsrv.calculate;

import com.example.demo.service.isrv.ICalculateMeasure;
import com.example.demo.service.implsrv.FileSrv;

import java.util.*;

public abstract class ACalculatorMeasure implements ICalculateMeasure {
    public FileSrv factFileUtil;

    public boolean writeFile(Object data, String outputFilePath, String fileName){
        boolean retVal = false;
        factFileUtil.writeFile((List) data,outputFilePath,fileName);
        return retVal;
    }

    public Object cal(Object data){
        Object measure = calculateMeasure(data);
        Object measureArr = createOutput(measure);
        return measureArr;
    }


}
