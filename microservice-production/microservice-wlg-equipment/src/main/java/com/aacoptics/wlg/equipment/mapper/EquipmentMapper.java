package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

    @DS("EAM_DB")
    List<Map<String, Object>> findEquipmentByEAM(@Param("assetGeneralCode") String assetGeneralCode, @Param("areaCode") String areaCode);

    @DS("WLGIOT")
    List<String> findMchNameList();


    @DS("WLGIOT")
    List<String> findSpecListByMchName(@Param("mchName") String mchName);


    @DS("WLGIOT")
    List<String> findTypeVersionListByMchNameAndSpec(@Param("mchName") String mchName, @Param("spec") String spec);

}
