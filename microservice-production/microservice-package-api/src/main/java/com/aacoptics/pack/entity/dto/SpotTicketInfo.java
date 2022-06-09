package com.aacoptics.pack.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotTicketInfo implements Serializable {

    @JSONField(name = "CartonNo")
    public String CartonNo;

    @JSONField(name = "Mday")
    public String Mday;

    @JSONField(name = "CartonQty")
    public Double CartonQty;
}
