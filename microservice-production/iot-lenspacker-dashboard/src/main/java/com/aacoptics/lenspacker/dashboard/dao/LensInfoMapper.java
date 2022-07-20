package com.aacoptics.lenspacker.dashboard.dao;

import com.aacoptics.lenspacker.dashboard.entity.LensInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LensInfoMapper extends BaseMapper<LensInfo> {
    List<LensInfo> getLensPackerMachineInfo();
}