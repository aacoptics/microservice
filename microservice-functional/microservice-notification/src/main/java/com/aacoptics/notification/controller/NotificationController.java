package com.aacoptics.notification.controller;

import cn.hutool.core.convert.Convert;
import com.aacoptics.common.core.exception.ErrorType;
import com.aacoptics.common.core.exception.SystemErrorType;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.form.*;
import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.param.UmsContentFeishuMsgHistoryQueryParam;
import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.aacoptics.notification.entity.po.NotificationJobSubscription;
import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.vo.FeishuMessage;
import com.aacoptics.notification.exception.BusinessException;
import com.aacoptics.notification.exception.CommonErrorType;
import com.aacoptics.notification.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
@Api("Notification")
@Slf4j
public class NotificationController {
    @Resource
    XxlJobInfoService xxlJobInfoService;
    @Resource
    XxlGroupInfoService xxlGroupInfoService;
    @Resource
    SendMessageService sendMessageService;

    @Resource
    NotificationJobInfoService notificationJobInfoService;

    @Resource
    FeishuService feishuService;

    @Resource
    UmsContentFeishuMsgHistoryService umsContentFeishuMsgHistoryService;

    @Resource
    NotificationJobSubscriptionService notificationJobSubscriptionService;

    @Resource
    MailService mailService;

    @ApiOperation(value = "发送邮件", notes = "发送邮件")
    @ApiImplicitParam(name = "emailSendForm", value = "邮件发送表单", required = true, dataType = "EmailSendForm")
    @PostMapping(value = "/sendEmail")
    public Result sendEmail(@Valid @RequestBody EmailSendForm emailSendForm) {
        if (mailService.send(emailSendForm))
            return Result.success();
        else
            return Result.fail();
    }

    @ApiOperation(value = "保存消息推送计划", notes = "保存消息推送计划")
    @ApiImplicitParam(name = "xxlJobInfo", value = "新增消息推送计划表单", required = true, dataType = "XxlJobInfo")
    @PostMapping
    public Result addNotificationTask(@RequestBody XxlJobInfo xxlJobInfo) {
        Result res = xxlJobInfoService.add(xxlJobInfo);
        if(res.isSuccess()){
            NotificationJobInfo notificationJobInfo = new NotificationJobInfo();
            BeanUtils.copyProperties(xxlJobInfo, notificationJobInfo);
            notificationJobInfo.setXxlJobId(xxlJobInfo.getId());
            notificationJobInfo.setJobStatus(xxlJobInfo.getTriggerStatus() == 1);
            return notificationJobInfoService.add(notificationJobInfo) ? Result.success() : Result.fail("新增消息失败！");
        }
        else
            return res;
    }

