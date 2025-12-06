package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactAppCountryAdvertiser {
    private Long app_id;
    private String country_code;
    private Long topNAdvertiserId;
}
