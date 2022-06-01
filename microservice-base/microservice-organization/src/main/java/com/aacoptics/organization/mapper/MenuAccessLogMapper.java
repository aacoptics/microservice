package com.aacoptics.organization.mapper;

import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface MenuAccessLogMapper extends BaseMapper<MenuAccessLog> {

    Page<MenuAccessLog> getAccessLogByTime(Page<MenuAccessLog> iPage,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    List<MenuAccessLog> getLastMouthTotalCount();

    List<MenuAccessLog> getLastWeekMenuCount();
}