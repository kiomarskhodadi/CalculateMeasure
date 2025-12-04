package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class FactAppCountry extends ABaseDto  {
    private Long app_id;
    private String country_code;
    private Integer cntImpressions;
    private Integer cntClicks;
    private double sumRevenue;

    public FactAppCountry(Long app_id, String country_code, Integer cntImpressions, Integer cntClicks, double sumRevenue) {
        this(new Timestamp(System.currentTimeMillis()),app_id,country_code,cntImpressions,cntClicks,sumRevenue);
    }

    public FactAppCountry(Timestamp creationDate, Long app_id, String country_code, Integer cntImpressions, Integer cntClicks, double sumRevenue) {
        super(creationDate);
        this.app_id = app_id;
        this.country_code = country_code;
        this.cntImpressions = cntImpressions;
        this.cntClicks = cntClicks;
        this.sumRevenue = sumRevenue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactAppCountry that = (FactAppCountry) o;
        return app_id.equals(that.app_id) && country_code.equals(that.country_code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app_id, country_code);
    }

    public void sumFact(FactAppCountry fact){
        this.setSumRevenue(this.getSumRevenue()+ fact.getSumRevenue());
        this.setCntClicks(this.getCntClicks() + fact.getCntClicks());
        this.setCntImpressions(this.getCntImpressions()+ fact.cntImpressions);
    }


}
