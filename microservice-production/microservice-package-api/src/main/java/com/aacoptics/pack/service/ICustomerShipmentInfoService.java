package com.aacoptics.pack.service;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ICustomerShipmentInfoService  extends IService<CustomerShipmentInfo> {

    boolean add(CustomerShipmentInfo customerShipmentInfo);

    CustomerShipmentInfo getByOrderNo(String customer, String orderNo);
}
