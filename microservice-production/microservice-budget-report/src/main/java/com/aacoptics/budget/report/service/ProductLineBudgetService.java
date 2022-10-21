package com.aacoptics.budget.report.service;


import com.aacoptics.budget.report.entity.param.ProductLineBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.ProductLineBudget;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ProductLineBudgetService {


    /**
     * 根据条件查询产品线预算预算
     *
     * @return
     */
    Map<String, Object> query(Page page, ProductLineBudgetQueryParam productLineBudgetQueryParam);



    /**
     * 更新上传产品线预算信息
     *
     * @param productLineBudget
     */
    boolean update(ProductLineBudget productLineBudget);

    /**
     * 根据id删除上传产品线预算
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增上传产品线预算
     *
     * @param productLineBudget
     * @return
     */
    boolean add(ProductLineBudget productLineBudget);



    /**
     * 获取上传产品线预算
     *
     * @param id
     * @return
     */
    ProductLineBudget get(Long id);


    /**
     * 解析Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importExcel(String originalFilename, MultipartFile file,  InputStream in) throws Exception;

}
