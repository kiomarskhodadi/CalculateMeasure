package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IBaseDto {
     boolean validation();
     @JsonIgnore
     String getId();
}
