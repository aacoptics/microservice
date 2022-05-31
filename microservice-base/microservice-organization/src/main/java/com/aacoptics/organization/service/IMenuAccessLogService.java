package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IMenuAccessLogService {

    /**
     * 新增菜单访问日志
     */
    boolean logMenuAccess(MenuAccessLog menuAccessLog);

    Page<MenuAccessLog> getLastWeekAccessLog(Page<MenuAccessLog> iPage);

    List<MenuAccessLog> getLastMouthTotalCount();

    List<MenuAccessLog> getLastWeekMenuCount();

}
