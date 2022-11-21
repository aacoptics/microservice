package com.aacoptics.budget.report.service;


import com.aacoptics.budget.report.entity.param.ProductionCostBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.ProductionCostBudget;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ProductionCostBudgetService {


    /**
     * 根据条件查询生产成本预算
     *
     * @return
     */
    Map<String, Object> query(Page page, ProductionCostBudgetQueryParam productionCostBudgetQueryParam);


    /**
     * 根据条件查询生产成本预算
     *
     * @return
     */
    Map<String, Object> findByUploadLogId(Long uploadLogId);


    /**
     * 根据条件查询生产成本预算
     *
     * @return
     */
    Map<String, Object> findByCondition(String businessDivision, List<String> productLineList);


    /**
     * 更新上传生产成本信息
     *
     * @param productionCostBudget
     */
    boolean update(ProductionCostBudget productionCostBudget);

    /**
     * 根据id删除上传生产成本
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增上传生产成本
     *
     * @param productionCostBudget
     * @return
     */
    boolean add(ProductionCostBudget productionCostBudget);



    /**
     * 获取上传生产成本
     *
     * @param id
     * @return
     */
    ProductionCostBudget get(Long id);


    /**
     * 解析Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importExcel(String originalFilename, MultipartFile file,  InputStream in) throws Exception;

}
