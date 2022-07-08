package com.aacoptics.wlg.report.service;



import com.aacoptics.wlg.report.entity.param.CustomerRequirementQueryParam;
import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomerRequirementService {
    /**
     * 解析模具使用情况Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importCustomerRequirementExcel(InputStream in) throws Exception;


    /**
     * 获取所有模具使用情况
     *
     * @return
     */
    List<CustomerRequirement> getAll();


    /**
     * 根据条件查询模具使用情况
     *
     * @return
     */
    IPage<CustomerRequirement> query(Page page, CustomerRequirementQueryParam customerRequirementQueryParam);


    /**
     * 查询存在的记录
     *
     * @param projectName
     * @return
     */
    CustomerRequirement queryCustomerRequirement(String projectName,
                                                     LocalDate requirementDate);


    /**
     * 根据条件查询模具使用情况
     *
     * @return
     */
    IPage<Map<String, Object>> queryCustomerRequirementByCondition(Page page, CustomerRequirementQueryParam customerRequirementQueryParam);

    /**
     * 更新客户需求信息
     *
     * @param customerRequirement
     */
    boolean update(CustomerRequirement customerRequirement);

    /**
     * 根据id删除客户需求
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增客户需求
     *
     * @param customerRequirement
     * @return
     */
    boolean add(CustomerRequirement customerRequirement);



    /**
     * 获取客户需求
     *
     * @param id
     * @return
     */
    CustomerRequirement get(Long id);


    /**
     * 通过项目查询是否存在客户需求
     */
    int queryCustomerRequirementCountByProjectName(String projectName);
}
