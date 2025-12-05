package com.example.demo.service.isrv;

import com.example.demo.dao.entity.Click;
import com.example.demo.dao.entity.Impression;
import com.example.demo.dao.repo.IClickRepo;
import com.example.demo.dao.repo.IImpressionRepo;
import com.example.demo.service.dto.ClickDto;
import com.example.demo.service.dto.ImpressionDto;
import com.example.demo.service.implsrv.ICreateFactPermanentSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

}
