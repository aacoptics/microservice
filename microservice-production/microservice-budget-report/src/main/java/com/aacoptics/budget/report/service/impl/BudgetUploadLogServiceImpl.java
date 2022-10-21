package com.aacoptics.budget.report.service.impl;


import com.aacoptics.budget.report.constants.BudgetTypeConstants;
import com.aacoptics.budget.report.entity.param.BudgetUploadLogQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.entity.po.ProductLineBudget;
import com.aacoptics.budget.report.entity.po.ProductionCostBudget;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.BudgetUploadLogMapper;
import com.aacoptics.budget.report.mapper.ProductLineBudgetMapper;
import com.aacoptics.budget.report.mapper.ProductionCostBudgetMapper;
import com.aacoptics.budget.report.mapper.ResearchBudgetMapper;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.budget.report.service.ResearchBudgetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class BudgetUploadLogServiceImpl extends ServiceImpl<BudgetUploadLogMapper, BudgetUploadLog> implements BudgetUploadLogService {

    @Resource
    ResearchBudgetMapper researchBudgetMapper;

    @Resource
    ProductLineBudgetMapper productLineBudgetMapper;

    @Resource
    ProductionCostBudgetMapper productionCostBudgetMapper;

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

    @Transactional
    @Override
    public boolean delete(Long id) {
        BudgetUploadLog budgetUploadLog = this.get(id);
        if(BudgetTypeConstants.RESEARCH_BUDGET.equals(budgetUploadLog.getType()))
        {
            QueryWrapper<ResearchBudget> researchBudgetQueryWrapper = new QueryWrapper<>();
            researchBudgetQueryWrapper.eq("upload_log_id", id);
            researchBudgetMapper.delete(researchBudgetQueryWrapper);
        }
        else if(BudgetTypeConstants.PRODUCTION_COST_BUDGET.equals(budgetUploadLog.getType()))
        {
            QueryWrapper<ProductionCostBudget> productionCostBudgetQueryWrapper = new QueryWrapper<>();
            productionCostBudgetQueryWrapper.eq("upload_log_id", id);
            productionCostBudgetMapper.delete(productionCostBudgetQueryWrapper);
        }
        else if(BudgetTypeConstants.PRODUCT_LINE_BUDGET.equals(budgetUploadLog.getType()))
        {
            QueryWrapper<ProductLineBudget> productLineBudgetQueryWrapper = new QueryWrapper<>();
            productLineBudgetQueryWrapper.eq("upload_log_id", id);
            productLineBudgetMapper.delete(productLineBudgetQueryWrapper);
        }
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
