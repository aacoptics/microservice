package com.aacoptics.czech.service.impl;

import com.aacoptics.czech.entity.FloorPlanMachineInfo;
import com.aacoptics.czech.entity.StatusInfo;
import com.aacoptics.czech.entity.TemperaturePlotInfo;
import com.aacoptics.czech.mapper.FloorPlanMachineInfoMapper;
import com.aacoptics.czech.service.FloorPlanMachineInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FloorPlanMachineInfoServiceImpl extends ServiceImpl<FloorPlanMachineInfoMapper, FloorPlanMachineInfo> implements FloorPlanMachineInfoService {

    @Autowired
    FloorPlanMachineInfoMapper floorPlanMachineInfoMapper;

    @Override
    public List<FloorPlanMachineInfo> getMachineInfoByFloor(int startNumber, int endNumber) {
        return floorPlanMachineInfoMapper.getMachineInfoByFloor(startNumber, endNumber);
    }

    @Override
    public List<FloorPlanMachineInfo> getAllMachineInfo() {
        List<FloorPlanMachineInfo> machineInfoList = floorPlanMachineInfoMapper.getAllMachineInfo();
        for(FloorPlanMachineInfo floorPlanMachineInfo : machineInfoList) {
            floorPlanMachineInfo.setShowStatusCode(getShowStatusCode(floorPlanMachineInfo.getStatus()));
        }
        return machineInfoList;
    }

    @Override
    public List<FloorPlanMachineInfo> getMachineInfoByFloorNumber(int floorNumber) {
        List<FloorPlanMachineInfo> machineInfoList = new ArrayList<FloorPlanMachineInfo>();
        switch(floorNumber) {
            case 1:
                machineInfoList = getMachineInfoByFloor(101, 116);
                break;
            case 2:
                machineInfoList = getMachineInfoByFloor(201, 216);
                break;
            case 3:
                machineInfoList = getMachineInfoByFloor(301, 316);
                break;
            case 4:
                machineInfoList = getMachineInfoByFloor(401, 416);
                break;
            case 5:
                machineInfoList = getMachineInfoByFloor(501, 516);
                break;
            case 6:
                machineInfoList = getMachineInfoByFloor(601, 616);
                break;
            case 7:
                machineInfoList = getMachineInfoByFloor(701, 716);
                break;
            case 8:
                machineInfoList = getMachineInfoByFloor(801, 816);
                break;
            case 9:
                machineInfoList = getMachineInfoByFloor(901, 916);
                break;
        }

        //根据机台号到对应的表里获取T2字段的值
        for(FloorPlanMachineInfo floorPlanMachineInfo : machineInfoList) {
            String machineNumber = floorPlanMachineInfo.getMachineNo();
            try {
                float t2 = floorPlanMachineInfoMapper.getTemperatureByMachineNo("G" + machineNumber);
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                String temperature = decimalFormat.format(t2);
                floorPlanMachineInfo.setTemperature(temperature);
                floorPlanMachineInfo.setShowStatus(getShowStatus(floorPlanMachineInfo.getStatus()));
            } catch (Exception e) {
                floorPlanMachineInfo.setTemperature("no data");
                floorPlanMachineInfo.setShowStatus(getShowStatus(floorPlanMachineInfo.getStatus()));
            }

        }

        return machineInfoList;
    }

    @Override
    public FloorPlanMachineInfo getMachineInfoByMachineNumber(int machineNumber) {
        FloorPlanMachineInfo machineInfo = floorPlanMachineInfoMapper.getMachineInfoByMachineNumber(machineNumber);
        float t2 = floorPlanMachineInfoMapper.getTemperatureByMachineNo("G" + machineNumber);
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String temperature = decimalFormat.format(t2);
        machineInfo.setTemperature(temperature);
        machineInfo.setShowStatus(getShowStatus(machineInfo.getStatus()));
        return machineInfo;
    }

    @Override
    public List<TemperaturePlotInfo> getSpindleTemperature(String startTime, String endTime, int machineNumber) {
        return floorPlanMachineInfoMapper.getSpindleTemperature(startTime, endTime, "G" + machineNumber);
    }

    @Override
    public List<TemperaturePlotInfo> getAirTemperature(String startTime, String endTime, int machineNumber) {
        return floorPlanMachineInfoMapper.getAirTemperature(startTime, endTime, "A" + machineNumber);
    }

    @Override
    public List<TemperaturePlotInfo> getBearingTemperature(String startTime, String endTime, int machineNumber) {
        return floorPlanMachineInfoMapper.getBearingTemperature(startTime, endTime, "G" + machineNumber);
    }

    @Override
    public List<TemperaturePlotInfo> getMotorTemperature(String startTime, String endTime, int machineNumber) {
        return floorPlanMachineInfoMapper.getMotorTemperature(startTime, endTime, "G" + machineNumber);
    }

    @Override
    public List<StatusInfo> getStatusInfoByMachineNumber(int machineNumber) {
        return floorPlanMachineInfoMapper.getStatusInfoByMachineNumber(machineNumber);
    }

    private String getShowStatus(String status) {
        String showStatus = "";
        if("idle".equals(status) || "SR".equals(status) || "HMIOff".equals(status) ||
            "LoJ".equals(status) || "IPZ".equals(status) || "IPU".equals(status) ||
            "IPF".equals(status) || "LoT".equals(status) || "OMW".equals(status) ||
            "undefined".equals(status) || "TB".equals(status) || "MB".equals(status) ||
            "NoVal".equals(status)) {
            showStatus = "Idle";
        } else if("Normal".equals(status)) {
            showStatus = "Running";
        } else if("BD".equals(status)) {
            showStatus = "Failure";
        } else if("PM".equals(status)) {
            showStatus = "Maintenance";
        }
        return showStatus;
    }

    private int getShowStatusCode(String status) {
        int showStatusCode = 0;
        if("Normal".equals(status)) {
            showStatusCode = 0;
        } else if("idle".equals(status) || "SR".equals(status) || "HMIOff".equals(status) ||
                "LoJ".equals(status) || "IPZ".equals(status) || "IPU".equals(status) ||
                "IPF".equals(status) || "LoT".equals(status) || "OMW".equals(status) ||
                "undefined".equals(status) || "TB".equals(status) || "MB".equals(status) ||
                "NoVal".equals(status)) {
            showStatusCode = 1;
        } else if("BD".equals(status)) {
            showStatusCode = 2;
        } else if("PM".equals(status)) {
            showStatusCode = 3;
        }
        return showStatusCode;
    }


}
