package com.example.demo.dao.entity;

import com.example.demo.service.dto.ImpressionDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "IMPRESSION",schema = "AD_MNG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Impression {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "APP_ID")
    private Long app_id;
    @Column(name = "ADVERTISER_ID")
    private Long advertiser_id;
    @Column(name = "COUNTRY_CODE")
    private String country_code;

    public Impression(ImpressionDto dto){
        this(dto.getId(),dto.getApp_id(),dto.getAdvertiser_id(),dto.getCountry_code());
    }
}
