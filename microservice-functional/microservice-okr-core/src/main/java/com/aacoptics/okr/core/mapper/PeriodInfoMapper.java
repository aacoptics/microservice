package com.aacoptics.okr.core.mapper;

import com.aacoptics.okr.core.entity.po.PeriodInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PeriodInfoMapper extends BaseMapper<PeriodInfo> {

}