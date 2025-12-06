package com.example.demo.dao.repo;

import com.example.demo.dao.entity.Impression;
import com.example.demo.service.dto.FactAppCountry;
import com.example.demo.service.dto.FactAppCountryAdvertiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IImpressionRepo extends JpaRepository<Impression,String> {
    @Query( "SELECT new com.example.demo.service.dto.FactAppCountry(imp.app_id,imp.country_code,CAST(COUNT(imp) AS integer ))        " +
            " FROM Impression imp                               " +
            "group by imp.app_id,imp.country_code               ")
    List<FactAppCountry> cntImpressionAppCountry();


@Query(value = "select app_Id,country_Code,advertiser_id                                                                " +
               " from (                                                                                                 " +
               "select app_Id,                                                                                          " +
               "       country_Code,                                                                                    " +
               "       advertiser_id,                                                                                   " +
               "       rank()  OVER (PARTITION BY app_Id,country_Code ORDER BY sum_revenue DESC) AS dept_salary_rank    " +
               "from (                                                                                                  " +
               "SELECT i.app_Id,                                                                                        " +
               "       i.country_Code,                                                                                  " +
               "       i.advertiser_id,                                                                                 " +
               "       SUM(c.revenue) sum_revenue                                                                       " +
               "   FROM ad_mng.click c                                                                                  " +
               "   JOIN ad_mng.impression i ON c.impression_Id = i.id                                                   " +
               "   GROUP BY i.app_Id, i.country_Code,i.advertiser_id)) f                                                " +
               "where f.dept_salary_rank < :topN                                                                        ",
                nativeQuery = true)
    List<Object[]> TopNImpressionAppCountryAdvertiser(int topN);
}
