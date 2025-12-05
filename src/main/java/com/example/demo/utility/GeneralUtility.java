package com.example.demo.utility;

import com.example.demo.service.dto.IBaseDto;
import com.example.demo.service.dto.ImpressionDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeneralUtility {

    public static  <T extends IBaseDto> List<T> removeDuplicatesByField(List<T> data) {

        Map<String, T> uniqueMap = new LinkedHashMap<>();

        for (T obj : data) {
            String key = obj.getId();
            if (!uniqueMap.containsKey(key)) {
                uniqueMap.put(key, obj);
            }
        }
        return new ArrayList<>(uniqueMap.values());
    }
}
