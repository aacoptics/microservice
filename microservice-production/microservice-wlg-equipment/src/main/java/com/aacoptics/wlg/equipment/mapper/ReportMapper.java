package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.param.SectionSummaryOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.vo.SectionSummaryOrderVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReportMapper {


    @DS("WLGIOT")
    List<SectionSummaryOrderVO> findSectionOrderCount(@Param("sectionSummaryOrderQueryParam") SectionSummaryOrderQueryParam sectionSummaryOrderQueryParam);

}