    @ApiOperation(value = "删除消息推送计划", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "消息推送计划ID", required = true, dataType = "Integer")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        Result res = xxlJobInfoService.delete(id);
        if (res.isSuccess())
            return notificationJobInfoService.deleteByXxlJobId(id) ? Result.success() : Result.fail("删除消息失败！");
        else
            return res;
    }

    @ApiOperation(value = "修改消息推送计划", notes = "修改指定消息推送计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息推送计划ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xxlJobInfo", value = "消息推送计划实体", required = true, dataType = "XxlJobInfo")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Integer id, @Valid @RequestBody XxlJobInfo xxlJobInfo) {
        xxlJobInfo.setId(id);
        Result res = xxlJobInfoService.update(xxlJobInfo);

        if(res.isSuccess()){
            NotificationJobInfo notificationJobInfo = new NotificationJobInfo();
            BeanUtils.copyProperties(xxlJobInfo, notificationJobInfo);
            notificationJobInfo.setJobStatus(xxlJobInfo.getTriggerStatus() == 1);
            return notificationJobInfoService.updateByXxlJobId(id, notificationJobInfo) ? Result.success() : Result.fail("保存消息失败！");
        }
        else
            return res;
    }

    @ApiOperation(value = "搜索消息推送计划", notes = "根据条件搜索消息推送计划信息")
    @ApiImplicitParam(name = "xxlJobInfoQueryForm", value = "消息推送计划查询参数", required = true, dataType = "XxlJobInfoQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result query(@Valid @RequestBody XxlJobInfoQueryForm xxlJobInfoQueryForm) {
        return Result.success(xxlJobInfoService.listNotificationTask(xxlJobInfoQueryForm.getPage(),
                xxlJobInfoQueryForm.toParam(),
                xxlJobInfoQueryForm.getSearchOption(),
                xxlJobInfoQueryForm.getUsername(),
                xxlJobInfoQueryForm.getProductLine(),
                xxlJobInfoQueryForm.getJobEnvironment(),
                xxlJobInfoQueryForm.getFieldName()));
    }

    @ApiOperation(value = "获取Group", notes = "获取Group")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/listGroup")
    public Result listGroup() {
        return Result.success(xxlGroupInfoService.list());
    }

    @ApiOperation(value = "推送飞书工作通知", notes = "推送飞书工作通知")
    @ApiImplicitParam(name = "feishuMessage", value = "飞书工作通知参数", required = true, dataType = "FeishuMessage")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/sendFeishuNotification")
    public Result sendFeishuNotification(@RequestBody FeishuMessage feishuMessage) {
        return sendMessageService.sendFeishuNotification(feishuMessage);
    }

    @ApiOperation(value = "推送飞书审批", notes = "推送飞书审批")
    @ApiImplicitParam(name = "notificationJobSubscription", value = "飞书工作通知参数", required = true, dataType = "NotificationJobSubscription")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/sendApproveInfo")
    public Result sendApproveInfo(@RequestBody NotificationJobSubscription notificationJobSubscription) {
        return notificationJobSubscriptionService.add(notificationJobSubscription);
    }

    @ApiOperation(value = "手动触发Task", notes = "手动触发Task")
    @ApiImplicitParam(name = "triggerJobForm", value = "触发参数实体", required = true, dataType = "TriggerJobForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/triggerNotificationJob")
    public Result triggerNotificationJob(@Valid @RequestBody TriggerJobForm triggerJobForm) {
        return xxlJobInfoService.triggerJob(triggerJobForm);
    }

    @ApiOperation(value = "启动Task", notes = "启动Task")
    @ApiImplicitParam(name = "id", value = "消息推送计划ID", required = true, dataType = "Integer")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/startNotificationJob/{id}")
    public Result startNotificationJob(@PathVariable Integer id) {
        return xxlJobInfoService.start(id);
    }

    @ApiOperation(value = "停止Task", notes = "停止Task")
    @ApiImplicitParam(name = "id", value = "消息推送计划ID", required = true, dataType = "Integer")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/stopNotificationJob/{id}")
    public Result stopNotificationJob(@PathVariable Integer id) {
        return xxlJobInfoService.stop(id);
    }

    @ApiOperation(value = "查询消息历史记录", notes = "查询消息历史记录")
    @ApiImplicitParam(name = "id", value = "消息推送计划ID", required = true, dataType = "Integer")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryMessageHistory")
    public Result queryMessageHistory(@Valid @RequestBody UmsContentFeishuMsgHistoryQueryForm umsContentFeishuMsgHistoryQueryForm) {
        return Result.success(umsContentFeishuMsgHistoryService.query(umsContentFeishuMsgHistoryQueryForm.getPage(), umsContentFeishuMsgHistoryQueryForm.toParam(UmsContentFeishuMsgHistoryQueryParam.class)));
    }

    @ApiOperation(value = "撤回对应的消息", notes = "撤回对应的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "batchId", value = "消息批次ID", required = true, dataType = "String"),
    })
    @DeleteMapping(value = "/deleteMessage/{id}")
    public Result deleteMessage(@PathVariable Long id, @RequestBody UmsContentFeishuMsgHistory umsContentFeishuMsgHistory) {
        umsContentFeishuMsgHistory.setId(id);
        try {
            boolean res = umsContentFeishuMsgHistoryService.deleteMessageByBatchId(umsContentFeishuMsgHistory);
            if (res)
                return Result.success();
            else
                return Result.fail("撤回失败！");
        } catch (BusinessException err) {
            return Result.fail(err.getMessage());
        }
    }
}