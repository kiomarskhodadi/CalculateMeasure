package com.example.demo.dao.repo;

import com.example.demo.dao.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClickRepo extends JpaRepository<Click,String> {
}
