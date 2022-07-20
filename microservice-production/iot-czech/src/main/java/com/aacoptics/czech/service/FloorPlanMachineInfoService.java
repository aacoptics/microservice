package com.aacoptics.czech.service;

import com.aacoptics.czech.entity.FloorPlanMachineInfo;
import com.aacoptics.czech.entity.StatusInfo;
import com.aacoptics.czech.entity.TemperaturePlotInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FloorPlanMachineInfoService extends IService<FloorPlanMachineInfo> {
    /**
     * 根据楼层获取机台信息
     * @param startNumber 楼层起始机台号
     * @param endNumber 楼层最终机台号
     * @return 该楼层所有机台信息
     */
    List<FloorPlanMachineInfo> getMachineInfoByFloor(int startNumber, int endNumber);

    /**
     * 获取所有机台信息
     * @return 所有机台信息
     */
    List<FloorPlanMachineInfo> getAllMachineInfo();

    /**
     * 根据楼层获取机台信息
     * @param floorNumber
     * @return 该楼层所有机台信息
     */
    List<FloorPlanMachineInfo> getMachineInfoByFloorNumber(int floorNumber);

    /**
     * 根据机台号获取机台信息
     * @param machineNumber
     * @return 机台信息
     */
    FloorPlanMachineInfo getMachineInfoByMachineNumber(int machineNumber);

    /**
     * 获取一段时间内的spindle t
     * @param startTime,endTime,machineNumaber
     * @return spindle t
     */
    List<TemperaturePlotInfo> getSpindleTemperature(String startTime, String endTime, int machineNumber);

    /**
     * 获取一段时间内的air t
     * @param startTime,endTime,machineNumaber
     * @return air t
     */
    List<TemperaturePlotInfo> getAirTemperature(String startTime, String endTime, int machineNumber);

    /**
     * 获取一段时间内的bearing t
     * @param startTime,endTime,machineNumaber
     * @return bearing t
     */
    List<TemperaturePlotInfo> getBearingTemperature(String startTime, String endTime, int machineNumber);

    /**
     * 获取一段时间内的motor t
     * @param startTime,endTime,machineNumaber
     * @return motor t
     */
    List<TemperaturePlotInfo> getMotorTemperature(String startTime, String endTime, int machineNumber);

    /**
     * 根据机台号获取状态信息
     * @param machineNumber
     * @return status
     */
    List<StatusInfo> getStatusInfoByMachineNumber(int machineNumber);


}
