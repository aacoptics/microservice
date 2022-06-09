package com.aacoptics.pack.mapper;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShipmentBatchInfoMapper extends BaseMapper<ShipmentBatchInfo> {

    List<ShipmentBatchInfo> getShipmentBatchInfo(@Param("customer") String customer, @Param("orderNo") String orderNo);
}
