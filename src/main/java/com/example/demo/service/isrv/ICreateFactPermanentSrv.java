package com.example.demo.service.isrv;

import com.example.demo.service.dto.ClickDto;
import com.example.demo.service.dto.FactAppCountry;
import com.example.demo.service.dto.FactAppCountryTopNAdvertiser;
import com.example.demo.service.dto.ImpressionDto;

import java.util.List;

public interface ICreateFactPermanentSrv {
    void saveImpression(List<ImpressionDto> data);
    void saveClicks(List<ClickDto> data);
    List<FactAppCountry> createFactFirst();
    List<FactAppCountryTopNAdvertiser> createFactTopN(int topN);
}
