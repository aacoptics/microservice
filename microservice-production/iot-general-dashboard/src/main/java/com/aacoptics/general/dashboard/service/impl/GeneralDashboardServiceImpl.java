package com.aacoptics.general.dashboard.service.impl;

import com.aacoptics.general.dashboard.entity.LensPackerAlarmInfo;
import com.aacoptics.general.dashboard.provider.CoatingProvider;
import com.aacoptics.general.dashboard.provider.FanucProvider;
import com.aacoptics.general.dashboard.provider.LensPackerProvider;
import com.aacoptics.general.dashboard.service.GeneralDashboardService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeneralDashboardServiceImpl implements GeneralDashboardService {
    @Autowired
    private CoatingProvider coatingProvider;

    @Autowired
    private LensPackerProvider lensPackerProvider;

    @Autowired
    private FanucProvider fanucProvider;

    @Override
    public Map<String, Integer> getGeneralStatusCount() {
        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("自动运转", 0);
        statusCount.put("设备报警", 0);
        statusCount.put("低温保持", 0);
        statusCount.put("上料预警", 0);
        statusCount.put("离线", 0);
        statusCount.put("其他", 0);
        JSONObject lensPackerData = (JSONObject) JSONObject.toJSON(lensPackerProvider.getStatusCount().getData());
        JSONObject fanucData = (JSONObject) JSONObject.toJSON(fanucProvider.getStatusCount().getData());
        JSONObject coatingData = (JSONObject) JSONObject.toJSON(coatingProvider.getStatusCount().getData());
        if(lensPackerData != null) {
            for (Map.Entry entry : lensPackerData.entrySet()) {
                JSONObject childrenEntrySet = (JSONObject) JSONObject.toJSON(entry.getValue());
                for (Map.Entry childrenEntry : childrenEntrySet.entrySet()) {
                    if (childrenEntry.getKey().equals("0")) {
                        statusCount.put("离线", statusCount.get("离线") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("1")) {
                        statusCount.put("自动运转", statusCount.get("自动运转") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("2")) {
                        statusCount.put("设备报警", statusCount.get("设备报警") + (Integer) childrenEntry.getValue());
                    }
                }
            }
        }
        if(fanucData != null) {
            for (Map.Entry entry : fanucData.entrySet()) {
                JSONObject childrenEntrySet = (JSONObject) JSONObject.toJSON(entry.getValue());
                for (Map.Entry childrenEntry : childrenEntrySet.entrySet()) {
                    if (childrenEntry.getKey().equals("-1")) {
                        statusCount.put("离线", statusCount.get("离线") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("02")) {
                        statusCount.put("自动运转", statusCount.get("自动运转") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("03")) {
                        statusCount.put("设备报警", statusCount.get("设备报警") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("17")) {
                        statusCount.put("低温保持", statusCount.get("低温保持") + (Integer) childrenEntry.getValue());
                    } else {
                        statusCount.put("其他", statusCount.get("其他") + (Integer) childrenEntry.getValue());
                    }
                }
            }
        }

        if(coatingData != null) {
            for (Map.Entry entry : coatingData.entrySet()) {
                JSONObject childrenEntrySet = (JSONObject) JSONObject.toJSON(entry.getValue());
                for (Map.Entry childrenEntry : childrenEntrySet.entrySet()) {
                    if (childrenEntry.getKey().equals("-1")) {
                        statusCount.put("离线", statusCount.get("离线") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("0")) {
                        statusCount.put("自动运转", statusCount.get("自动运转") + (Integer) childrenEntry.getValue());
                    } else if (childrenEntry.getKey().equals("1")) {
                        statusCount.put("上料预警", statusCount.get("上料预警") + (Integer) childrenEntry.getValue());
                    }
                }
            }
        }
        return statusCount;
    }

    @Override
    public List<LensPackerAlarmInfo> getGeneralCurrentAlarm() {
        JSONArray alarmInfo = (JSONArray) JSONArray.toJSON(lensPackerProvider.getCurrentAlarm().getData());
        JSONArray FanucInfo = (JSONArray) JSONArray.toJSON(fanucProvider.getAllStatus().getData());
        List<LensPackerAlarmInfo> allAlarms = new ArrayList<>();
        if(alarmInfo != null) {
            allAlarms = alarmInfo.toJavaList(LensPackerAlarmInfo.class);
            for (LensPackerAlarmInfo alarm : allAlarms) {
                Duration duration = Duration.between(alarm.getStartTime(), LocalDateTime.now());
                alarm.setDuration(duration.getSeconds());
                alarm.setMachineType("包料机");
            }
        }
        for (int i = 0; i < FanucInfo.size(); i++) {
            if (FanucInfo.getJSONObject(i).get("monitStatus").equals("03")) {
                LensPackerAlarmInfo alarm = new LensPackerAlarmInfo();
                alarm.setMachineNo("3B" + FanucInfo.getJSONObject(i).get("monitMcName").toString());
                alarm.setAlarmCode("Alarm");
                alarm.setAlarmDesc("报警");
                alarm.setMachineType("注塑机");
                allAlarms.add(alarm);
            }
        }
        return allAlarms;
    }
}
