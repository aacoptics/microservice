package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.po.InputReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface InputReportMapper extends BaseMapper<InputReport> {
    List<InputReport> getByDateAndMachineName(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("machineNames") List<String> machineNames);

    InputReport getTwoHourSum();


    /**
     * 获取output汇总
     * @param startTime
     * @return
     */
    List<Map<String, Object>> getMachineOutputSummary(@Param("startTime") LocalDateTime startTime);


    Map<String, Object> getMoldingMachineTotalQty(@Param("startTime") LocalDateTime startTime);



}