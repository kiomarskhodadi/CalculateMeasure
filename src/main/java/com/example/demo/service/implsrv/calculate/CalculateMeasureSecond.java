package com.example.demo.service.implsrv.calculate;

import com.example.demo.service.dto.FactAppCountryTopNAdvertiser;
import com.example.demo.service.dto.ImpressionDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class CalculateMeasureSecond extends ACalculatorMeasure {



    public HashMap<String,HashMap<Long, FactAppCountryTopNAdvertiser>> calculateMeasure(Object data){
        HashMap<String,HashMap<Long, FactAppCountryTopNAdvertiser>> retVal = new HashMap<>();
        HashMap<String,HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>>> impressions = (HashMap<String, HashMap<Long, HashMap<Long, ArrayList<ImpressionDto>>>>) data;
        HashMap<Long, FactAppCountryTopNAdvertiser> countryFact;
        List<ImpressionDto> topN;
        FactAppCountryTopNAdvertiser fact;
        List<ImpressionDto> allImpressions;
        for(String countryCode :impressions.keySet()){
            for(Long appId: impressions.get(countryCode).keySet()){
                allImpressions = impressions.get(countryCode).get(appId).values().stream().flatMap(List::stream).toList();
                topN = getTopN(allImpressions,5);
                fact = new FactAppCountryTopNAdvertiser(appId,
                        countryCode,
                        topN.stream().map(impression -> impression.getAdvertiser_id()).
                                collect(Collectors.toCollection(ArrayList::new)));
                countryFact = retVal.get(countryCode);
                countryFact = Objects.isNull(countryFact)? new HashMap<>():countryFact;
                countryFact.put(appId,fact);
                retVal.put(countryCode,countryFact);
            }
        }
        return retVal;
    }

    public ArrayList<FactAppCountryTopNAdvertiser>  createOutput(Object data){
        HashMap<String,HashMap<Long, FactAppCountryTopNAdvertiser>> input = (HashMap<String, HashMap<Long, FactAppCountryTopNAdvertiser>>) data;
        return input.values().stream()
                .flatMap(countryMap -> countryMap.values().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<ImpressionDto> getTopN(List<ImpressionDto> impressions, int n) {
        PriorityQueue<ImpressionDto> minHeap = new PriorityQueue<>(Comparator.comparingDouble(ImpressionDto::getSumRevenue));
        Set<Long> advertiserIds = new HashSet<>();
        ImpressionDto impressionDel;
        for (ImpressionDto impression : impressions) {
            if(impression.getSumRevenue() > 0.0d){
                minHeap.offer(impression);
                if(!advertiserIds.add(impression.getAdvertiser_id())){
                    minHeap.poll();
                }else if (minHeap.size() > n) {
                    impressionDel = minHeap.poll();
                    advertiserIds.remove(impressionDel.getAdvertiser_id());
                }
            }
        }
        List<ImpressionDto> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll());
        }
        Collections.reverse(result);
        return result;
    }

}
