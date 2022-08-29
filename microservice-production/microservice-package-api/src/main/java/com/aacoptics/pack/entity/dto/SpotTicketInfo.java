package com.aacoptics.pack.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SpotTicketInfo implements Serializable {

    @JSONField(name = "CartonNo")
    public String CartonNo;

    @JSONField(name = "Mday")
    public String Mday;

    @JSONField(name = "CartonQty")
    public Double CartonQty;

    @JSONField(name = "TrayNoLists")
    public List<Object> TrayNoLists;

    @JSONField(serialize = false)
    public Long id;

    public SpotTicketInfo(){
        TrayNoLists = new ArrayList<>();
        String jsonStr = "{\n" +
                "    \"TrayNo\":\"\",\n" +
                "    \"TrayQty\":0,\n" +
                "    \"SnCodeLists\":[\n" +
                "        {\n" +
                "            \"QrCode\":\"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONObject json = JSONObject.parseObject(jsonStr);
        TrayNoLists.add(json);
    }

}
