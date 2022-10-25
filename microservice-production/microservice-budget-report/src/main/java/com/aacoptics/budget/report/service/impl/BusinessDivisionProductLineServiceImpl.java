package com.aacoptics.budget.report.service.impl;

import com.aacoptics.budget.report.entity.param.BusinessDivisionProductLineQueryParam;
import com.aacoptics.budget.report.entity.po.BusinessDivisionProductLine;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.BusinessDivisionProductLineMapper;
import com.aacoptics.budget.report.service.BusinessDivisionProductLineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BusinessDivisionProductLineServiceImpl extends ServiceImpl<BusinessDivisionProductLineMapper, BusinessDivisionProductLine> implements BusinessDivisionProductLineService {

    @Autowired
    private BusinessDivisionProductLineMapper businessDivisionProductLineMapper;


    @Override
    public List<BusinessDivisionProductLine> getAll() {
        return this.list();
    }


    @Override
    public List<String> getAllBusinessDivision() {
       return businessDivisionProductLineMapper.getAllBusinessDivision();
    }

    @Override
    public List<String> getProductLineByBusinessDivision(String businessDivision) {
        return businessDivisionProductLineMapper.getProductLineByBusinessDivision(businessDivision);
    }



    @Override
    public IPage<BusinessDivisionProductLine> query(Page page, BusinessDivisionProductLineQueryParam businessDivisionProductLineQueryParam) {
        QueryWrapper<BusinessDivisionProductLine> queryWrapper = businessDivisionProductLineQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(businessDivisionProductLineQueryParam.getBusinessDivision()), "business_division", businessDivisionProductLineQueryParam.getBusinessDivision());
        queryWrapper.eq(StringUtils.isNotBlank(businessDivisionProductLineQueryParam.getProductLine()), "product_line", businessDivisionProductLineQueryParam.getProductLine());

        queryWrapper.orderByAsc("business_division");
        queryWrapper.orderByAsc("product_line");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(BusinessDivisionProductLine businessDivisionProductLine) {

        this.validationBusinessDivisionProductLine(businessDivisionProductLine);

        boolean isSuccess = this.save(businessDivisionProductLine);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(BusinessDivisionProductLine businessDivisionProductLine) {
        this.validationBusinessDivisionProductLine(businessDivisionProductLine);

        boolean isSuccess = this.updateById(businessDivisionProductLine);
        return isSuccess;
    }


    @Override
    public BusinessDivisionProductLine get(Long id) {
        BusinessDivisionProductLine businessDivisionProductLine = this.getById(id);
        if (Objects.isNull(businessDivisionProductLine)) {
            throw new BusinessException("data not found with id:" + id);
        }
        return businessDivisionProductLine;
    }




    /**
     * 校验数据
     *
     * @param businessDivisionProductLine
     */
    private void validationBusinessDivisionProductLine(BusinessDivisionProductLine businessDivisionProductLine)
    {
        String businessDivision = businessDivisionProductLine.getBusinessDivision();
        String productLine = businessDivisionProductLine.getProductLine();


        QueryWrapper<BusinessDivisionProductLine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_division", businessDivision);
        queryWrapper.eq("product_line", productLine);


        List<BusinessDivisionProductLine> businessDivisionProductLineList = businessDivisionProductLineMapper.selectList(queryWrapper);
        if (businessDivisionProductLineList != null && businessDivisionProductLineList.size() > 0) {
            if(businessDivisionProductLine.getId() != null && businessDivisionProductLine.getId() != 0) {
                if(!businessDivisionProductLineList.get(0).getId().equals(businessDivisionProductLine.getId()))
                {
                    throw new BusinessException("已存在相同记录，请确认！");
                }
            }
            else
            {
                throw new BusinessException("已存在相同记录，请确认！");
            }
        }
    }
}