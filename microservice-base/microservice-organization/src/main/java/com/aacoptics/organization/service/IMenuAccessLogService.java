package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IMenuAccessLogService {

    /**
     * 新增菜单访问日志
     */
    boolean logMenuAccess(MenuAccessLog menuAccessLog);

    Page<MenuAccessLog> getAccessLogByTime(Page<MenuAccessLog> iPage,
                                           LocalDateTime startTime,
                                           LocalDateTime endTime);

    List<MenuAccessLog> getLastMouthTotalCount();

    List<MenuAccessLog> getLastWeekMenuCount();

}
