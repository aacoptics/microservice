package com.aacoptics.mold.toollife.consumer;
import com.aacoptics.mold.toollife.entity.AreaConfig;
import com.aacoptics.mold.toollife.service.MachineAreaInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class MoldConsumer {
    @Autowired
    MachineAreaInfoService machineAreaInfoService;

    public static List<Map<String, Object>> machineStatus = new ArrayList<>();

    public void handleMessage(Object result) {
        int offlineNum = 0;
        int idleNum = 0;
        int adjustNum = 0;
        int warnNum = 0;
        int workNum = 0;
        int handModeNum = 0;
        ArrayList<LinkedHashMap<String, Object>> res = (ArrayList<LinkedHashMap<String, Object>>) result;
        AreaConfig areaConfig = machineAreaInfoService.getAreaConfig();
        Map<String, String> areaInfo = areaConfig.getAreaInfo();
        for(int i = 0; i < res.size(); i++) {
            LinkedHashMap<String, Object> cncNodeInfo = (LinkedHashMap<String, Object>)res.get(i).get("CncNode");
            String machineNo = (String) res.get(i).get("MachineNo");
            int state = (Integer) cncNodeInfo.get("State");
//            String machineName = (String) cncNodeInfo.get("MachineName");
//            if(machineName == null || machineName.length() == 0) {
//                continue;
//            }
            if(areaInfo.get(machineNo) == null) {
                continue;
            }
            if(state == 0) {
                offlineNum++;
            } else if(state == 2) {
                idleNum++;
            } else if(state == 3) {
                adjustNum++;
            } else if(state == 4) {
                warnNum++;
            } else if(state == 5) {
                workNum++;
            } else if(state == 6) {
//                handModeNum++;
                adjustNum++;
            }
        }
        machineStatus = new ArrayList<>();
        Map<String, Object> offlineMap = new HashMap<>();
        offlineMap.put("status", "??????");
        offlineMap.put("qty", offlineNum);
        machineStatus.add(offlineMap);
        Map<String, Object> idleMap = new HashMap<>();
        idleMap.put("status", "??????");
        idleMap.put("qty", idleNum);
        machineStatus.add(idleMap);
        Map<String, Object> adjustMap = new HashMap<>();
        adjustMap.put("status", "??????");
        adjustMap.put("qty", adjustNum);
        machineStatus.add(adjustMap);
        Map<String, Object> warnMap = new HashMap<>();
        warnMap.put("status", "??????");
        warnMap.put("qty", warnNum);
        machineStatus.add(warnMap);
        Map<String, Object> workMap = new HashMap<>();
        workMap.put("status", "?????????");
        workMap.put("qty", workNum);
        machineStatus.add(workMap);
//        Map<String, Object> handModeMap = new HashMap<>();
//        handModeMap.put("status", "????????????");
//        handModeMap.put("qty", handModeNum);
//        machineStatus.add(handModeMap);


//        machineStatus.put("??????", offlineNum);
//        machineStatus.put("??????", idleNum);
//        machineStatus.put("??????", adjustNum);
//        machineStatus.put("??????", warnNum);
//        machineStatus.put("?????????", workNum);
//        machineStatus.put("????????????", handModeNum);
    }
}
