package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.SectionSummaryOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.vo.SectionSummaryOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ReportService {



    /**
     * 根据条件查询按工段统计工单数量
     *
     * @return
     */
   List<SectionSummaryOrderVO> findSectionOrderCount(SectionSummaryOrderQueryParam sectionSummaryOrderQueryParam);
}
