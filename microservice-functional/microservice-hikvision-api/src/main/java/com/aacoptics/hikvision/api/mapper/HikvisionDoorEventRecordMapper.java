package com.aacoptics.hikvision.api.mapper;

import com.aacoptics.hikvision.api.entity.po.HikvisionDoorEventRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HikvisionDoorEventRecordMapper extends BaseMapper<HikvisionDoorEventRecord> {
}