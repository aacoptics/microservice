package com.aacoptics.notification.service;


import com.aacoptics.notification.entity.po.UmsContent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UmsContentService extends IService<UmsContent> {
    List<UmsContent> getUmsContent(String conType);

    List<UmsContent> getUmsContentByBatchId(String batchId);
}
