package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.po.DingtalkUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Yan Shangqi
 * @since 2021-06-03
 */
public interface DingtalkUserService extends IService<DingtalkUser> {

    List<DingtalkUser> GetUsersInfoFromDingtalk(String jobNumber);
}
