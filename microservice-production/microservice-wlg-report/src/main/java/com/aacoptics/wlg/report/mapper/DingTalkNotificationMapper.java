package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.DingTalkMessageHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DingTalkNotificationMapper extends BaseMapper<DingTalkMessageHistory> {

    List<Map<String, String>> findRobotListByType(@Param("groupType") String groupType);
}
