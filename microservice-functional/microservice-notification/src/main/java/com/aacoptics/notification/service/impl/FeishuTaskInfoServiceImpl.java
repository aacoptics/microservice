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
            // 更新情况：先更新一把，如果能更新成功，就说明是更新情况，否则就是新增情况
            boolean flag = new LambdaUpdateChainWrapper<>(feishuTaskInfoMapper)
                    .eq(FeishuTaskInfo::getTaskId, feishuTaskEvent.getEvent().getTask_id())
                    .set(FeishuTaskInfo::getTaskStatus, feishuTaskEvent.getEvent().getObj_type())
                    .set(FeishuTaskInfo::getCreatedTime, LocalDateTime.now())
                    .set(FeishuTaskInfo::getCreatedBy, "FeishuEventHandle").update();
            log.info("flag is " + flag);
            // 新增情况：如果不能更新成功
            if (!flag) {
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