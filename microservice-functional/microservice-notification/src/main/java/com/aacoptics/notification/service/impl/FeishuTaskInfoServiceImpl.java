package com.aacoptics.notification.service.impl;

import cn.hutool.core.util.StrUtil;
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
import java.util.List;
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
            JSONObject taskInfoRes = feishuService.getTaskInfo(feishuTaskEvent.getEvent().getTask_id());
            if (taskInfoRes.getInt("code") == 0) {
                JSONObject taskJson = taskInfoRes.getJSONObject("data").getJSONObject("task");
                FeishuTaskVo feishuTaskVo = JSONUtil.toBean(taskJson, FeishuTaskVo.class);
                List<FeishuTaskInfo> list = this.list(new QueryWrapper<FeishuTaskInfo>().lambda()
                        .eq(FeishuTaskInfo::getTaskId, feishuTaskEvent.getEvent().getTask_id()));
                if (list.size() > 0) {
                    new LambdaUpdateChainWrapper<>(feishuTaskInfoMapper)
                            .eq(FeishuTaskInfo::getTaskId, feishuTaskVo.getId())
                            .set(FeishuTaskInfo::getTaskStatus, feishuTaskEvent.getEvent().getObj_type())
                            .set(FeishuTaskInfo::getUpdatedTime, LocalDateTime.now())
                            .set(FeishuTaskInfo::getUpdatedBy, "FeishuEventHandle").update();
                } else {
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
        if (feishuTaskVo.getOrigin() != null && feishuTaskVo.getOrigin().getHref() != null && !StrUtil.isBlank(feishuTaskVo.getOrigin().getHref().getTitle()))
            feishuTaskInfo.setLinkTitle(feishuTaskVo.getOrigin().getHref().getTitle());
        if (feishuTaskVo.getOrigin() != null && feishuTaskVo.getOrigin().getHref() != null && !StrUtil.isBlank(feishuTaskVo.getOrigin().getHref().getUrl()))
            feishuTaskInfo.setLinkUrl(feishuTaskVo.getOrigin().getHref().getUrl());
        feishuTaskInfo.setTaskSummary(feishuTaskVo.getSummary());
        feishuTaskInfo.setTaskDescription(feishuTaskVo.getDescription());
        if (feishuTaskVo.getDue() != null && feishuTaskVo.getDue().getTime() > 0)
            feishuTaskInfo.setDue(LocalDateTime.ofEpochSecond(feishuTaskVo.getDue().getTime(), 0, ZoneOffset.ofHours(8)));
        if (feishuTaskVo.getCollaborators() != null && feishuTaskVo.getCollaborators().size() > 0)
            feishuTaskInfo.setCollaborators(feishuTaskVo.getCollaborators().stream().map(FeishuTaskVo.UserId::getId).collect(Collectors.joining(";")));
        if (feishuTaskVo.getFollowers() != null && feishuTaskVo.getFollowers().size() > 0)
            feishuTaskInfo.setFollowers(feishuTaskVo.getFollowers().stream().map(FeishuTaskVo.UserId::getId).collect(Collectors.joining(";")));
        return feishuTaskInfo;
    }
}