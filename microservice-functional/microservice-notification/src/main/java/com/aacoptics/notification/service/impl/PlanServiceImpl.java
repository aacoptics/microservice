package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.mapper.PlanDataMapper;
import com.aacoptics.notification.mapper.UserMapper;
import com.aacoptics.notification.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PlanServiceImpl implements PlanService {

    @Autowired
    PlanDataMapper planDataMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    @CrossOrigin
    public String insertPlanData(Map<String, String> params) throws Exception {

        String PLAN_KEY = params.get("planKey");
        String PLAN_NAME = params.get("planName");
        String PLAN_CRON = params.get("planCron");
        String MSG_TYPE = params.get("msgType");
        String NOTIFY_TYPE = params.get("notifyType");
        String ROBOT = params.get("notifyRobot");
        String sign = params.get("sign");
        sign = "".equals(sign)?null:sign;
        String MSG_EXPR = params.get("msgExpr");
        MSG_EXPR = URLDecoder.decode(MSG_EXPR, "UTF-8");
        String TRIGGER_TYPE = params.get("triggerType");
        String PLAN_MONTH = params.get("planMonth");
        String PLAN_DAY = params.get("planDay");
        String PLAN_HOUR = params.get("planHour");
        String PLAN_MINUTE = params.get("planMinute");
        String PLAN_SECOND = params.get("planSecond");

        int STATUS = "true".equals(params.get("statusFlag"))?1:0;
        String TIME_PERIOD = params.get("timePeriod");
        String CREATE_USER = params.get("user");

        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("planKey", PLAN_KEY);
        List<Map<String, Object>> existPlanData = planDataMapper.filterPlanData(queryParam);
        if(existPlanData != null && !existPlanData.isEmpty())
        {
            return "已存在同计划主键的数据，无法保存！";
        }

        Map<String, Object> insertParam = new HashMap<>();
        insertParam.put("PLAN_KEY", PLAN_KEY);
        insertParam.put("PLAN_NAME", PLAN_NAME);
        insertParam.put("PLAN_CRON", PLAN_CRON);
        insertParam.put("MSG_TYPE", MSG_TYPE);
        insertParam.put("NOTIFY_TYPE", NOTIFY_TYPE);
        insertParam.put("ROBOT", ROBOT);
        insertParam.put("SECRET_COL", sign);
        insertParam.put("MSG_TYPE", MSG_TYPE);
        insertParam.put("MSG_EXPR", MSG_EXPR);
        insertParam.put("TRIGGER_TYPE", TRIGGER_TYPE);

        if("按周期".equals(TRIGGER_TYPE))
        {
            insertParam.put("PLAN_MONTH", null);
            insertParam.put("PLAN_DAY", null);
            insertParam.put("PLAN_HOUR", null);
            insertParam.put("PLAN_MINUTE", null);
            insertParam.put("PLAN_SECOND", null);
            insertParam.put("TIME_PERIOD", Integer.parseInt(TIME_PERIOD));
        }
        else
        {
            insertParam.put("PLAN_MONTH", PLAN_MONTH);
            insertParam.put("PLAN_DAY", PLAN_DAY);
            insertParam.put("PLAN_HOUR", PLAN_HOUR);
            insertParam.put("PLAN_MINUTE", PLAN_MINUTE);
            insertParam.put("PLAN_SECOND", PLAN_SECOND);
            insertParam.put("TIME_PERIOD", null);
        }
        insertParam.put("STATUS", STATUS);
        insertParam.put("CREATE_USER", CREATE_USER);
        planDataMapper.insertPlanData(insertParam);
        return "";
    }

    @Override
    @CrossOrigin
    public String insertPlanContact(String planKey, String[] contactArray) throws Exception {

        planDataMapper.deletePlanContact(planKey);
        for(int i=0;i<contactArray.length;i++)
        {
            String userNo = contactArray[i];

            Map<String, Object> insertParam = new HashMap<>();
            insertParam.put("PLAN_KEY", planKey);
            insertParam.put("USER_NO", userNo);
            planDataMapper.insertPlanContact(insertParam);
        }
        return "";
    }

    @Override
    @CrossOrigin
    public String updatePlanData(Map<String, String> params) throws Exception {

        String PLAN_KEY = params.get("planKey");
        String PLAN_NAME = params.get("planName");
        String PLAN_CRON = params.get("planCron");
        String MSG_TYPE = params.get("msgType");
        String NOTIFY_TYPE = params.get("notifyType");
        if("".equals(NOTIFY_TYPE))
        {
            NOTIFY_TYPE = null;
        }
        String ROBOT = params.get("notifyRobot");
        if("".equals(ROBOT))
        {
            ROBOT = null;
        }

        String sign = params.get("sign");
        if("".equals(sign))
        {
            sign = null;
        }

        String MSG_EXPR = params.get("msgExpr");
        MSG_EXPR = URLDecoder.decode(MSG_EXPR, "UTF-8");
        String TRIGGER_TYPE = params.get("triggerType");
        String PLAN_MONTH = params.get("planMonth");
        String PLAN_DAY = params.get("planDay");
        String PLAN_HOUR = params.get("planHour");
        String PLAN_MINUTE = params.get("planMinute");
        String PLAN_SECOND = params.get("planSecond");

        int STATUS = "true".equals(params.get("statusFlag"))?1:0;
        String TIME_PERIOD = params.get("timePeriod");
        String UPDATE_USER = params.get("user");

        planDataMapper.deletePlanContact(PLAN_KEY);

        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put("PLAN_KEY", PLAN_KEY);
        updateParam.put("PLAN_NAME", PLAN_NAME);
        updateParam.put("PLAN_CRON", PLAN_CRON);
        updateParam.put("MSG_TYPE", MSG_TYPE);
        updateParam.put("NOTIFY_TYPE", NOTIFY_TYPE);
        updateParam.put("ROBOT", ROBOT);
        updateParam.put("SECRET_COL", sign);
        updateParam.put("MSG_EXPR", MSG_EXPR);
        updateParam.put("TRIGGER_TYPE", TRIGGER_TYPE);

        if("按周期".equals(TRIGGER_TYPE))
        {
            updateParam.put("PLAN_MONTH", null);
            updateParam.put("PLAN_DAY", null);
            updateParam.put("PLAN_HOUR", null);
            updateParam.put("PLAN_MINUTE", null);
            updateParam.put("PLAN_SECOND", null);
            updateParam.put("TIME_PERIOD", Integer.parseInt(TIME_PERIOD));
        }
        else
        {
            updateParam.put("PLAN_MONTH", PLAN_MONTH);
            updateParam.put("PLAN_DAY", PLAN_DAY);
            updateParam.put("PLAN_HOUR", PLAN_HOUR);
            updateParam.put("PLAN_MINUTE", PLAN_MINUTE);
            updateParam.put("PLAN_SECOND", PLAN_SECOND);
            updateParam.put("TIME_PERIOD", null);
        }
        updateParam.put("STATUS", STATUS);

        updateParam.put("UPDATE_USER", UPDATE_USER);
        planDataMapper.updatePlanData(updateParam);
        return "";
    }

    @Override
    @CrossOrigin
    public List<Map<String, Object>> filterPlanData(String planKey, String planName)
    {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("planKey", planKey);
        queryParam.put("planName", planName);

        List<Map<String, Object>> resultList = planDataMapper.filterPlanData(queryParam);
        return resultList;
    }

    @Override
    @CrossOrigin
    public List<Map<String, Object>> filterPlanJOB(String planKey, String planName, String workDay)
    {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("planKey", planKey);
        queryParam.put("planName", planName);
        queryParam.put("workDay", workDay);

        List<Map<String, Object>> resultList = planDataMapper.filterPlanJOB(queryParam);
        return resultList;
    }


    @Override
    @CrossOrigin
    public List<Map<String, Object>> queryScheduledPlan()
    {
        Map<String, Object> queryParam = new HashMap<>();

        List<Map<String, Object>> resultList = planDataMapper.queryScheduledPlan(queryParam);
        return resultList;
    }

    @Override
    @CrossOrigin
    public List<Map<String, Object>> filterContactData(String userName)
    {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("userName", userName);

        List<Map<String, Object>> resultList = planDataMapper.filterContactData(queryParam);
        return resultList;
    }

    @Override
    @CrossOrigin
    public List<Map<String, Object>> filterPlanContact(String planKey)
    {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("planKey", planKey);

        List<Map<String, Object>> resultList = planDataMapper.filterPlanContact(queryParam);
        return resultList;
    }

    @Override
    @CrossOrigin
    public String updateUser()
    {
        Map<String, Object> queryParam = new HashMap<>();
        List<Map<String, Object>> userList = userMapper.findAllUsers(queryParam);

        try {
            if (userList != null && !userList.isEmpty()) {
                for (int i = 0; i < userList.size(); i++) {
                    Map<String, Object> userMap = userList.get(i);
                    String USER_STATUS = userMap.get("USER_STATUS") + "";
                    String USER_NO = userMap.get("USER_NO") + "";

                    planDataMapper.deleteContactData(USER_NO);
                    if("1".equals(USER_STATUS))
                    {
                        planDataMapper.insertContactData(userMap);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return "更新用户出现异常！";
        }
        return "更新用户成功！";
    }
}
