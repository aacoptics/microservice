package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MaintenanceOrderItemMapper extends BaseMapper<MaintenanceOrderItem> {


}
