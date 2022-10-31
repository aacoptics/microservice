package com.aacoptics.budget.report.service;


import com.aacoptics.budget.report.entity.param.BusinessDivisionProductLineQueryParam;
import com.aacoptics.budget.report.entity.po.BusinessDivisionProductLine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface BusinessDivisionProductLineService {


    /**
     * 获取所有事业部产品线配置
     *
     * @return
     */
    List<BusinessDivisionProductLine> getAll();



    /**
     * 获取所有事业部
     *
     * @return
     */
    List<String> getAllBusinessDivision();

    /**
     * 获取产品线
     *
     * @return
     */
    List<String> getProductLineByBusinessDivision(String businessDivision);


    /**
     * 根据条件查询事业部产品线配置
     *
     * @return
     */
    IPage<BusinessDivisionProductLine> query(Page page, BusinessDivisionProductLineQueryParam businessDivisionProductLineQueryParam);


    /**
     * 更新事业部产品线信息
     *
     * @param businessDivisionProductLine
     */
    boolean update(BusinessDivisionProductLine businessDivisionProductLine);

    /**
     * 根据id删除事业部产品线
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增事业部产品线
     *
     * @param businessDivisionProductLine
     * @return
     */
    boolean add(BusinessDivisionProductLine businessDivisionProductLine);



    /**
     * 获取事业部产品线
     *
     * @param id
     * @return
     */
    BusinessDivisionProductLine get(Long id);
}
