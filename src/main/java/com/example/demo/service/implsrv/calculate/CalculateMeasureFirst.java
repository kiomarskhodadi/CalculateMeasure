package com.example.demo.service.implsrv.calculate;

import com.example.demo.service.dto.FactAppCountry;
import com.example.demo.service.dto.ImpressionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CalculateMeasureFirst extends ACalculatorMeasure {


    public HashMap<String,HashMap<Long, FactAppCountry>> calculateMeasure(Object data){
        HashMap<String,HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>>> impressions = (HashMap<String, HashMap<Long, HashMap<Long, ArrayList<ImpressionDto>>>>) data;
        HashMap<String,HashMap<Long, FactAppCountry>> retVal = new HashMap<>();
        HashMap<Long, FactAppCountry> countryFact;
        FactAppCountry fact;
        List<ImpressionDto> allImpressions;

        for(String countryCode :impressions.keySet()){
            for(Long appId: impressions.get(countryCode).keySet()){
                allImpressions = impressions.get(countryCode).get(appId).values().stream().flatMap(List::stream).toList();
                fact = new FactAppCountry(
                        appId,countryCode,
                        allImpressions.size(),
                        allImpressions.stream().mapToInt(impression -> impression.getClicks().size()).sum(),
                        allImpressions.stream().flatMap(impression -> impression.getClicks().stream())
                                .mapToDouble(cl -> cl.getRevenue()).sum()
                );
                countryFact = retVal.get(countryCode);
                countryFact = Objects.isNull(countryFact)? new HashMap<>():countryFact;
                countryFact.put(appId,fact);
                retVal.put(countryCode,countryFact);
            }
        }
        return retVal;
    }
    public ArrayList<FactAppCountry>  createOutput(Object data){
        HashMap<String,HashMap<Long, FactAppCountry>> input = (HashMap<String, HashMap<Long, FactAppCountry>>) data;
        return input.values().stream()
                .flatMap(countryMap -> countryMap.values().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
