package com.aacoptics.organization.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aacoptics.organization.entity.param.UserQueryParam;
import com.aacoptics.organization.entity.po.User;
import com.aacoptics.organization.entity.vo.UserVo;
import com.aacoptics.organization.exception.UserNotFoundException;
import com.aacoptics.organization.mapper.UserMapper;
import com.aacoptics.organization.service.IUserRoleService;
import com.aacoptics.organization.service.IUserService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public boolean add(User user) {
        if (StringUtils.isNotBlank(user.getPassword())) user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean inserts = this.save(user);
        userRoleService.saveBatch(user.getId(), user.getRoleIds());
        return inserts;
    }

    @Override
    @Transactional
    @CacheInvalidate(name = "user::", key = "#id")
    @CacheInvalidate(name = "user::", key = "targetObject.getUserKeys()", multi = true)
    @CacheInvalidate(name = "userInfo::", key = "targetObject.getUserInfoKeys()", multi = true)
    @CacheInvalidate(name = "role4user::", key = "#id")
    @CacheInvalidate(name = "menu4user::", key = "targetObject.getMenu4UserKeys()", multi = true)
    @CacheInvalidate(name = "resource4user::", key = "targetObject.getResource4UserKeys()", multi = true)
    public boolean delete(Long id) {
        this.removeById(id);
        return userRoleService.removeByUserId(id);
    }

    @Override
    @Transactional
    @CacheInvalidate(name = "user::", key = "#user.id")
    @CacheInvalidate(name = "user::", key = "#user.username")
    @CacheInvalidate(name = "userInfo::", key = "#user.username")
    @CacheInvalidate(name = "role4user::", key = "#user.id")
    @CacheInvalidate(name = "menu4user::", key = "#user.username")
    @CacheInvalidate(name = "resource4user::", key = "#user.username")
    public boolean update(User user) {
        if (StringUtils.isNotBlank(user.getPassword())) user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean isSuccess = this.updateById(user);
        userRoleService.saveBatch(user.getId(), user.getRoleIds());
        return isSuccess;
    }

    @Override
    @Cached(name = "user::", key = "#id", cacheType = CacheType.REMOTE)
    public UserVo get(Long id) {
        User user = this.getById(id);
        if (Objects.isNull(user)) throw new UserNotFoundException("user not found with id:" + id);
        user.setRoleIds(userRoleService.queryByUserId(id));
        return new UserVo(user);
    }

    @Override
    @Cached(name = "user::", key = "#uniqueId", cacheType = CacheType.REMOTE)
    public User getByUniqueId(String uniqueId) {
        User user = this.getOne(new QueryWrapper<User>().eq("username", uniqueId).or().eq("mobile", uniqueId));
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("user not found with uniqueId:" + uniqueId);
        }
        user.setRoleIds(userRoleService.queryByUserId(user.getId()));
        return user;
    }

    @Override
    @Cached(name = "userInfo::", key = "#username", cacheType = CacheType.REMOTE)
    public User getByUsername(String username) {
        User user = this.getOne(new QueryWrapper<User>().select("id", "username", "name", "mobile", "description").eq("username", username));
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("没有找到此用户:" + username);
        }
        user.setRoleIds(userRoleService.queryByUserId(user.getId()));
        return user;
    }

    @Override
    public List<User> listAll() {
        return list().stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public IPage<UserVo> query(Page page, UserQueryParam userQueryParam) {
        QueryWrapper<User> queryWrapper = userQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(userQueryParam.getName()), "name", userQueryParam.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userQueryParam.getUsername()), "username", userQueryParam.getUsername());
        queryWrapper.eq(StringUtils.isNotBlank(userQueryParam.getMobile()), "mobile", userQueryParam.getMobile());
        queryWrapper.like(StringUtils.isNotBlank(userQueryParam.getDescription()), "description", userQueryParam.getDescription());
        // 转换成VO
        IPage<User> iPageUser = this.page(page, queryWrapper);
        return iPageUser.convert(UserVo::new);
    }

    public Set<String> getUserKeys() {
        Set<String> userKeys = stringRedisTemplate.keys("user::*");
        if (CollUtil.isEmpty(userKeys)) return Collections.singleton("user::*");
        return userKeys.stream().map(key -> key.replace("user::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

    public Set<String> getUserInfoKeys() {
        Set<String> userInfoKeys = stringRedisTemplate.keys("userInfo::*");
        if (CollUtil.isEmpty(userInfoKeys)) return Collections.singleton("userInfo::*");
        return userInfoKeys.stream().map(key -> key.replace("userInfo::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

    public Set<String> getMenu4UserKeys() {
        Set<String> menu4UserKeys = stringRedisTemplate.keys("menu4user::*");
        if (CollUtil.isEmpty(menu4UserKeys)) return Collections.singleton("menu4user::*");
        return menu4UserKeys.stream().map(key -> key.replace("menu4user::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

    public Set<String> getResource4UserKeys() {
        Set<String> resource4UserKeys = stringRedisTemplate.keys("resource4user::*");
        if (CollUtil.isEmpty(resource4UserKeys)) return Collections.singleton("resource4user::*");
        return resource4UserKeys.stream().map(key -> key.replace("resource4user::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

}
