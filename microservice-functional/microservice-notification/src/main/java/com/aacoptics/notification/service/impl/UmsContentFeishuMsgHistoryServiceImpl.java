package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.param.UmsContentFeishuMsgHistoryQueryParam;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import com.aacoptics.notification.mapper.UmsContentFeishuMsgHistoryMapper;
import com.aacoptics.notification.service.FeishuService;
import com.aacoptics.notification.service.UmsContentFeishuMsgHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class UmsContentFeishuMsgHistoryServiceImpl extends ServiceImpl<UmsContentFeishuMsgHistoryMapper, UmsContentFeishuMsgHistory> implements UmsContentFeishuMsgHistoryService {

    @Resource
    FeishuService feishuService;

    @Override
    public boolean add(UmsContentFeishuMsgHistory umsContentFeishuMsgHistory) {
        return this.save(umsContentFeishuMsgHistory);
    }

    @Override
    public IPage<UmsContentFeishuMsgHistory> query(Page page, UmsContentFeishuMsgHistoryQueryParam umsContentFeishuMsgHistoryQueryParam) {
        QueryWrapper<UmsContentFeishuMsgHistory> queryWrapper = umsContentFeishuMsgHistoryQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(umsContentFeishuMsgHistoryQueryParam.getBatchId()), "batch_id", umsContentFeishuMsgHistoryQueryParam.getBatchId());
        queryWrapper.eq(StringUtils.isNotBlank(umsContentFeishuMsgHistoryQueryParam.getConType()), "con_type", umsContentFeishuMsgHistoryQueryParam.getConType());
        return this.page(page.addOrder(OrderItem.desc("id")), queryWrapper);
    }

    @Override
    public void deleteMessageByBatchId(String batchId) {
        QueryWrapper<UmsContentFeishuMsgHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_id", batchId);
        queryWrapper.eq("msg_status", true);
        List<UmsContentFeishuMsgHistory> umsContentFeishuMsgHistories = this.list(queryWrapper);
        if (umsContentFeishuMsgHistories.size() > 0) {
            for (UmsContentFeishuMsgHistory umsContentFeishuMsgHistory : umsContentFeishuMsgHistories) {
                feishuService.deleteMessage(umsContentFeishuMsgHistory.getMessageId());
                umsContentFeishuMsgHistory.setMsgStatus(false);
                this.updateById(umsContentFeishuMsgHistory);
            }
        }
    }

    @Override
    public boolean deleteMessageByBatchId(UmsContentFeishuMsgHistory umsContentFeishuMsgHistory) {
        boolean res = feishuService.deleteMessage(umsContentFeishuMsgHistory.getMessageId());
        if(res){
            umsContentFeishuMsgHistory.setMsgStatus(false);
            this.updateById(umsContentFeishuMsgHistory);
            return true;
        }
        else return false;
    }
}