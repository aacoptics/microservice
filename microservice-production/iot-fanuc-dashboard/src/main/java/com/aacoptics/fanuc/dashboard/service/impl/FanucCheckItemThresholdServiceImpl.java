package com.aacoptics.fanuc.dashboard.service.impl;


import cn.hutool.core.util.StrUtil;
import com.aacoptics.fanuc.dashboard.constants.SiteConstants;
import com.aacoptics.fanuc.dashboard.dao.FanucCheckItemThresholdMapper;
import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckItemThresholdParam;
import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import com.aacoptics.fanuc.dashboard.entity.vo.MarkdownMessage;
import com.aacoptics.fanuc.dashboard.exception.BusinessException;
import com.aacoptics.fanuc.dashboard.provider.FeishuApi;
import com.aacoptics.fanuc.dashboard.service.FanucCheckItemThresholdService;
import com.aacoptics.fanuc.dashboard.service.FeishuService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FanucCheckItemThresholdServiceImpl extends ServiceImpl<FanucCheckItemThresholdMapper, FanucCheckItemThreshold> implements FanucCheckItemThresholdService {

    @Resource
    FanucCheckItemThresholdMapper fanucCheckItemThresholdMapper;

    @Resource
    private FeishuService feishuService;

    @Resource
    private FeishuApi feishuApi;


    @Override
    public IPage<FanucCheckItemThreshold> query(Page page, FanucCheckItemThresholdParam fanucCheckItemThresholdParam) {
        QueryWrapper<FanucCheckItemThreshold> queryWrapper = fanucCheckItemThresholdParam.build();
        queryWrapper.eq("site", SiteConstants.SITE_SZ);
        queryWrapper.like(StringUtils.isNotBlank(fanucCheckItemThresholdParam.getMachineName()), "machine_name", fanucCheckItemThresholdParam.getMachineName());
        queryWrapper.like(StringUtils.isNotBlank(fanucCheckItemThresholdParam.getMoldFileName()), "mold_file_name", fanucCheckItemThresholdParam.getMoldFileName());
        queryWrapper.like(StringUtils.isNotBlank(fanucCheckItemThresholdParam.getCheckItemName()), "check_item_name", fanucCheckItemThresholdParam.getCheckItemName());
        queryWrapper.orderByAsc("machine_name");
        queryWrapper.orderByAsc("mold_file_name");
        queryWrapper.orderByAsc("check_item_name");

        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(FanucCheckItemThreshold fanucCheckItemThreshold) {
        //??????????????????
        QueryWrapper<FanucCheckItemThreshold> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site", SiteConstants.SITE_SZ);
        queryWrapper.eq("machine_name", fanucCheckItemThreshold.getMachineName());
        queryWrapper.eq("mold_file_name", fanucCheckItemThreshold.getMoldFileName());
        queryWrapper.eq("check_item", fanucCheckItemThreshold.getCheckItem());

        Integer selectCount = fanucCheckItemThresholdMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("????????????????????????????????????");
        }

        fanucCheckItemThreshold.setSite(SiteConstants.SITE_SZ);
        boolean isSuccess = this.save(fanucCheckItemThreshold);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(FanucCheckItemThreshold fanucCheckItemThreshold) {
        //??????????????????
        QueryWrapper<FanucCheckItemThreshold> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site", SiteConstants.SITE_SZ);
        queryWrapper.eq("machine_name", fanucCheckItemThreshold.getMachineName());
        queryWrapper.eq("mold_file_name", fanucCheckItemThreshold.getMoldFileName());
        queryWrapper.eq("check_item", fanucCheckItemThreshold.getCheckItem());
        queryWrapper.ne("id", fanucCheckItemThreshold.getId());

        Integer selectCount = fanucCheckItemThresholdMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("????????????????????????????????????");
        }

        fanucCheckItemThreshold.setSite(SiteConstants.SITE_SZ);
        boolean isSuccess = this.updateById(fanucCheckItemThreshold);
        return isSuccess;
    }


    @Override
    public FanucCheckItemThreshold get(Long id) {
        FanucCheckItemThreshold exceptionType = this.getById(id);
        if (Objects.isNull(exceptionType)) {
            throw new BusinessException("ID???" + id + "?????????????????????");
        }

        return exceptionType;
    }

    @Override
    public List<FanucCheckItemThreshold> findFanucCheckItemThresholdList() {
        QueryWrapper<FanucCheckItemThreshold> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site", SiteConstants.SITE_SZ);
        queryWrapper.orderByAsc("machine_name");
        queryWrapper.orderByAsc("mold_file_name");
        queryWrapper.orderByAsc("check_item");

        return fanucCheckItemThresholdMapper.selectList(queryWrapper);
    }


    @Override
    public void sendFeishuMessage(JSONObject msgJson) {
        if (StrUtil.isBlank(msgJson.getString("Message")))
            return;
        log.info("Message:", msgJson);

        //??????????????????String
        JSONObject dataJson = msgJson.getJSONObject("Data");
        String machineName = dataJson.getString("machineName"); //??????

        String dateTime = dataJson.getString("dateTime");//??????

        MarkdownMessage markdownGroupMessage = new MarkdownMessage();
        String title = null;
        switch (msgJson.getString("Message")) {
            case "abnormalCushion":
                title = "???????????????????????????";
                markdownGroupMessage.setTitle(title);
                markdownGroupMessage.addContent("?????????" + machineName);
                markdownGroupMessage.addContent("???????????????" + dateTime);
                markdownGroupMessage.addContent("?????????????????????0");
                break;
            case "abnormalTemp":
                title = "???????????????????????????";
                markdownGroupMessage.setTitle(title);

                String abnormalParam = dataJson.getString("abnormalParam"); //?????????
                String currentValue = dataJson.getString("currentValue"); //?????????
                String setValue = dataJson.getString("setValue");//?????????

                markdownGroupMessage.addContent("?????????" + machineName);
                markdownGroupMessage.addContent("????????????" + abnormalParam);
                markdownGroupMessage.addContent("???????????????" + dateTime);
                markdownGroupMessage.addContent("????????????" + setValue);
                markdownGroupMessage.addContent("????????????" + currentValue);
                break;
        }

        if(markdownGroupMessage == null || StrUtil.isBlank(markdownGroupMessage.toString()))
            return;
        String chatName = "???????????????????????????IOT??????????????????";
        try {
            if (StrUtil.isBlank(title)) {
                log.error("title??????");
                return;
            }
            String chatId = feishuService.fetchChatIdByRobot(chatName);
            cn.hutool.json.JSONObject cardJson = feishuApi.getMarkdownMessage(markdownGroupMessage.toString(), null);
            feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                    chatId,
                    FeishuService.MSG_TYPE_INTERACTIVE,
                    cardJson);
        } catch (Exception err) {
            log.error("??????" + title + "?????????", err);
        }
    }
}
