package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.MatInfo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MatInfoMapper extends BaseMapper<MatInfo> {

    @DS("moldMes")
    List<MatInfo> getMatInfo();

    @DS("moldMes")
    Integer getScrapCount(@Param("startTime") String startTime);

    @DS("moldMes")
    Integer getOutCount(@Param("startTime") String startTime);
}
