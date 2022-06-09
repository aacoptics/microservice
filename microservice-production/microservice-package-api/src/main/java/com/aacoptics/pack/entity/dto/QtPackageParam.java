package com.aacoptics.pack.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QtPackageParam implements Serializable {

    @JSONField(name = "AsnNo")
    public String AsnNo;

    @JSONField(name = "EmsNo")
    public String EmsNo;

    @JSONField(name = "ItemCode")
    public String ItemCode;

    @JSONField(name = "MpartSpec")
    public String MpartSpec;

    @JSONField(name = "Factoryname")
    public String Factoryname;

    @JSONField(name = "VendorCode")
    public String VendorCode;

    @JSONField(name = "PalletNoLists")
    public List<OuterBoxInfo> PalletNoLists;

    public QtPackageParam(){
        PalletNoLists = new ArrayList<>();
    }
}