package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface InspectionMainMapper extends BaseMapper<InspectionMain> {


}
