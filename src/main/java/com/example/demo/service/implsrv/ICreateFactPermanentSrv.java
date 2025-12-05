package com.example.demo.service.implsrv;

import com.example.demo.service.dto.ClickDto;
import com.example.demo.service.dto.ImpressionDto;

import java.util.List;

public interface ICreateFactPermanentSrv {
    void saveImpression(List<ImpressionDto> data);
    void saveClicks(List<ClickDto> data);
}
