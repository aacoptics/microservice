package com.aacoptics.notification.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.notification.entity.po.FeishuTaskCommentInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;
import com.aacoptics.notification.entity.vo.FeishuTaskVo;
import com.aacoptics.notification.exception.BusinessException;
import com.aacoptics.notification.mapper.FeishuTaskInfoMapper;
import com.aacoptics.notification.service.FeishuService;
import com.aacoptics.notification.service.FeishuTaskInfoHistoryService;
import com.aacoptics.notification.service.FeishuTaskInfoService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FeishuTaskInfoServiceImpl extends ServiceImpl<FeishuTaskInfoMapper, FeishuTaskInfo> implements FeishuTaskInfoService {

    @Resource
    FeishuTaskInfoMapper feishuTaskInfoMapper;

    @Resource
    FeishuTaskInfoHistoryService feishuTaskInfoHistoryService;

    @Resource
    FeishuService feishuService;

    @Override
    public boolean add(FeishuTaskInfo feishuTaskInfo) {
        return this.save(feishuTaskInfo);
    }

    @Override
    public boolean updateOrInsert(FeishuTaskEvent feishuTaskEvent) {
        try {
            int count = this.count(new QueryWrapper<FeishuTaskInfo>()
                    .eq("task_id", feishuTaskEvent.getEvent().getTask_id())
            );
            if (count > 0) {
                new LambdaUpdateChainWrapper<>(feishuTaskInfoMapper)
                        .eq(FeishuTaskInfo::getTaskId, feishuTaskEvent.getEvent().getTask_id())
                        .set(FeishuTaskInfo::getTaskStatus, feishuTaskEvent.getEvent().getObj_type())
                        .set(FeishuTaskInfo::getUpdatedTime, LocalDateTime.now())
                        .set(FeishuTaskInfo::getUpdatedBy, "FeishuEventHandle").update();
            } else {
                JSONObject taskInfoRes = feishuService.getTaskInfo(feishuTaskEvent.getEvent().getTask_id());
                if (taskInfoRes.getInt("code") == 0) {
                    JSONObject taskJson = taskInfoRes.getJSONObject("data").getJSONObject("task");
                    FeishuTaskVo feishuTaskVo = JSONUtil.toBean(taskJson, FeishuTaskVo.class);
                    FeishuTaskInfo feishuTaskInfo = ConvertVo(feishuTaskVo);
                    feishuTaskInfo.setTaskStatus(feishuTaskEvent.getEvent().getObj_type());
                    feishuTaskInfoMapper.insert(feishuTaskInfo);
                }
            }

            FeishuTaskInfoHistory feishuTaskInfoHistory = new FeishuTaskInfoHistory();
            feishuTaskInfoHistory.setTaskId(feishuTaskEvent.getEvent().getTask_id());
            feishuTaskInfoHistory.setTaskStatus(feishuTaskEvent.getEvent().getObj_type());
            feishuTaskInfoHistory.setCreatedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(feishuTaskEvent.getHeader().getCreate_time()), ZoneOffset.ofHours(8)));
            feishuTaskInfoHistory.setCreatedBy("FeishuEventHandle");
            return feishuTaskInfoHistoryService.add(feishuTaskInfoHistory);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public FeishuTaskInfo ConvertVo(FeishuTaskVo feishuTaskVo) {
        FeishuTaskInfo feishuTaskInfo = new FeishuTaskInfo();
        feishuTaskInfo.setTaskId(feishuTaskVo.getId());
        feishuTaskInfo.setLinkTitle(feishuTaskVo.getOrigin().getHref().getTitle());
        feishuTaskInfo.setLinkUrl(feishuTaskVo.getOrigin().getHref().getUrl());
        feishuTaskInfo.setTaskSummary(feishuTaskVo.getSummary());
        feishuTaskInfo.setTaskDescription(feishuTaskVo.getDescription());
        feishuTaskInfo.setDue(LocalDateTime.ofEpochSecond(feishuTaskVo.getDue().getTime(), 0, ZoneOffset.ofHours(8)));
        feishuTaskInfo.setCollaborators(feishuTaskVo.getCollaborators().stream().map(FeishuTaskVo.UserId::getId).collect(Collectors.joining(";")));
        feishuTaskInfo.setFollowers(feishuTaskVo.getFollowers().stream().map(FeishuTaskVo.UserId::getId).collect(Collectors.joining(";")));

        return feishuTaskInfo;
    }
}