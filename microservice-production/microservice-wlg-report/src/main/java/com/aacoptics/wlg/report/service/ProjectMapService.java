package com.aacoptics.wlg.report.service;


import com.aacoptics.wlg.report.entity.param.ProjectMapQueryParam;
import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
import com.aacoptics.wlg.report.entity.po.MoldUse;
import com.aacoptics.wlg.report.entity.po.ProjectMap;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ProjectMapService {


    /**
     * 获取所有项目映射配置
     *
     * @return
     */
    List<ProjectMap> getAll();


    /**
     * 根据条件查询项目映射配置
     *
     * @return
     */
    IPage<ProjectMap> query(Page page, ProjectMapQueryParam projectMapQueryParam);


    /**
     * 更新项目映射信息
     *
     * @param projectMap
     */
    boolean update(ProjectMap projectMap);

    /**
     * 根据id删除项目映射
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增项目映射
     *
     * @param projectMap
     * @return
     */
    boolean add(ProjectMap projectMap);



    /**
     * 获取项目映射
     *
     * @param id
     * @return
     */
    ProjectMap get(Long id);
}
