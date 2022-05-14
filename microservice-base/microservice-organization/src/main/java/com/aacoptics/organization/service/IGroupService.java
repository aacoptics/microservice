package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.GroupQueryParam;
import com.aacoptics.organization.entity.po.Group;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IGroupService extends IService<Group> {

    /**
     * 获取用户组
     */
    Group get(Long id);

    /**
     * 新增用户组
     */
    boolean add(Group group);

    /**
     * 查询用户组
     */
    List<Group> query(GroupQueryParam groupQueryParam);

    /**
     * 根据父id查询用户组
     */
    List<Group> queryByParentId(Long id);

    /**
     * 更新用户组信息
     */
    boolean update(Group group);

    /**
     * 根据id删除用户组
     */
    boolean delete(Long id);

}
