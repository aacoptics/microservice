package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@DS("msg_db")
public interface NotificationJobInfoMapper extends BaseMapper<NotificationJobInfo> {

    @DS("msg_db")
    String getMaxNo(@Param("jobEnvironment") String jobEnvironment);
}