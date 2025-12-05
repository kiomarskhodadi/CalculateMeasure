package com.example.demo.dao.repo;

import com.example.demo.dao.entity.Impression;
import com.example.demo.service.dto.FactAppCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IImpressionRepo extends JpaRepository<Impression,String> {
    @Query( "SELECT imp.app_id,imp.country_code FROM Impression imp                "+
            " JOIN FETCH Click clk  on (imp.id = clk.impression_id)                " +
            "    group by imp.app_id,imp.country_code "
    )
    Optional<List<FactAppCountry>> getProviders();

}
