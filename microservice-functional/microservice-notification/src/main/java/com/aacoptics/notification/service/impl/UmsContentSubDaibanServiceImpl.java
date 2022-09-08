package com.aacoptics.notification.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.notification.entity.po.UmsContentSubDaiban;
import com.aacoptics.notification.mapper.FeishuUserMapper;
import com.aacoptics.notification.mapper.UmsContentSubDaibanMapper;
import com.aacoptics.notification.service.UmsContentSubDaibanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UmsContentSubDaibanServiceImpl extends ServiceImpl<UmsContentSubDaibanMapper, UmsContentSubDaiban> implements UmsContentSubDaibanService {

    @Resource
    FeishuUserMapper feishuUserMapper;

    @Override
    public List<UmsContentSubDaiban> getUmsContentSubDaiban(String batchId) {
        QueryWrapper<UmsContentSubDaiban> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_id", batchId)
                .eq("is_status", "0");
        return this.list(queryWrapper);
    }


    @Override
    public JSONObject getTaskJson(UmsContentSubDaiban umsContentSubDaiban){
        List<String> collaboratorNos = Arrays.stream(umsContentSubDaiban.getExecutor().split(";")).collect(Collectors.toList());
        List<String> followerNos = Arrays.stream(umsContentSubDaiban.getFollowPeople().split(";")).collect(Collectors.toList());

        List<String> collaboratorIds = feishuUserMapper.getFeishuUserIds(collaboratorNos);
        List<String> followerIds = feishuUserMapper.getFeishuUserIds(followerNos);

        JSONObject params = new JSONObject();
        params.set("rich_summary", umsContentSubDaiban.getTaskTitle());
        params.set("description", umsContentSubDaiban.getTaskNotes());

        if(collaboratorIds.size() == 0)
            return null;

        params.set("collaborator_ids", collaboratorIds);

        if(followerIds.size() > 0){
            params.set("follower_ids", followerIds);
        }

        JSONObject due = JSONUtil.createObj().set("time", umsContentSubDaiban.getDeadline().toEpochSecond(ZoneOffset.ofHours(8))).set("timezone", "Asia/Shanghai");


        JSONObject origin = new JSONObject();
        origin.set("platform_i18n_name", "{\"zh_cn\": \""+ "详情" +"\"}");
        JSONObject originHref = new JSONObject();
        originHref.set("url", umsContentSubDaiban.getLinkUrl());
        originHref.set("title", umsContentSubDaiban.getLinkUrlName());
        origin.set("href", originHref);
        params.set("origin", origin);
        params.set("due", due);

        return params;
    }

}
