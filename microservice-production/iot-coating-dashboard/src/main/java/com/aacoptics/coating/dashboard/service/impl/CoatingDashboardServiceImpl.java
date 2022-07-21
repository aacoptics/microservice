package com.aacoptics.coating.dashboard.service.impl;

import com.aacoptics.coating.dashboard.dao.LensCoatingMachineNoodDataMapper;
import com.aacoptics.coating.dashboard.entity.CoatingStatusEntities;
import com.aacoptics.coating.dashboard.entity.CoatingStatusEntity;
import com.aacoptics.coating.dashboard.entity.LensCoatingMachineNoodData;
import com.aacoptics.coating.dashboard.service.CoatingDashboardService;
import com.aacoptics.coating.dashboard.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CoatingDashboardServiceImpl implements CoatingDashboardService {

    @Autowired
    LensCoatingMachineNoodDataMapper lensCoatingMachineNoodDataMapper;

    @Value("${coating.web-api-url}")
    private String webApiUrl;

    @Override
    public CoatingStatusEntities getCoatingMachineStatus() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse("2021-07-06 07:00:00", df);
        List<LensCoatingMachineNoodData> nodeDataList = lensCoatingMachineNoodDataMapper.getMachineTotalCount(ldt);
        JSONObject statusRes = HttpClientUtil.doGet(webApiUrl + "/Opc/GetAllLoadingStatus");
        CoatingStatusEntities allMachineStatus = JSONObject.toJavaObject(statusRes, CoatingStatusEntities.class);
        if (allMachineStatus.getSite() != null && allMachineStatus.getSite().size() > 0) {
            for (CoatingStatusEntity coatingStatusEntity : allMachineStatus.getSite()) {
                Optional<LensCoatingMachineNoodData> nodeDataOpt = nodeDataList.stream()
                        .filter(item -> item.getMachineId().equals(coatingStatusEntity.getName()))
                        .findFirst();
                LensCoatingMachineNoodData nodeData = nodeDataOpt.orElse(null);
                if (nodeData == null) {
                    coatingStatusEntity.setTotalNums(0);
                } else {
                    coatingStatusEntity.setTotalNums(nodeData.getTotalNums());
                }
            }
        }
        return allMachineStatus;
    }

    @Override
    public JSONObject getCoatingMachineAlarmInfo() {
        return HttpClientUtil.doGet(webApiUrl + "/Opc/GetAllAlarmCode");
    }

    @Override
    public Map<String, Map<String, Integer>> getStatusCount() {
        Map<String, Map<String, Integer>> coatingStatusCount = new HashMap<>();
        JSONObject statusRes = HttpClientUtil.doGet(webApiUrl + "/Opc/GetAllLoadingStatus");
        CoatingStatusEntities allMachineStatus = JSONObject.toJavaObject(statusRes, CoatingStatusEntities.class);
        if (allMachineStatus.getSite() != null && allMachineStatus.getSite().size() > 0) {
            List<CoatingStatusEntity> allMachineStatusData = allMachineStatus.getSite();
            for (CoatingStatusEntity statusData : allMachineStatusData) {
                String floor = checkCoatingPhase(statusData.getName());
                if (!coatingStatusCount.containsKey(floor)) {
                    Map<String, Integer> statusNums = new HashMap<>();
                    coatingStatusCount.put(floor, statusNums);
                }
                String statusCode = "-1";
                if (statusData.isIsOnline()) {
                    statusCode = statusData.getStatus().toString();
                }
                if (!coatingStatusCount.get(floor).containsKey(statusCode)) {
                    coatingStatusCount.get(floor).put(statusCode, 0);
                }
                coatingStatusCount.get(floor).put(statusCode, coatingStatusCount.get(floor).get(statusCode) + 1);
            }
        }
        return coatingStatusCount;
    }

    private String checkCoatingPhase(String machineName) {
        if (machineName.startsWith("A") ||
                machineName.startsWith("B") ||
                machineName.startsWith("C") ||
                machineName.startsWith("D") ||
                machineName.startsWith("E") ||
                machineName.startsWith("F") ||
                machineName.startsWith("L")) {
            return "1期";
        } else {
            return "2期";
        }
    }
}