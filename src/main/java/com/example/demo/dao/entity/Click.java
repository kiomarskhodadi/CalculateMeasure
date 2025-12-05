package com.example.demo.dao.entity;

import com.example.demo.service.dto.ClickDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "CLICK",schema = "AD_MNG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Click {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "IMPRESSION_ID")
    private String impression_id;
    @Column(name = "REVENUE")
    private double revenue;

    public Click(ClickDto dto){
        this(UUID.randomUUID().toString(),dto.getImpression_id(), dto.getRevenue());
    }
}
