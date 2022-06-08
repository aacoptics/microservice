package com.aacoptics.pack.mapper;

import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CustomerShipmentInfoMapper extends BaseMapper<CustomerShipmentInfo> {
}
