package com.example.demo.service.isrv;

import com.example.demo.service.dto.ClickDto;
import com.example.demo.service.dto.ImpressionDto;
import com.example.demo.service.implsrv.ICalculateMeasure;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreateFact {
    public final FileSrv<ImpressionDto> impressionReader;
    public final FileSrv<ClickDto> clickReader;
    public final ICalculateMeasure calculateMeasureFirst;
    public final ICalculateMeasure calculateMeasureSecond;

    public CreateFact(FileSrv<ImpressionDto> impressionReader, FileSrv<ClickDto> clickReader, ICalculateMeasure calculateMeasureFirst, ICalculateMeasure calculateMeasureSecond) {
        this.impressionReader = impressionReader;
        this.clickReader = clickReader;
        this.calculateMeasureFirst = calculateMeasureFirst;
        this.calculateMeasureSecond = calculateMeasureSecond;

    }
    public void transferDataToFact(String impressionFilePath,String clickFilePath,String outputFilePath)  {
        try{
            List impression = impressionReader.readFile(impressionFilePath, ImpressionDto.class);
            List click = clickReader.readFile(clickFilePath, ClickDto.class);

            setClickToImpression(impression,click);
            HashMap<String,HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>>> newStructureImpression = changeStructure(impression);
            calculateMeasureFirst.cal(newStructureImpression,outputFilePath,"outputFirst.json");
            calculateMeasureSecond.cal(newStructureImpression,outputFilePath,"outputSecond.json");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setClickToImpression(List<ImpressionDto> impressions, List<ClickDto> clicks){
        impressions.stream().forEach(impression -> {
            impression.setClicks(clicks.stream().
                    filter(click -> click.getImpression_id().equals(impression.getId())).
                    collect(Collectors.toCollection(ArrayList::new)));
            impression.setSumRevenue(impression.getClicks().stream().mapToDouble(click-> click.getRevenue()).sum());
        });
    }
    public HashMap<String,HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>>> changeStructure(List<ImpressionDto> impressions){
        HashMap<String,HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>>> retVal = new HashMap<>();
        HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>> countryImpression;
        HashMap<Long,ArrayList<ImpressionDto>> countryAppImpression;
        ArrayList<ImpressionDto> countryAppAdvertiserImpression;
        for(ImpressionDto impression:impressions){
            countryImpression = retVal.get(impression.getCountry_code());
            countryImpression = Objects.isNull(countryImpression)? new HashMap<>():countryImpression;
            countryAppImpression = countryImpression.get(impression.getApp_id());
            countryAppImpression = Objects.isNull(countryAppImpression)? new HashMap<>(): countryAppImpression;
            countryAppAdvertiserImpression = countryAppImpression.get(impression.getAdvertiser_id());
            countryAppAdvertiserImpression = Objects.isNull(countryAppAdvertiserImpression)?new ArrayList<>():countryAppAdvertiserImpression;
            countryAppAdvertiserImpression.add(impression);
            countryAppImpression.put(impression.getAdvertiser_id(),countryAppAdvertiserImpression);
            countryImpression.put(impression.getApp_id(),countryAppImpression);
            retVal.put(impression.getCountry_code(),countryImpression);
        }
        return retVal;
    }

}
