package com.aacoptics.budget.report.service.impl;


import com.aacoptics.budget.report.entity.param.BudgetUploadLogQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.BudgetUploadLogMapper;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class BudgetUploadLogServiceImpl extends ServiceImpl<BudgetUploadLogMapper, BudgetUploadLog> implements BudgetUploadLogService {



    @Override
    public IPage<BudgetUploadLog> query(Page page, BudgetUploadLogQueryParam budgetUploadLogQueryParam) {
        QueryWrapper<BudgetUploadLog> queryWrapper = budgetUploadLogQueryParam.build();
        queryWrapper.like(StringUtils.isNotBlank(budgetUploadLogQueryParam.getType()), "type", budgetUploadLogQueryParam.getType());
        queryWrapper.like(StringUtils.isNotBlank(budgetUploadLogQueryParam.getExcelName()), "excel_name", budgetUploadLogQueryParam.getExcelName());

        queryWrapper.orderByAsc("excel_name");
        queryWrapper.orderByDesc("upload_time");
        return this.page(page, queryWrapper);
    }



    @Override
    public boolean add(BudgetUploadLog budgetUploadLog) {
        boolean isSuccess = this.save(budgetUploadLog);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(BudgetUploadLog budgetUploadLog) {

        boolean isSuccess = this.updateById(budgetUploadLog);
        return isSuccess;
    }


    @Override
    public BudgetUploadLog get(Long id) {
        BudgetUploadLog budgetUploadLog = this.getById(id);
        if (Objects.isNull(budgetUploadLog)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return budgetUploadLog;
    }

}
