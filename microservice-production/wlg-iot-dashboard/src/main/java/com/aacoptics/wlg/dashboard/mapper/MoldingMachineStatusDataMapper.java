package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.po.MoldingMachineStatusData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MoldingMachineStatusDataMapper extends BaseMapper<MoldingMachineStatusData> {


    /**
     * 获取机台当前状态
     * @param machineName
     * @return
     */
    MoldingMachineStatusData getTopOneMachineStatusData(@Param("machineName") String machineName);


    /**
     * 获取ACTIVATED总时长
     * @param machineName
     * @return
     */
    Long getMachineActivatedTime(@Param("machineName") String machineName, @Param("startTime") LocalDateTime startTime);

    /**
     * 获取ACTIVATED总时长
     * @param startTime
     * @return
     */
    Long getMachineActivatedTotalTime(@Param("startTime") LocalDateTime startTime);

    /**
     * 获取ACTIVATED总机台数
     *
     * @param startTime
     * @return
     */
    Long getMachineActivatedCount(@Param("startTime") LocalDateTime startTime);

    /**
     * 统计设备状态数量
     */
    List<Map<String, Object>> getMachineStatusSummary();
}