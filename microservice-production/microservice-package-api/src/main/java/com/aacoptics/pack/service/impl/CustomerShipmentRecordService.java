package com.aacoptics.pack.service.impl;

import com.aacoptics.pack.entity.po.CustomerShipmentRecord;
import com.aacoptics.pack.mapper.CustomerShipmentRecordMapper;
import com.aacoptics.pack.service.ICustomerShipmentRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerShipmentRecordService extends ServiceImpl<CustomerShipmentRecordMapper, CustomerShipmentRecord> implements ICustomerShipmentRecordService {
}
