package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.MoldingAbnormalData;
import com.aacoptics.wlg.dashboard.entity.MoldingEventData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Mapper
public interface MoldingAbnormalDataMapper extends BaseMapper<MoldingAbnormalData> {

    IPage<MoldingAbnormalData> getMachineAbnormalData(Page page, @Param("machineName") String machineName,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}