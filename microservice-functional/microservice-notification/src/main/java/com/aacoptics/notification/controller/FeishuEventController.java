package com.aacoptics.notification.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.config.FeishuAppKeyConfig;
import com.aacoptics.notification.entity.form.EmailSendForm;
import com.aacoptics.notification.entity.form.TriggerJobForm;
import com.aacoptics.notification.entity.form.XxlJobInfoQueryForm;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.vo.FeishuMessage;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;
import com.aacoptics.notification.entity.vo.FeishuTaskVo;
import com.aacoptics.notification.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aacoptics.notification.utils.Decrypt;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/feishuEvent")
@Api("feishuEvent")
@Slf4j
public class FeishuEventController {
    @Resource
    private FeishuAppKeyConfig feishuAppKeyConfig;

    @Resource
    private FeishuTaskCommentInfoService feishuTaskCommentInfoService;

    @Resource
    private NotificationJobSubscriptionService notificationJobSubscriptionService;

    @Resource
    private NotificationJobInfoService notificationJobInfoService;

    @Resource
    private FeishuTaskInfoService feishuTaskInfoService;


    @ApiOperation(value = "接收飞书任务信息", notes = "接收飞书任务信息")
    @ApiImplicitParam(name = "jsonObject", value = "消息JSON", required = true)
    @PostMapping(value = "/receiveTaskInfo")
    public Result receiveTaskInfo(@RequestBody String bodyString,
                                          @RequestHeader("X-Lark-Request-Timestamp") String timeStamp,
                                          @RequestHeader("X-Lark-Request-Nonce") String nonce,
                                          @RequestHeader("X-Lark-Signature") String sign) throws Exception {
        Decrypt d = new Decrypt(feishuAppKeyConfig.getEncryptKey());
        String signature = d.calculateSignature(timeStamp, nonce, feishuAppKeyConfig.getEncryptKey(), bodyString);
        if (!signature.equals(sign))
            throw new RuntimeException("签名不一致！");
        JSONObject bodyJson = JSONObject.parseObject(bodyString, JSONObject.class);
        JSONObject msgJson = JSONObject.parseObject(d.decrypt(bodyJson.getString("encrypt")), JSONObject.class);

        if(msgJson.containsKey("event") && msgJson.getJSONObject("event").getString("type").equals("approval_instance")){
            JSONObject event = msgJson.getJSONObject("event");
            String approveCode = event.getString("approval_code");
            if(approveCode.equals("69D4A4EE-D374-4262-8E49-8F94CEB7400E")){
                String approveId = event.getString("instance_code");
                String status = event.getString("status");
                if(status.equals("REJECTED"))
                    notificationJobSubscriptionService.updateApproveStatus(approveId, 2);
                else if(status.equals("APPROVED"))
                    notificationJobSubscriptionService.updateApproveStatus(approveId, 1);
                else if(status.equals("PENDING"))
                    notificationJobSubscriptionService.updateApproveStatus(approveId, 3);
                log.info(JSONObject.toJSONString(msgJson));
                return Result.success();
            }else{
                String approveId = event.getString("instance_code");
                String status = event.getString("status");
                if(status.equals("REJECTED"))
                    notificationJobInfoService.updateApproveStatus(approveId, 2);
                else if(status.equals("APPROVED"))
                    notificationJobInfoService.updateApproveStatus(approveId, 1);
                else if(status.equals("PENDING"))
                    notificationJobInfoService.updateApproveStatus(approveId, 0);
                log.info(JSONObject.toJSONString(msgJson));
                return Result.success();
            }

        }

        FeishuTaskEvent feishuTaskEvent = JSONObject.parseObject(msgJson.toJSONString(), FeishuTaskEvent.class);

        boolean res;
        if (StrUtil.isBlank(feishuTaskEvent.getEvent().getComment_id())) {
            res = feishuTaskInfoService.updateOrInsert(feishuTaskEvent);
        } else {
            res = feishuTaskCommentInfoService.add(feishuTaskEvent);
        }

        if (!res)
            throw new RuntimeException("保存失败！");
        log.info(JSONObject.toJSONString(feishuTaskEvent));
        return Result.success();
    }
}