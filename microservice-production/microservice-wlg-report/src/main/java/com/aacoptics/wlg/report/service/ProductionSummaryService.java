package com.aacoptics.wlg.report.service;


import com.aacoptics.wlg.report.entity.param.ProductionSummaryQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionSummary;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProductionSummaryService {
    /**
     * 解析生产汇总Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importProductionSummaryExcel(InputStream in) throws Exception;


    /**
     * 获取所有生产汇总
     *
     * @return
     */
    List<ProductionSummary> getAll();


    /**
     * 根据条件查询生产汇总
     *
     * @return
     */
    IPage<ProductionSummary> query(Page page, ProductionSummaryQueryParam productionSummaryQueryParam);


    /**
     * 查询存在的记录
     *
     * @param projectName
     * @return
     */
    ProductionSummary queryProductionSummary(String projectName,
                                             LocalDate requirementDate);


    /**
     * 根据条件查询生产汇总
     *
     * @return
     */
    List<Map<String, Object>> queryProductionSummaryByCondition(Page page, ProductionSummaryQueryParam productionSummaryQueryParam);

    /**
     * 更新客户需求信息
     *
     * @param productionSummary
     */
    boolean update(ProductionSummary productionSummary);

    /**
     * 根据id删除客户需求
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增客户需求
     *
     * @param productionSummary
     * @return
     */
    boolean add(ProductionSummary productionSummary);



    /**
     * 获取客户需求
     *
     * @param id
     * @return
     */
    ProductionSummary get(Long id);


    /**
     * 通过项目查询是否存在客户需求
     */
    int queryProductionSummaryCountByProjectName(String projectName);



    /**
     * 根据条件查询生产汇总标题
     *
     * @return
     */
    JSONArray queryProductionSummaryTitleByMonth(ProductionSummaryQueryParam productionSummaryQueryParam);

}
