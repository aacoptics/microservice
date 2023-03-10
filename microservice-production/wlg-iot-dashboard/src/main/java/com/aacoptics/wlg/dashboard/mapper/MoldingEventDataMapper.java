package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.po.MoldingEventData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingMK4Data;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface MoldingEventDataMapper extends BaseMapper<MoldingEventData> {

    IPage<MoldingEventData> getMachineEvents(Page page, @Param("machineName") String machineName,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    IPage<MoldingEventData> getMachineErrors(Page page, @Param("machineName") String machineName,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    IPage<MoldingEventData> getMachineStatus(Page page, @Param("machineName") List<String> machineName,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    IPage<MoldingMK4Data> getMoldingMK4Data(Page page, @Param("machineName") List<String> machineName,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    IPage<MoldingEventData> getMachineSingleStatus(Page page, @Param("machineName") List<String> machineName, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}