package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.po.UmsContentSub;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UmsContentSubService extends IService<UmsContentSub> {
    List<UmsContentSub> getUmsContentSub(String batchId);
}
