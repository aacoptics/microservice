package com.aacoptics.notification.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.notification.entity.po.FeishuTaskCommentInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;
import com.aacoptics.notification.entity.vo.FeishuTaskVo;
import com.aacoptics.notification.mapper.FeishuTaskCommentInfoMapper;
import com.aacoptics.notification.mapper.FeishuTaskInfoHistoryMapper;
import com.aacoptics.notification.service.FeishuService;
import com.aacoptics.notification.service.FeishuTaskCommentInfoService;
import com.aacoptics.notification.service.FeishuTaskInfoHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@Slf4j
public class FeishuTaskCommentInfoServiceImpl extends ServiceImpl<FeishuTaskCommentInfoMapper, FeishuTaskCommentInfo> implements FeishuTaskCommentInfoService {

    @Resource
    FeishuService feishuService;

    @Override
    public boolean add(FeishuTaskEvent feishuTaskEvent) {
        JSONObject commentsInfoRes = feishuService.getTaskCommentsInfo(feishuTaskEvent.getEvent().getTask_id(), feishuTaskEvent.getEvent().getComment_id());
        if(commentsInfoRes.getInt("code") == 0){
            JSONObject commentJson = commentsInfoRes.getJSONObject("data").getJSONObject("comment");
           FeishuTaskCommentInfo feishuTaskCommentInfo = new FeishuTaskCommentInfo();
            feishuTaskCommentInfo.setTaskId(feishuTaskEvent.getEvent().getTask_id());
            feishuTaskCommentInfo.setCommentId(feishuTaskEvent.getEvent().getComment_id());
            feishuTaskCommentInfo.setParentId(feishuTaskEvent.getEvent().getParent_id());
            feishuTaskCommentInfo.setContentType(feishuTaskEvent.getEvent().getObj_type());
            feishuTaskCommentInfo.setCreatedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(feishuTaskEvent.getHeader().getCreate_time()), ZoneOffset.ofHours(8)));
            feishuTaskCommentInfo.setCreatedBy("FeishuEventHandle");
            feishuTaskCommentInfo.setContent(commentJson.getStr("content"));
            return this.save(feishuTaskCommentInfo);
        }else{
            return false;
        }

    }
}