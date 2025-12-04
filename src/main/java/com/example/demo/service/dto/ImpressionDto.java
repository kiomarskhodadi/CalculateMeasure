package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImpressionDto extends ABaseDto implements Comparable<ImpressionDto>{

    private String id;
    private Long app_id;
    private Long advertiser_id;
    private String country_code;
    private ArrayList<ClickDto> clicks;
    private double sumRevenue;

    @Override
    public boolean validation() {
        return StringUtils.hasLength(id) &&
                Objects.nonNull(app_id) &&
                StringUtils.hasLength(country_code) &&
                Objects.nonNull(advertiser_id);
    }

    @Override
    public int compareTo(ImpressionDto o) {
        int retVal = 0;
        if(o.getSumRevenue() > this.getSumRevenue()){
            retVal = 1;
        }else if(o.getSumRevenue() < this.getSumRevenue()){
            retVal = -1;
        }
        return retVal;
    }
}
