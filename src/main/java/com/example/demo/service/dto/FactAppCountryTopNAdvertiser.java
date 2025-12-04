package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class FactAppCountryTopNAdvertiser extends ABaseDto {
    private Long app_id;
    private String country_code;
    private ArrayList<Long> topNAdvertiserId;

    public FactAppCountryTopNAdvertiser(Long app_id, String country_code, ArrayList<Long> topNAdvertiserId) {
        this(new Timestamp(System.currentTimeMillis()),app_id,country_code,topNAdvertiserId);
    }

    public FactAppCountryTopNAdvertiser(Timestamp creationDate, Long app_id, String country_code, ArrayList<Long> topNAdvertiserId) {
        super(creationDate);
        this.app_id = app_id;
        this.country_code = country_code;
        this.topNAdvertiserId = topNAdvertiserId;
    }
}
