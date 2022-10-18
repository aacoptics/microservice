package com.aacoptics.hikvision.api.service;


import com.aacoptics.hikvision.api.entity.po.HikvisionDoorEventRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface HikvisionDoorEventRecordService extends IService<HikvisionDoorEventRecord> {
    void getHikvisionDoorEventRecord();
}
