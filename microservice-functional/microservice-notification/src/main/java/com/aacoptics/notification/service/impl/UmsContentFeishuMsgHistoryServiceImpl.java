package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import com.aacoptics.notification.mapper.UmsContentFeishuMsgHistoryMapper;
import com.aacoptics.notification.service.UmsContentFeishuMsgHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UmsContentFeishuMsgHistoryServiceImpl extends ServiceImpl<UmsContentFeishuMsgHistoryMapper, UmsContentFeishuMsgHistory> implements UmsContentFeishuMsgHistoryService {

    @Override
    public boolean add(UmsContentFeishuMsgHistory umsContentFeishuMsgHistory) {
        return this.save(umsContentFeishuMsgHistory);
    }
}