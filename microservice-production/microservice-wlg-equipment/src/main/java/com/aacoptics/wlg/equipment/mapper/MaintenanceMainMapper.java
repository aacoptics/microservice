package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MaintenanceMainMapper extends BaseMapper<MaintenanceMain> {

    @DS("WLGIOT")
    List<MaintenanceMain> findMaintenanceMainAndItemList(@Param("maintenanceQueryParam") MaintenanceQueryParam maintenanceQueryParam);


}
