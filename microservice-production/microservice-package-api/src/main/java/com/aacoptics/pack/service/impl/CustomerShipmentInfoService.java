package com.aacoptics.pack.service.impl;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.mapper.CustomerShipmentInfoMapper;
import com.aacoptics.pack.service.ICustomerShipmentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerShipmentInfoService extends ServiceImpl<CustomerShipmentInfoMapper, CustomerShipmentInfo> implements ICustomerShipmentInfoService {
}
