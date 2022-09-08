package com.aacoptics.notification.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.notification.entity.po.UmsContentSubDaiban;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UmsContentSubDaibanService extends IService<UmsContentSubDaiban> {
    List<UmsContentSubDaiban> getUmsContentSubDaiban(String batchId);

    JSONObject getTaskJson(UmsContentSubDaiban umsContentSubDaiban);
}
