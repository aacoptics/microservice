package com.aacoptics.pack.service.impl;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.mapper.CustomerShipmentInfoMapper;
import com.aacoptics.pack.service.ICustomerShipmentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CustomerShipmentInfoService extends ServiceImpl<CustomerShipmentInfoMapper, CustomerShipmentInfo> implements ICustomerShipmentInfoService {

    @Resource
    CustomerShipmentInfoMapper customerShipmentInfoMapper;

    @Override
    public boolean add(CustomerShipmentInfo customerShipmentInfo) {
        return this.save(customerShipmentInfo);
    }

    @Override
    public CustomerShipmentInfo getByOrderNo(String customer, String orderNo) {
        QueryWrapper<CustomerShipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo)
                .eq("customer", customer);
        return customerShipmentInfoMapper.selectOne(queryWrapper);
    }
}
