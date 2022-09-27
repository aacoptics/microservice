package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FeishuTaskInfoHistoryMapper extends BaseMapper<FeishuTaskInfoHistory> {

}