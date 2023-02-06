package com.aacoptics.notification.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.FeishuUser;
import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.aacoptics.notification.entity.po.NotificationJobSubscription;
import com.aacoptics.notification.mapper.NotificationJobInfoMapper;
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
import java.util.Collections;

@Service
@Slf4j
@DS("msg_db")
public class NotificationJobInfoServiceImpl extends ServiceImpl<NotificationJobInfoMapper, NotificationJobInfo> implements NotificationJobInfoService {

    @Resource
    NotificationJobInfoMapper notificationJobInfoMapper;

    @Resource
    FeishuService feishuService;

    @Override
    public boolean add(NotificationJobInfo notificationJobInfo) {
        getNextNotificationNo(notificationJobInfo);
        String username = UserContextHolder.getInstance().getUsername();
        JSONObject approveJson = createApproveJson(username, notificationJobInfo.getJobDesc() + "-" + notificationJobInfo.getRemark(),
        "60054916", "1D357EE9-AC60-403E-A302-A5AEBCF9A735");

        JSONObject res = feishuService.createApproveInstance(approveJson);

        if (res.getInt("code") == 0) {
            notificationJobInfo.setApproveId(res.getJSONObject("data").getStr("instance_code"));
            notificationJobInfo.setApproveStatus(0);
        }
        return this.save(notificationJobInfo);
    }

    @Override
    public boolean updateApproveStatus(String approveId, Integer status) {
        UpdateWrapper<NotificationJobInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("approve_status", status);
        updateWrapper.eq("approve_id", approveId);
        return this.update(updateWrapper);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean deleteByXxlJobId(Integer id){
        QueryWrapper<NotificationJobInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("xxl_job_id", id);
        return notificationJobInfoMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean updateByXxlJobId(Integer id, NotificationJobInfo notificationJobInfo){
        QueryWrapper<NotificationJobInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("xxl_job_id", id);
        NotificationJobInfo searchResult = this.getOne(wrapper);
        if(searchResult != null && !searchResult.getJobEnvironment().equals(notificationJobInfo.getJobEnvironment())){
            getNextNotificationNo(notificationJobInfo);
        }
        return this.update(notificationJobInfo, wrapper);
    }

    private void getNextNotificationNo(NotificationJobInfo notificationJobInfo) {
        String jobNo = notificationJobInfoMapper.getMaxNo(notificationJobInfo.getJobEnvironment());
        String notificationNo;
        if(notificationJobInfo.getJobEnvironment().equals("PROD")){
            notificationNo = String.format("%03d", Integer.parseInt(jobNo) + 1);
        }else{
            notificationNo = "C" + String.format("%03d", Integer.parseInt(jobNo.replace("C", "")) + 1);
        }
        notificationJobInfo.setNotificationNo(notificationNo);
    }

    @Override
    public boolean update(NotificationJobInfo notificationJobInfo) {
        return this.updateById(notificationJobInfo);
    }

    private JSONObject createApproveJson(String username, String notificationDesc, String approveUsername, String approveId) {
        FeishuUser user = feishuService.getFeishuUser(username);
        FeishuUser approveUser = feishuService.getFeishuUser(approveUsername);

        JSONObject formJson = JSONUtil.createObj();
        formJson.set("id", "widget16723660284730001");
        formJson.set("type", "textarea");
        formJson.set("value", notificationDesc);

        JSONObject json = JSONUtil.createObj();
        json.set("approval_code", approveId);
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