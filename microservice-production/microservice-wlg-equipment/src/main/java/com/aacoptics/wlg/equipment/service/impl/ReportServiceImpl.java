package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.param.SectionSummaryOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.vo.SectionSummaryOrderVO;
import com.aacoptics.wlg.equipment.mapper.ReportMapper;
import com.aacoptics.wlg.equipment.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Resource
    ReportMapper reportMapper;

    @Override
    public List<SectionSummaryOrderVO> findSectionOrderCount(SectionSummaryOrderQueryParam sectionSummaryOrderQueryParam) {
        List<SectionSummaryOrderVO> sectionSummaryOrderVOList = reportMapper.findSectionOrderCount(sectionSummaryOrderQueryParam);
        return sectionSummaryOrderVOList;
    }
}
