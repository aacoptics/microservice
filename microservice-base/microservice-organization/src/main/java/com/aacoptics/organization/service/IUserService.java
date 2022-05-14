package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.UserQueryParam;
import com.aacoptics.organization.entity.po.User;
import com.aacoptics.organization.entity.vo.UserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IUserService {

    /**
     * 新增用户
     */
    boolean add(User user);

    /**
     * 根据id删除用户
     */
    boolean delete(Long id);

    /**
     * 更新用户信息
     */
    boolean update(User user);

    /**
     * 获取用户
     */
    UserVo get(Long id);

    /**
     * 根据用户唯一标识获取用户信息
     * 目前用户标识为用户名或mobile
     */
    User getByUniqueId(String uniqueId);

    /**
     * 根据用户名获取用户信息
     */
    User getByUsername(String username);

    /**
     * 获取所有用户
     */
    List<User> listAll();

    /**
     * 查询用户
     */
    IPage<UserVo> query(Page page, UserQueryParam userQueryParam);

}
