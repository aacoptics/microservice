package com.aacoptics.budget.report.service;


import com.aacoptics.budget.report.entity.param.BudgetUploadLogQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface BudgetUploadLogService {


    /**
     * 根据条件查询
     *
     * @return
     */
    IPage<BudgetUploadLog> query(Page page, BudgetUploadLogQueryParam budgetUploadLogQueryParam);



    /**
     * 更新上传日志信息
     *
     * @param budgetUploadLog
     */
    boolean update(BudgetUploadLog budgetUploadLog);

    /**
     * 根据id删除上传日志
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增上传日志
     *
     * @param budgetUploadLog
     * @return
     */
    boolean add(BudgetUploadLog budgetUploadLog);



    /**
     * 获取上传日志
     *
     * @param id
     * @return
     */
    BudgetUploadLog get(Long id);


}
