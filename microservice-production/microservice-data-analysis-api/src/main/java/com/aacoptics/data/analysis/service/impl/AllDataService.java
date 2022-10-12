package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.AllData;
import com.aacoptics.data.analysis.mapper.AllDataMapper;
import com.aacoptics.data.analysis.service.IAllDataService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AllDataService implements IAllDataService {

    @Autowired
    AllDataMapper allDataMapper;

    @Override
    public Page<AllData> getAllDataByConditionsWithPage(Page<AllData> page, String category, String project, String partName, String material) {
        return allDataMapper.getAllDataByConditionsWithPage(page, category, project, partName, material);
    }

    @Override
    public List<AllData> getAllDataByConditions(QueryParams queryParams) {
        return allDataMapper.getAllDataByConditions(queryParams.getCategory(), queryParams.getProject(), queryParams.getPartName(), queryParams.getMaterial());
    }
}
