package com.aacoptics.pack.service;

import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IShipmentBatchInfoService extends IService<ShipmentBatchInfo> {

    List<ShipmentBatchInfo> getShipmentBatchInfo(String customer, String orderNo);

    Page<ShipmentBatchInfo> getShipmentBatchInfo(Page<ShipmentBatchInfo> iPage,
                                               String customer,
                                                 String orderNo);
}
