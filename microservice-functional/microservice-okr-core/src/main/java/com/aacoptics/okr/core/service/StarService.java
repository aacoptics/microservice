package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.Star;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Kaizhi Xuan
 * created on 2022/12/7 15:21
 */
public interface StarService extends IService<Star> {

    boolean isStar(String userName, String starUserName);

    boolean deleteOrCreate(String userName, String starUserName);

    List<String> employeeNoToStarUser(String employeeNo);

}
