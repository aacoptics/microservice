package com.aacoptics.wlg.report.service;



import com.aacoptics.wlg.report.entity.param.ProductionPlanQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionPlan;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProductionPlanService {
    /**
     * 解析计划数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importProductionPlanExcel(String fileName, InputStream in) throws Exception;


    /**
     * 获取所有计划数据
     *
     * @return
     */
    List<ProductionPlan> getAll();


    /**
     * 根据条件查询计划数据
     *
     * @return
     */
    IPage<ProductionPlan> query(Page page, ProductionPlanQueryParam productionPlanQueryParam);

    /**
     * 根据条件查询计划数据
     *
     * @return
     */
    List<Map<String, Object>> queryProductionPlanByMonth(ProductionPlanQueryParam productionPlanQueryParam);


    /**
     * 根据条件查询计划数据标题
     *
     * @return
     */
    JSONArray queryProductionPlanTitleByMonth(ProductionPlanQueryParam productionPlanQueryParam);


    /**
     * 查询存在的记录
     *
     * @param code 条件代码
     * @param projectName 项目
     * @param planDate 计划日期
     * @return
     */
    ProductionPlan queryProductionPlan(String projectName,
                                                              String mold,
                                                              String cycle,
                                                              String code,
                                                              LocalDate planDate);
}
