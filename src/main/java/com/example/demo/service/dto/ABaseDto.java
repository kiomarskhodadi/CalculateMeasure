package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ABaseDto implements IBaseDto{
    private Timestamp creationDate;
    @Override
    public boolean validation() {
        return true;
    }

    public String getId(){
        return null;
    }


}
