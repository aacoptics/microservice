package com.aacoptics.data.analysis.service;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.AllData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IAllDataService {
    Page<AllData> getAllDataByConditionsWithPage(Page<AllData> page, String category, String project, String partName,
                                                 String material, String department,String lensNumber);

    List<AllData> getAllDataByConditions(QueryParams queryParams);
}
