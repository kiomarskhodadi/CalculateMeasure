package com.example.demo.service.isrv;

import com.example.demo.service.dto.ClickDto;
import com.example.demo.service.dto.FactAppCountry;
import com.example.demo.service.dto.FactAppCountryTopNAdvertiser;
import com.example.demo.service.dto.ImpressionDto;
import com.example.demo.service.implsrv.ICalculateMeasure;
import com.example.demo.service.implsrv.ICreateFactPermanentSrv;
import com.example.demo.service.implsrv.ICreateFactSrv;
import com.example.demo.utility.GeneralUtility;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreateFactSrv implements ICreateFactSrv {
    public final FileSrv<ImpressionDto> impressionReader;

    public final FileSrv<ClickDto> clickReader;

    public final FileSrv<FactAppCountry> factFirstFileUtil;

    public final FileSrv<FactAppCountryTopNAdvertiser> factTopNAdvertiserFileUtil;

    public final ICalculateMeasure calculateMeasureFirst;

    public final ICalculateMeasure calculateMeasureSecond;

    public final ICreateFactPermanentSrv createFactPermanentSrv;



    public CreateFactSrv(FileSrv<ImpressionDto> impressionReader, FileSrv<ClickDto> clickReader, FileSrv<FactAppCountry> factFirstFileUtil, FileSrv<FactAppCountryTopNAdvertiser> factTopNAdvertiserFileUtil, ICalculateMeasure calculateMeasureFirst, ICalculateMeasure calculateMeasureSecond, ICreateFactPermanentSrv createFactPermanentSrv) {
        this.impressionReader = impressionReader;
        this.clickReader = clickReader;
        this.factFirstFileUtil = factFirstFileUtil;
        this.factTopNAdvertiserFileUtil = factTopNAdvertiserFileUtil;
        this.calculateMeasureFirst = calculateMeasureFirst;
        this.calculateMeasureSecond = calculateMeasureSecond;
        this.createFactPermanentSrv = createFactPermanentSrv;
    }
    public void transferDataToFact(String impressionFilePath,String clickFilePath,String outputFilePath,String useDataBase)  {
        try{
            List impression = impressionReader.readFile(impressionFilePath, ImpressionDto.class);
            List click = clickReader.readFile(clickFilePath, ClickDto.class);
            impression = GeneralUtility.removeDuplicatesByField(impression);
            if(useDataBase.equalsIgnoreCase("Y")){
                calculateMeasureFileBaseAndDataBase(impression,click, outputFilePath);
            }else{
                calculateMeasureFileBaseAndCreateFile(impression,click, outputFilePath);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // TODO: Add delete Function
    // TODO: Write Query For Calculate Measure First and TopN

    public void calculateMeasureFileBaseAndDataBase(List impression,List click,String outputFilePath){
        createFactPermanentSrv.saveImpression(impression);
        createFactPermanentSrv.saveClicks(click);
    }
    public void calculateMeasureFileBaseAndCreateFile(List impression,List click,String outputFilePath){
        setClickToImpression(impression,click);
        HashMap<String,HashMap<Long,HashMap<Long,ArrayList<ImpressionDto>>>> newStructureImpression = changeStructure(impression);
        Object factFirst = calculateMeasureFirst.cal(newStructureImpression);
        Object factTopN = calculateMeasureSecond.cal(newStructureImpression);
        factFirstFileUtil.writeFile((List) factFirst,outputFilePath,"outputFirst.json");
        factTopNAdvertiserFileUtil.writeFile((List) factTopN,outputFilePath,"outputSecond.json");
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
