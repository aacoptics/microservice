package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.XxlGroupInfo;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper

public interface XxlGroupInfoMapper extends BaseMapper<XxlGroupInfo> {
}