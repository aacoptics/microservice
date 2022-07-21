package com.aacoptics.coating.dashboard.dao;

import com.aacoptics.coating.dashboard.entity.LensCoatingMachineNoodData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface LensCoatingMachineNoodDataMapper extends BaseMapper<LensCoatingMachineNoodData> {
    List<LensCoatingMachineNoodData> getMachineTotalCount(@Param("dateTime")LocalDateTime dateTime);
}