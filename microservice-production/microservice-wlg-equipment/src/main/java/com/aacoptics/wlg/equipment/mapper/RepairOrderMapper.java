package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {


    @DS("WLGIOT")
    IPage<RepairOrderVO> findRepairOrderList(@Param("page")  Page page,
                                                     @Param("repairOrderQueryParam")  RepairOrderQueryParam repairOrderQueryParam);

    @DS("WLGIOT")
    List<RepairOrderVO> findRepairOrderList(@Param("repairOrderQueryParam")  RepairOrderQueryParam repairOrderQueryParam);

    @DS("WLGIOT")
    List<RepairOrderVO> findOrderByCondition(Map<String, String> conditionMap);
}
