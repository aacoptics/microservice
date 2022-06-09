package com.aacoptics.pack.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class OuterBoxInfo {
    @JSONField(name = "PalletNo")
    public String PalletNo;

    @JSONField(name = "DateCode")
    public String DateCode;

    @JSONField(name = "PalletQty")
    public Double PalletQty;

    @JSONField(name = "CartonNoLists")
    public List<SpotTicketInfo> CartonNoLists;

    public OuterBoxInfo(){
        CartonNoLists = new ArrayList<>();
    }

}
