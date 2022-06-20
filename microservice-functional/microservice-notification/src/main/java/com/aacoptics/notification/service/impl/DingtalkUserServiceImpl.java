package com.aacoptics.notification.service.impl;


import com.aacoptics.notification.entity.po.DingtalkUser;
import com.aacoptics.notification.mapper.DingtalkUserMapper;
import com.aacoptics.notification.service.DingtalkUserService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Yan Shangqi
 * @since 2021-06-03
 */
@Service
@DS("user_db")
public class DingtalkUserServiceImpl extends ServiceImpl<DingtalkUserMapper, DingtalkUser> implements DingtalkUserService {

    @Override
    public List<DingtalkUser> GetUsersInfoFromDingtalk(String jobNumber) {
        QueryWrapper<DingtalkUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("JOBNUMBER", jobNumber);
        return this.list(queryWrapper);
    }
}
