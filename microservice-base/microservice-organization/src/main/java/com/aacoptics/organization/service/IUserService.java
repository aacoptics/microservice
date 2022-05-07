package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.UserQueryParam;
import com.aacoptics.organization.entity.po.User;
import com.aacoptics.organization.entity.vo.UserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IUserService {
    /**
     * 获取用户
     *
     * @param id 用户id
     * @return UserVo
     */
    UserVo get(Long id);

    /**
     * 根据用户唯一标识获取用户信息
     * 目前用户标识为用户名或mobile
     *
     * @param uniqueId
     * @return
     */
    User getByUniqueId(String uniqueId);

    User getByUsername(String username);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    boolean add(User user);

    /**
     * 查询用户
     *
     * @return
     */
    IPage<UserVo> query(Page<User> page, UserQueryParam userQueryParam);

    List<User> queryAll();

    /**
     * 更新用户信息
     *
     * @param user
     */
    boolean update(User user);

    /**
     * 根据id删除用户
     *
     * @param id
     */
    boolean delete(Long id);
}
