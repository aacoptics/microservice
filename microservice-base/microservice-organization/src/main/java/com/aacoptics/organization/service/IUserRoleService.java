package com.aacoptics.organization.service;

import java.util.Set;

public interface IUserRoleService {

    /**
     * 给用户添加角色
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean saveBatch(Long userId, Set<Long> roleIds);

    /**
     * 删除用户拥有的角色
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean removeByUserId(Long userId);

    /**
     * 根据userId查询用户拥有角色id集合
     */
    Set<Long> queryByUserId(Long userId);

}
