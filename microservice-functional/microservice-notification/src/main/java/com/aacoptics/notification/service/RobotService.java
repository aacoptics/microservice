package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.po.Robot;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface RobotService {

    boolean add(Robot robot);

    boolean delete(Long id);
    boolean update(Robot robot);
    IPage<Robot> query(Page page, RobotQueryParam robotQueryParam);

    List<Robot> listAll();

    List<Robot> findByName(List<String> robotNames);

}
