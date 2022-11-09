package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderDetailVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderVO;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MaintenanceOrderMapper extends BaseMapper<MaintenanceOrder> {

    @DS("WLGIOT")
    List<MaintenanceMain> findMaintenanceList();

    @DS("WLGIOT")
    IPage<MaintenanceOrderVO> findMaintenanceOrderList(@Param("page")  Page page,
                                                     @Param("maintenanceOrderQueryParam")  MaintenanceOrderQueryParam maintenanceOrderQueryParam);


    @DS("WLGIOT")
    IPage<MaintenanceOrderDetailVO> findMaintenanceOrderDetailList(@Param("page")  Page page,
                                                                   @Param("maintenanceOrderQueryParam")  MaintenanceOrderQueryParam maintenanceOrderQueryParam);


    @DS("WLGIOT")
    List<MaintenanceOrderAndItemVO> findMaintenanceOrderAndItemList(@Param("maintenanceOrderQueryParam")  MaintenanceOrderQueryParam maintenanceOrderQueryParam);

    @DS("WLGIOT")
    MaintenanceOrderAndItemVO findOrderByMchCode(@Param("mchCode")  String mchCode);

    @DS("WLGIOT")
    List<String> findMaintenanceTimeoutOrderDutyPersonIdList();
}
