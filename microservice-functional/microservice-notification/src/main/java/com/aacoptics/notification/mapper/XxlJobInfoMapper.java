package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper

public interface XxlJobInfoMapper extends BaseMapper<XxlJobInfo> {
    @DS("msg_db")
    int maxId();

    @DS("msg_db")
    Page<XxlJobInfo> listNotificationTask(Page<XxlJobInfo> iPage, @Param("planKey") String planKey, @Param("username") String username);

    @DS("msg_db")
    Page<XxlJobInfo> listSubNotificationTask(Page<XxlJobInfo> iPage, @Param("planKey") String planKey, @Param("username") String username);

    @DS("msg_db")
    Page<XxlJobInfo> listSubNotificationTaskByUser(Page<XxlJobInfo> iPage, @Param("planKey") String planKey, @Param("username") String username);
}