package com.example.demo.dao.repo;

import com.example.demo.dao.entity.Click;
import com.example.demo.service.dto.FactAppCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClickRepo extends JpaRepository<Click,String> {

    @Query( "SELECT new com.example.demo.service.dto.FactAppCountry(imp.app_id,imp.country_code,CAST(COUNT(clk) AS integer),Sum(clk.revenue))       " +
            " FROM Impression imp                                                                                                                " +
            "inner join Click clk  on (imp.id = clk.impression_id)                                                                               " +
            "group by imp.app_id,imp.country_code                                                                                                ")
    List<FactAppCountry> cntClkAppCountry();
}
