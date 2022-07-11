package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.ScrapedTool;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ScrapedToolMapper extends BaseMapper<ScrapedTool> {

    @DS("moldMes")
    List<ScrapedTool> getScrapedList(@Param("startTime") String startTime);
}
