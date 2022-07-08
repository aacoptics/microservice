package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.CustomerRequirementQueryParam;
import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.CustomerRequirementMapper;
import com.aacoptics.wlg.report.service.CustomerRequirementService;
import com.aacoptics.wlg.report.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CustomerRequirementServiceImpl extends ServiceImpl<CustomerRequirementMapper, CustomerRequirement> implements CustomerRequirementService {

    @Autowired
    private CustomerRequirementMapper customerRequirementMapper;

    @Override
    @Transactional
    public void importCustomerRequirementExcel(InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();

        //获取当月一号
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.from(currentLocalDate.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(0);//标题行

        String projectNameTitle = titleArray[0];
        String requirementDateTitle = titleArray[1];
        if ((!"项目名".equals(projectNameTitle)) || (!"需求月份".equals(requirementDateTitle))) {
            throw new BusinessException("Excel模板错误，请确认!");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }

            String projectName = dataArray[0]; //项目
            String requirementDateStr = dataArray[1]; //需求月份
            String qtyStr = dataArray[2]; //需求数量
            String targetYieldStr = dataArray[3];//目标总产量

            if (StringUtils.isEmpty(projectName)) {
                throw new BusinessException("第" + (i + 1) + "行，项目不能为空");
            }
            if (StringUtils.isEmpty(requirementDateStr)) {
                throw new BusinessException("第" + (i + 1) + "行，需求日期不能为空");
            }
            if (StringUtils.isEmpty(qtyStr)) {
                throw new BusinessException("第" + (i + 1) + "行，数量不能为空");
            }
            if (StringUtils.isEmpty(targetYieldStr)) {
                throw new BusinessException("第" + (i + 1) + "行，目标总产量不能为空");
            }
            if (StringUtils.isEmpty(requirementDateStr)) {
                break;
            }
            BigDecimal qty = new BigDecimal(qtyStr);
            BigDecimal targetYield = new BigDecimal(targetYieldStr);

            LocalDate requirementDate = null;
            try {
                requirementDate = LocalDate.parse(requirementDateStr+"-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                log.error("日期格式错误", e);
                throw new BusinessException("日期格式错误" + e.getMessage());
            }

            CustomerRequirement customerRequirement = null;
            if (monthStart.toLocalDate().isAfter(requirementDate)) {
                continue;
            }
            else {
                customerRequirement = this.queryCustomerRequirement(projectName, requirementDate);
                if (customerRequirement == null) {
                    customerRequirement = new CustomerRequirement();
                }
            }

            customerRequirement.setProjectName(projectName);
            customerRequirement.setRequirementDate(requirementDate);
            customerRequirement.setQty(qty);
            customerRequirement.setTargetYield(targetYield);

            this.saveOrUpdate(customerRequirement);
        }
    }

    @Override
    public List<CustomerRequirement> getAll() {
        return this.list();
    }

    @Override
    public IPage<CustomerRequirement> query(Page page, CustomerRequirementQueryParam customerRequirementQueryParam) {
        QueryWrapper<CustomerRequirement> queryWrapper = customerRequirementQueryParam.build();

        queryWrapper.eq(StringUtils.isNotBlank(customerRequirementQueryParam.getProjectName()), "project_name", customerRequirementQueryParam.getProjectName());

        if (customerRequirementQueryParam.getRequirementDateStart() != null) {
            queryWrapper.ge("requirement_date", customerRequirementQueryParam.getRequirementDateStart());
        }
        if (customerRequirementQueryParam.getRequirementDateEnd() != null) {
            queryWrapper.le("requirement_date", customerRequirementQueryParam.getRequirementDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("requirement_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public CustomerRequirement queryCustomerRequirement(String projectName, LocalDate requirementDate) {
        QueryWrapper<CustomerRequirement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("requirement_date", requirementDate);

        List<CustomerRequirement> customerRequirementList = customerRequirementMapper.selectList(queryWrapper);
        if (customerRequirementList != null && customerRequirementList.size() > 0) {
            return customerRequirementList.get(0);
        }
        return null;
    }

    @Override
    public IPage<Map<String, Object>> queryCustomerRequirementByCondition(Page page, CustomerRequirementQueryParam customerRequirementQueryParam) {

        page.addOrder(OrderItem.asc("project_name"));
        page.addOrder(OrderItem.asc("requirement_date"));

        IPage<Map<String, Object>> customerRequirements = customerRequirementMapper.queryCustomerRequirementByCondition(page,
                customerRequirementQueryParam.getProjectName(),
                customerRequirementQueryParam.getRequirementDateStart(),
                customerRequirementQueryParam.getRequirementDateEnd());

        return customerRequirements;
    }


    @Override
    public boolean add(CustomerRequirement customerRequirement) {

        this.validationCustomerRequirement(customerRequirement);

        boolean isSuccess = this.save(customerRequirement);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(CustomerRequirement customerRequirement) {
        this.validationCustomerRequirement(customerRequirement);

        boolean isSuccess = this.updateById(customerRequirement);
        return isSuccess;
    }


    @Override
    public CustomerRequirement get(Long id) {
        CustomerRequirement customerRequirement = this.getById(id);
        if (Objects.isNull(customerRequirement)) {
            throw new BusinessException("data not found with id:" + id);
        }
        return customerRequirement;
    }



    /**
     * 校验数据
     *
     * @param customerRequirement
     */
    private void validationCustomerRequirement(CustomerRequirement customerRequirement)
    {
        String projectName = customerRequirement.getProjectName();
        BigDecimal qty = customerRequirement.getQty();
        LocalDate requirementDate = customerRequirement.getRequirementDate();
        if(StringUtils.isEmpty(projectName))
        {
            throw new BusinessException("项目不能为空");
        }
    }

    @Override
    public int queryCustomerRequirementCountByProjectName(String projectName) {
        QueryWrapper<CustomerRequirement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectName);

        Integer count = customerRequirementMapper.selectCount(queryWrapper);
        return count;
    }
}
