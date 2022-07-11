package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.EquipInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EquipInfoMapper extends BaseMapper<EquipInfo> {

    List<EquipInfo> getMachineNames();
}
