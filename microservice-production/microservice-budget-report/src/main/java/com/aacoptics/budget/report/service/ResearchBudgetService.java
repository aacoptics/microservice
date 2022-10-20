package com.aacoptics.budget.report.service;


import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ResearchBudgetService {


    /**
     * 根据条件查询研发费用预算
     *
     * @return
     */
    Map<String, Object> query(Page page, ResearchBudgetQueryParam researchBudgetQueryParam);



    /**
     * 更新上传研发费用信息
     *
     * @param researchBudget
     */
    boolean update(ResearchBudget researchBudget);

    /**
     * 根据id删除上传研发费用
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增上传研发费用
     *
     * @param researchBudget
     * @return
     */
    boolean add(ResearchBudget researchBudget);



    /**
     * 获取上传研发费用
     *
     * @param id
     * @return
     */
    ResearchBudget get(Long id);


    /**
     * 解析Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importExcel(String originalFilename, InputStream in) throws Exception;

}
