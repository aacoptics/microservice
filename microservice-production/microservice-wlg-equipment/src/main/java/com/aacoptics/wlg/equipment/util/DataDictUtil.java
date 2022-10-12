package com.aacoptics.wlg.equipment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDictUtil {

    public static HashMap<String, String> convertDataDictListToMap(List<HashMap<String, Object>> dataDictList)
    {
        HashMap<String, String> dataDictMap = new HashMap<>();
        for(HashMap<String, Object> hashMap : dataDictList)
        {
            dataDictMap.put(hashMap.get("dictValue")+"", hashMap.get("dictLabel")+"");
        }

        return dataDictMap;
    }
}
