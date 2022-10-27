package com.aacoptics.budget.report.service.impl;

import com.aacoptics.budget.report.entity.param.ProductLinePermissionQueryParam;
import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.ProductLinePermissionMapper;
import com.aacoptics.budget.report.provider.UserProvider;
import com.aacoptics.budget.report.service.ProductLinePermissionService;
import com.aacoptics.common.core.vo.Result;
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
public class ProductLinePermissionServiceImpl extends ServiceImpl<ProductLinePermissionMapper, ProductLinePermission> implements ProductLinePermissionService {

    @Autowired
    private ProductLinePermissionMapper productLinePermissionMapper;

    @Autowired
    private UserProvider userProvider;


    @Override
    public List<ProductLinePermission> getAll() {
        return this.list();
    }

    @Override
    public IPage<ProductLinePermission> query(Page page, ProductLinePermissionQueryParam productLinePermissionQueryParam) {
        QueryWrapper<ProductLinePermission> queryWrapper = productLinePermissionQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(productLinePermissionQueryParam.getBusinessDivision()), "business_division", productLinePermissionQueryParam.getBusinessDivision());
        queryWrapper.eq(StringUtils.isNotBlank(productLinePermissionQueryParam.getProductLine()), "product_line", productLinePermissionQueryParam.getProductLine());

        queryWrapper.orderByAsc("business_division");
        queryWrapper.orderByAsc("product_line");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(ProductLinePermission productLinePermission) {

        this.validationProductLinePermission(productLinePermission);

        boolean isSuccess = this.save(productLinePermission);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProductLinePermission productLinePermission) {
        this.validationProductLinePermission(productLinePermission);

        boolean isSuccess = this.updateById(productLinePermission);
        return isSuccess;
    }


    @Override
    public ProductLinePermission get(Long id) {
        ProductLinePermission productLinePermission = this.getById(id);
        if (Objects.isNull(productLinePermission)) {
            throw new BusinessException("data not found with id:" + id);
        }
        return productLinePermission;
    }

    @Override
    public List<ProductLinePermission> getByUserCode(String userCode) {
        QueryWrapper<ProductLinePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_code", userCode);

        List<ProductLinePermission> productLinePermissionList = this.list(queryWrapper);

        return productLinePermissionList;
    }



    /**
     * 校验数据
     *
     * @param productLinePermission
     */
    private void validationProductLinePermission(ProductLinePermission productLinePermission)
    {
        String businessDivision = productLinePermission.getBusinessDivision();
        String productLine = productLinePermission.getProductLine();
        String userCode = productLinePermission.getUserCode();

        Result result = userProvider.getByUsername(userCode);
        if(result.isFail())
        {
            throw new BusinessException("查询用户异常," + result.getMsg());
        }
        Object userObject = result.getData();
        if(userObject == null)
        {
            throw new BusinessException("用户【" + userCode + "】不存在，请确认！");
        }

        QueryWrapper<ProductLinePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_division", businessDivision);
        queryWrapper.eq("product_line", productLine);
        queryWrapper.eq("user_code", userCode);


        List<ProductLinePermission> productLinePermissionList = productLinePermissionMapper.selectList(queryWrapper);
        if (productLinePermissionList != null && productLinePermissionList.size() > 0) {
            if(productLinePermission.getId() != null && productLinePermission.getId() != 0) {
                if(!productLinePermissionList.get(0).getId().equals(productLinePermission.getId()))
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