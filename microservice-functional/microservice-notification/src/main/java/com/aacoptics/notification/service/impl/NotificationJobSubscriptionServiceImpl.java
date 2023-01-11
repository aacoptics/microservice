package com.aacoptics.notification.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.FeishuUser;
import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.aacoptics.notification.entity.po.NotificationJobSubscription;
import com.aacoptics.notification.mapper.FeishuUserMapper;
import com.aacoptics.notification.mapper.NotificationJobSubscriptionMapper;
import com.aacoptics.notification.service.FeishuService;
import com.aacoptics.notification.service.NotificationJobInfoService;
import com.aacoptics.notification.service.NotificationJobSubscriptionService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@DS("msg_db")
public class NotificationJobSubscriptionServiceImpl extends ServiceImpl<NotificationJobSubscriptionMapper, NotificationJobSubscription> implements NotificationJobSubscriptionService {

    @Resource
    FeishuService feishuService;
    @Resource
    NotificationJobSubscriptionMapper notificationJobSubscriptionMapper;

    @Resource
    FeishuUserMapper feishuUserMapper;

    @Override
    public Result add(NotificationJobSubscription notificationJobSubscription) {
        if(notificationJobSubscription.getSubscriptionStatus() == 1){
            return removeSubscription(notificationJobSubscription) ?
                    Result.success("移除消息订阅成功") :
                    Result.fail();
        }else{
            QueryWrapper<NotificationJobSubscription> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("notification_job_id", notificationJobSubscription.getNotificationJobId());
            queryWrapper.eq("subscription_person", notificationJobSubscription.getSubscriptionPerson());

            NotificationJobSubscription info = this.getOne(queryWrapper);

            JSONObject approveJson = createApproveJson(notificationJobSubscription.getSubscriptionPerson(),
                    notificationJobSubscription.getNotificationDesc(),
                    notificationJobSubscription.getApproveUser());

            JSONObject res = feishuService.createApproveInstance(approveJson);

            if(res.getInt("code") == 0){
                notificationJobSubscription.setApproveId(res.getJSONObject("data").getStr("instance_code"));
                notificationJobSubscription.setApproveStatus(0);
                if(info != null)
                    return this.update(notificationJobSubscription, queryWrapper) ?
                            Result.success("发送审批成功，待审批完成即可成功订阅消息") :
                            Result.fail();
                else
                    return this.save(notificationJobSubscription) ?
                            Result.success("发送审批成功，待审批完成即可成功订阅消息") :
                            Result.fail();
            }else
                return Result.fail();
        }
    }

    @Override
    public boolean removeSubscription(NotificationJobSubscription notificationJobSubscription) {

        QueryWrapper<NotificationJobSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notification_job_id", notificationJobSubscription.getNotificationJobId());
        queryWrapper.eq("subscription_person", notificationJobSubscription.getSubscriptionPerson());

        NotificationJobSubscription info = this.getOne(queryWrapper);

        if(info != null){
            info.setApproveStatus(0);
            return this.updateById(info);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(NotificationJobSubscription notificationJobSubscription) {
        return this.updateById(notificationJobSubscription);
    }

    @Override
    public boolean updateApproveStatus(String approveId, Integer status) {
        UpdateWrapper<NotificationJobSubscription> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("approve_status", status);
        updateWrapper.eq("approve_id", approveId);
        return this.update(updateWrapper);
    }

    @Override
    public List<String> listSubscriptionUsers(String planKey){
        List<String> employeeNos = notificationJobSubscriptionMapper.getSubscriptionUsers(planKey);
        return feishuUserMapper.getFeishuUserIds(employeeNos);
    }

    private JSONObject createApproveJson(String username, String notificationDesc, String approveUsername){
        FeishuUser user = feishuService.getFeishuUser(username);
        FeishuUser approveUser = feishuService.getFeishuUser(approveUsername);

        JSONObject formJson = JSONUtil.createObj();
        formJson.set("id", "widget16723660284730001");
        formJson.set("type", "textarea");
        formJson.set("value", notificationDesc);

        JSONObject json = JSONUtil.createObj();
        json.set("approval_code", "69D4A4EE-D374-4262-8E49-8F94CEB7400E");
        json.set("user_id", user.getUserId());
        JSONArray formList = new JSONArray();
        formList.add(formJson);
        json.set("form", formList.toJSONString(0));

        JSONArray userList = new JSONArray();

        JSONObject userJson = JSONUtil.createObj();
        userJson.set("key", "a5a5b6640bd28ff3794907e610b1842f");
        userJson.set("value", Collections.singletonList(approveUser.getUserId()));

        userList.add(userJson);

        json.set("node_approver_user_id_list", userList);

        return json;

    }
}