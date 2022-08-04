package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.ProjectMapQueryParam;
import com.aacoptics.wlg.report.entity.po.ProjectMap;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.ProjectMapMapper;
import com.aacoptics.wlg.report.service.ProjectMapService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ProjectMapServiceImpl extends ServiceImpl<ProjectMapMapper, ProjectMap> implements ProjectMapService {

    @Autowired
    private ProjectMapMapper projectMapMapper;


    @Override
    public List<ProjectMap> getAll() {
        return this.list();
    }

    @Override
    public IPage<ProjectMap> query(Page page, ProjectMapQueryParam projectMapQueryParam) {
        QueryWrapper<ProjectMap> queryWrapper = projectMapQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(projectMapQueryParam.getBusinessProject()), "business_project", projectMapQueryParam.getBusinessProject());
        queryWrapper.eq(StringUtils.isNotBlank(projectMapQueryParam.getInternalProject()), "internal_project", projectMapQueryParam.getInternalProject());
        queryWrapper.orderByAsc("business_project");
        queryWrapper.orderByAsc("internal_project");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(ProjectMap projectMap) {

        this.validationProjectMap(projectMap);

        boolean isSuccess = this.save(projectMap);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProjectMap projectMap) {
        this.validationProjectMap(projectMap);

        boolean isSuccess = this.updateById(projectMap);
        return isSuccess;
    }


    @Override
    public ProjectMap get(Long id) {
        ProjectMap projectMap = this.getById(id);
        if (Objects.isNull(projectMap)) {
            throw new BusinessException("data not found with id:" + id);
        }
        return projectMap;
    }




    /**
     * 校验数据
     *
     * @param projectMap
     */
    private void validationProjectMap(ProjectMap projectMap)
    {
        String businessProject = projectMap.getBusinessProject();
        String internalProject = projectMap.getInternalProject();

        QueryWrapper<ProjectMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_project", businessProject);
        queryWrapper.eq("internal_project", internalProject);

        List<ProjectMap> projectMapList = projectMapMapper.selectList(queryWrapper);
        if (projectMapList != null && projectMapList.size() > 0) {
            if(projectMap.getId() != null && projectMap.getId() != 0) {
                if(!projectMapList.get(0).getId().equals(projectMap.getId()))
                {
                    throw new BusinessException("已存在相同记录，请确认！");
                }
            }
            else
            {
                throw new BusinessException("已存在相同记录，请确认！");
            }
        }
    }
}