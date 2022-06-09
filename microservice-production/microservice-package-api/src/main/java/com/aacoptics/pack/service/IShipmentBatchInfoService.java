package com.aacoptics.pack.service;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IShipmentBatchInfoService extends IService<ShipmentBatchInfo> {

    List<ShipmentBatchInfo> getShipmentBatchInfo(String customer, String orderNo);
}
