package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderVO;
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
public interface InspectionOrderMapper extends BaseMapper<InspectionOrder> {

    @DS("WLGIOT")
    List<InspectionMain> findInspectionList();

    @DS("WLGIOT")
    IPage<InspectionOrderVO> findInspectionOrderList(@Param("page")  Page page,
                                                     @Param("inspectionOrderQueryParam")  InspectionOrderQueryParam inspectionOrderQueryParam);

}
