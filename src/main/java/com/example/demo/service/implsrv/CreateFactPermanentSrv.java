package com.example.demo.service.implsrv;

import com.example.demo.dao.repo.IClickRepo;
import com.example.demo.dao.repo.IImpressionRepo;
import com.example.demo.service.dto.*;
import com.example.demo.service.isrv.ICreateFactPermanentSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CreateFactPermanentSrv implements ICreateFactPermanentSrv {


    private final JdbcTemplate jdbcTemplate;

    private final IImpressionRepo impressionRepo;
    private final IClickRepo clickRepo;

    public CreateFactPermanentSrv(JdbcTemplate jdbcTemplate, IImpressionRepo impressionRepo, IClickRepo clickRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.impressionRepo = impressionRepo;
        this.clickRepo = clickRepo;
    }

    public void saveImpression(List<ImpressionDto> data){
        try{
            List<CompletableFuture<Boolean>> resultInsert = new ArrayList<>();
            int stepIndex = Math.min(100, data.size());
            int lastIndex = 0;
            do {
                resultInsert.add(saveAsyncImpression(data.subList(lastIndex,stepIndex)));
                lastIndex = stepIndex;
                stepIndex = Math.min(stepIndex + 100, data.size());
            }while(data.size() > lastIndex);
            CompletableFuture<Void> allInserts = CompletableFuture.allOf(resultInsert.toArray(new CompletableFuture[0]));
            allInserts.get();
        }catch (Exception e){
            log.error("Error in Insert",e);
        }
    }

    public void saveClicks(List<ClickDto> data){
        try{
            List<CompletableFuture<Boolean>> resultInsert = new ArrayList<>();
            int stepIndex = Math.min(100, data.size());
            int lastIndex = 0;
            do {
                resultInsert.add(saveAsyncClick(data.subList(lastIndex,stepIndex)));
                lastIndex = stepIndex;
                stepIndex = Math.min(stepIndex + 100, data.size());
            }while(data.size() > lastIndex);
            CompletableFuture<Void> allInserts = CompletableFuture.allOf(resultInsert.toArray(new CompletableFuture[0]));
            allInserts.get();
        }catch (Exception e){
            log.error("Error in Insert",e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async("threadPool")
    public CompletableFuture<Boolean> saveAsyncImpression(List<ImpressionDto> data){
        CompletableFuture retVal = CompletableFuture.completedFuture(true);
        try{
        String sql = " INSERT INTO AD_MNG.IMPRESSION (ID, APP_ID, ADVERTISER_ID, COUNTRY_CODE) " +
                     "  VALUES (?, ?, ?, ?)  ";
        List<Object[]> batchArgs = data.stream()
                .map(dto -> new Object[]{
                        dto.getId(),
                        dto.getApp_id(),
                        dto.getAdvertiser_id(),
                        dto.getCountry_code()
                })
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(sql, batchArgs);
        }catch (Exception e){
            retVal = CompletableFuture.completedFuture(false);
            log.error("Error in save Data",e);
        }
        return retVal;
    }

    // TODO: Transfer to Repository function
    @Transactional
    @Async("threadPool")
    public CompletableFuture<Boolean> saveAsyncClick(List<ClickDto> data){
        CompletableFuture retVal = CompletableFuture.completedFuture(true);
        try{
        String sql = " INSERT INTO AD_MNG.CLICK (ID,IMPRESSION_ID, REVENUE) " +
                     "  VALUES (?, ?, ?)  ";
        List<Object[]> batchArgs = data.stream()
                .map(dto -> new Object[]{
                        UUID.randomUUID().toString(),
                        dto.getImpression_id(),
                        dto.getRevenue()
                })
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(sql, batchArgs);
        }catch (Exception e){
            retVal = CompletableFuture.completedFuture(false);
            log.error("Error in save Data",e);
        }
        return retVal;
    }

    public List<FactAppCountry> createFactFirst(){
        List<FactAppCountry> click = loadListOfAggClick();
        List<FactAppCountry> impression = loadListOfAggImpression();
        impression.stream().forEach( imp -> {
            click.stream().
                    filter(clk -> clk.getApp_id().equals(imp.getApp_id()) && clk.getCountry_code().equals(imp.getCountry_code())).
                    findFirst().
                    ifPresent(fclk -> {
                        imp.setCntClicks(fclk.getCntClicks());
                        imp.setSumRevenue(fclk.getSumRevenue());
                    });
        });
        return impression;
    }

    // TODO: Multi Thread
    public List<FactAppCountry> loadListOfAggClick(){
        List<FactAppCountry> retVal = clickRepo.cntClkAppCountry();
        return retVal;
    }
    public List<FactAppCountry> loadListOfAggImpression(){
        List<FactAppCountry> retVal = impressionRepo.cntImpressionAppCountry();
        return retVal;
    }

    public List<FactAppCountryTopNAdvertiser> createFactTopN(int topN){

        List<FactAppCountryTopNAdvertiser> retVal ;
        HashMap<String,FactAppCountryTopNAdvertiser> factApp;
        FactAppCountryTopNAdvertiser factAppCountry;
        List<FactAppCountryAdvertiser> factAppCountryAdvertisers = loadListOfTopN(topN);
        HashMap<Long,HashMap<String,FactAppCountryTopNAdvertiser>> restructure = new HashMap<>();
        for(FactAppCountryAdvertiser f:factAppCountryAdvertisers){
            factApp = restructure.get(f.getApp_id());
            factApp = Objects.isNull(factApp) ? new HashMap<>():factApp;
            factAppCountry = factApp.get(f.getCountry_code());
            factAppCountry = Objects.isNull(factAppCountry) ? new FactAppCountryTopNAdvertiser():factAppCountry;
            factAppCountry.setApp_id(f.getApp_id());
            factAppCountry.setCountry_code(f.getCountry_code());
            factAppCountry.getTopNAdvertiserId().add(f.getTopNAdvertiserId());
            factApp.put(f.getCountry_code(),factAppCountry);
            restructure.put(f.getApp_id(),factApp);
        }
        retVal = restructure.values().stream()
                    .flatMap(countryMap -> countryMap.values().stream())
                    .collect(Collectors.toCollection(ArrayList::new));
        return retVal;
    }
    // TODO: Multi Thread
    public List<FactAppCountryAdvertiser> loadListOfTopN(int topN){
        List<FactAppCountryAdvertiser> retVal ;
        List<Object[]> results = impressionRepo.TopNImpressionAppCountryAdvertiser(topN);

        retVal =  results.stream()
                .map(row -> new FactAppCountryAdvertiser(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .collect(Collectors.toList());
        return retVal;
    }

}
