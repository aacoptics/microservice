package com.aacoptics.organization.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aacoptics.organization.entity.po.Role;
import com.aacoptics.organization.entity.po.User;
import com.aacoptics.organization.mapper.RoleMapper;
import com.aacoptics.organization.mapper.UserMapper;
import com.aacoptics.organization.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Kaizhi Xuan
 * created on 2022/7/4 9:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Resource
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Test
    public void Test() {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setSheetNum(1);
        params.setTitleRows(0);
        params.setHeadRows(1);
        params.setStartSheetIndex(0);
        final String property = System.getProperty("user.home");
        List<Map<String, Object>> list = ExcelImportUtil.importExcel(new File(property + "/Downloads/7月1日 群成员清单.xlsx"), Map.class, params);
        final Role qualityMil1 = roleMapper.selectOne(new LambdaQueryWrapper<Role>().in(Role::getCode, "quality_msg_MIL"));
        final Role qualityMil2 = roleMapper.selectOne(new LambdaQueryWrapper<Role>().in(Role::getCode, "quality_mil"));
        if (ObjectUtil.isNull(qualityMil1) || ObjectUtil.isNull(qualityMil2)) {
            log.error("quality_msg_MIL or quality_mil is null");
        }
        for (Map<String, Object> stringObjectMap : list) {
            if (ObjectUtil.isNull(stringObjectMap.get("域账号")) || StrUtil.isEmpty(stringObjectMap.get("域账号").toString().trim())) {
                continue;
            }
            if (ObjectUtil.isNull(stringObjectMap.get("姓名")) || StrUtil.isEmpty(stringObjectMap.get("姓名").toString().trim())) {
                continue;
            }
            User byUniqueId = userMapper.selectOne(new QueryWrapper<User>().eq("username", stringObjectMap.get("域账号").toString().trim()));
            if (ObjectUtil.isNotNull(byUniqueId)) {
                byUniqueId.setDescription("质量日报用户");
                userService.update(byUniqueId);
                log.info("update user: {}", byUniqueId.getUsername());
                continue;
            }
            User user = new User();
            user.setName(stringObjectMap.get("姓名").toString().trim());
            user.setDescription(stringObjectMap.get("姓名").toString().trim());
            user.setUsername(stringObjectMap.get("域账号").toString().trim());
            user.setPassword(UUID.randomUUID().toString());
            user.setMobile("12345678910");
            user.setRoleIds(ImmutableSet.of(qualityMil1.getId(), qualityMil2.getId()));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            final boolean add = userService.add(user);
            if (add) {
                log.info("添加用户成功：{}", user.getUsername());
            } else {
                log.error("添加用户失败：{}", user.getUsername());
            }
        }
    }

}