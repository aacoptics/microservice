package com.aacoptics.pack.service.impl;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.aacoptics.pack.mapper.CustomerShipmentInfoMapper;
import com.aacoptics.pack.mapper.ShipmentBatchInfoMapper;
import com.aacoptics.pack.service.ICustomerShipmentInfoService;
import com.aacoptics.pack.service.IShipmentBatchInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShipmentBatchInfoService extends ServiceImpl<ShipmentBatchInfoMapper, ShipmentBatchInfo> implements IShipmentBatchInfoService {

    @Resource
    ShipmentBatchInfoMapper shipmentBatchInfoMapper;

    @Override
    public List<ShipmentBatchInfo> getShipmentBatchInfo(String customer, String orderNo) {
        return shipmentBatchInfoMapper.getShipmentBatchInfo(customer, orderNo);
    }

    @Override
    public Page<ShipmentBatchInfo> getShipmentBatchInfo(Page<ShipmentBatchInfo> iPage,
                                                      String customer,
                                                      String orderNo) {
        return shipmentBatchInfoMapper.getShipmentBatchInfoPage(iPage, customer, orderNo);
    }
}
