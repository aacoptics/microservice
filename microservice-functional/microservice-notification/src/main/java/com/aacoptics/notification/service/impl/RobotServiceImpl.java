package com.aacoptics.notification.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.mapper.RobotMapper;
import com.aacoptics.notification.service.RobotService;
import com.baomidou.dynamic.datasource.annotation.DS;
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
@DS("msg_db")
public class RobotServiceImpl extends ServiceImpl<RobotMapper, Robot> implements RobotService {

    @Override
    public boolean add(Robot robot) {
        return this.save(robot);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(Robot robot) {
        return this.updateById(robot);
    }

    @Override
    public IPage<Robot> query(Page page, RobotQueryParam robotQueryParam) {
        QueryWrapper<Robot> queryWrapper = robotQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(robotQueryParam.getRobotType()), "robot_type", robotQueryParam.getRobotType());
        queryWrapper.like(StringUtils.isNotBlank(robotQueryParam.getRobotName()), "robot_name", robotQueryParam.getRobotName());
        queryWrapper.eq("status", true);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Robot> listAll() {
        QueryWrapper<Robot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        return this.list(queryWrapper);
    }

    @Override
    public List<Robot> findByName(List<String> robotNames) {
        QueryWrapper<Robot> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("robot_name", robotNames);
        queryWrapper.eq("status", true);
        return this.list(queryWrapper);
    }
}