package com.aacoptics.fanuc.dashboard.consumer;//package com.aacoptics.fanuc.dashboard.consumer;
//
//import com.aacoptics.fanuc.dashboard.entity.FanucDataEntity;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.TreeMap;
//
//
//@Component
//@Slf4j
//public class FanucConsumer {
//    public static Map<String, FanucDataEntity> FanucMachineDataMap = new TreeMap<>();
//
//    public void handleMessage(Object result) {
//        JSONObject resJson = (JSONObject) JSONObject.toJSON(result);
//        JSONArray baseDataArray = (JSONArray) resJson.get("fanucDataMessages");
//        for (Object baseData : baseDataArray) {
//            JSONObject baseDataJson = (JSONObject) baseData;
//            String mcName = baseDataJson.getString("machineNo");
//            if (mcName.contains("delete"))
//                continue;
//            if (FanucMachineDataMap.containsKey(mcName)) {
//                FanucMachineDataMap.get(mcName).setMonitData(baseDataJson.get("monitData"));
//                FanucMachineDataMap.get(mcName).setCondData(baseDataJson.get("condData"));
//                FanucMachineDataMap.get(mcName).setMoldFileName(baseDataJson.getString("moldFileName"));
//            } else {
//                FanucDataEntity fanucDataEntity = new FanucDataEntity();
//                fanucDataEntity.setMonitData(baseDataJson.get("monitData"));
//                fanucDataEntity.setCondData(baseDataJson.get("condData"));
//                fanucDataEntity.setMoldFileName(baseDataJson.getString("moldFileName"));
//                FanucMachineDataMap.put(mcName, fanucDataEntity);
//            }
//        }
//
//        JSONArray moldDataArray = (JSONArray) resJson.get("fanucMoldMessages");
//        for (Object moldData : moldDataArray) {
//            JSONObject moldDataJson = (JSONObject) moldData;
//            String mcName = moldDataJson.getString("monit_mc_name");
//            if (mcName.contains("delete"))
//                continue;
//            if (FanucMachineDataMap.containsKey(mcName)) {
//                FanucMachineDataMap.get(mcName).setMoldData(moldData);
//            } else {
//                FanucDataEntity fanucDataEntity = new FanucDataEntity();
//                fanucDataEntity.setMoldData(moldData);
//                FanucMachineDataMap.put(mcName, fanucDataEntity);
//            }
//        }
//        //log.info("Received Message:<{}>", result);
//        // 待实现动态del路由
//    }
//}