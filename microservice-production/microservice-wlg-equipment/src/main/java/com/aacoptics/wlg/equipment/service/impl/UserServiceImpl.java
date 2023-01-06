package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.EquipmentStatusConstants;
import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.entity.po.User;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.EquipmentMapper;
import com.aacoptics.wlg.equipment.mapper.UserMapper;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.UserService;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public List<String> findUserByName(String userNameInput) {

        List<String> resultList = userMapper.findUserByName(userNameInput);

        return resultList;
    }

}
