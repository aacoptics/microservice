package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.FanucCheckDataEveryDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FanucCheckDataEveryDayMapper extends BaseMapper<FanucCheckDataEveryDay> {

    List<String> getAllMachineName();

    List<String> getAllMoldFileName();

}