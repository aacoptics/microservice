package com.aacoptics.notification.schedule;

import com.aacoptics.notification.mapper.PlanDataMapper;
import com.aacoptics.notification.provider.DingTalkApi;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.utils.DingTalkUtil;
import com.aacoptics.notification.utils.SqlSourceUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiGettokenResponse;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DynamicNotifyTask  implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DynamicNotifyTask.class);

    @Resource
    PlanDataMapper planDataMapper;

    @Resource
    DingTalkApi dingTalkApi;

    @Resource
    FeishuApi feishuApi;

    @Resource
    private SqlSourceUtil sqlSourceUtil;


    public String getPlanKey() {
        return planKey;
    }

    public void setPlanKey(String planKey) {
        this.planKey = planKey;
    }

    private String planKey;

    @Override
    public void run() {
        SimpleDateFormat currentSdf = new SimpleDateFormat("yyyy-MM-dd");
        String WORK_DAY = currentSdf.format(new Date());

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("planKey", planKey);
        queryMap.put("planName", "");

        Map<String, Object> insertJobParam = new HashMap<>();
        insertJobParam.put("PLAN_KEY", planKey);
        insertJobParam.put("WORK_DAY", WORK_DAY);

        List<Map<String, Object>> resultMapList = planDataMapper.filterPlanData(queryMap);
        if(resultMapList == null || resultMapList.isEmpty())
        {
            return;
        }

        Map<String, Object> planMap = resultMapList.get(0);
        String status = planMap.get("status") + "";
        String notifyRobot = planMap.get("notifyRobot") + "";
        String secret = planMap.get("sign") + "";

        if("0".equals(status))
        {
            insertJobParam.put("MSG", "通知计划未启用");
            planDataMapper.insertPlanJob(insertJobParam);

            return;
        }

        Map<String, String> keyMap = new HashMap<>();
        String msgExpr = planMap.get("msgExpr") + "";
        String[] keyArray = msgExpr.split("@@");
        if(keyArray.length == 0)
        {
            insertJobParam.put("MSG", "消息表达式属性不能为空！");
            planDataMapper.insertPlanJob(insertJobParam);

            return;
        }
        for(String keyTxt:keyArray)
        {
            String[] attrArr = keyTxt.split("=");
            if(attrArr.length != 0)
            {
                keyMap.put(attrArr[0], attrArr[1]);
            }
        }

        long agentId = Long.parseLong(keyMap.get("AGENT_ID"));
        String title = keyMap.get("TITLE");
        String url = keyMap.get("URL");

        String message = keyMap.get("MSG");
        if(message == null || "".equals(message))
        {
            insertJobParam.put("MSG", "MSG属性不能为空！");
            planDataMapper.insertPlanJob(insertJobParam);

            return;
        }

        String dateFormat = keyMap.get("DATE_FORMAT");
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        message = message.replace("${date}", sdf.format(calendar.getTime()));

        int groupIdx1 = message.indexOf("(");
        int groupIdx2 = message.indexOf(")");

        StringBuilder notifyMessage = new StringBuilder();

        notifyMessage.append("### ").append(title).append("  \n");

        String messageHead = message.substring(0, groupIdx1);
        String messageContent = message.substring(groupIdx1+1, groupIdx2);
        String messageTear = message.substring(groupIdx2+1);

        notifyMessage.append(messageHead);

        if(keyMap.get("GROUP") == null || "".equals(keyMap.get("GROUP")))
        {
            insertJobParam.put("MSG", "组名不能为空！");
            planDataMapper.insertPlanJob(insertJobParam);

            return;
        }
        String[] groupArray =  keyMap.get("GROUP").split(",");

        String[] attributeArray =  keyMap.get("ATTRIBUTE").split(",");
        String[] attributeValueArray =  keyMap.get("ATTRIBUTE_VALUE").split(",");

        for(String groupName:groupArray)
        {
            String messageContentTxt = messageContent.replace("${group}", groupName);

            int attrIdx1  = messageContentTxt.indexOf("[");
            int attrIdx2 = messageContentTxt.indexOf("]");

            String msgHead = messageContentTxt.substring(0, attrIdx1);
            String msgContent = messageContentTxt.substring(attrIdx1+1, attrIdx2);
            String msgTear = messageContentTxt.substring(attrIdx2+1);

            notifyMessage.append(msgHead);

            Map<String, Object> groupMap = excuteDynamicSQL(groupName, keyMap);
            for(int j=0;j<attributeArray.length;j++)
            {

                String attribute = attributeArray[j];
                String attributeValue = groupMap.get(attributeValueArray[j]) + "";
                if("null".equalsIgnoreCase(attributeValue))
                {
                    attributeValue = "0" + "  \n";
                }

                String msgContentConcat = msgContent.replace("${attribute}", attribute);
                if(attribute.contains("**"))
                {
                    attributeValue = "**" + attributeValue + "**";
                }
                msgContentConcat = msgContentConcat.replace("${attributeValue}", attributeValue);

                notifyMessage.append(msgContentConcat);
            }
            notifyMessage.append(msgTear);
        }
        notifyMessage.append(messageTear);

        notifyMessage.append("[查看详情](").append(url).append(")  \n");

        String msgType = planMap.get("msgType") + "";
        String notifyType = planMap.get("notifyType") + "";
        // String notifyRobot = planMap.get("notifyRobot") + "";

        String userids = "";
        List<Map<String,Object>> planContactList = planDataMapper.filterPlanContact(queryMap);
        if(planContactList != null && !planContactList.isEmpty())
        {
            for(int i=0;i<planContactList.size();i++)
            {
                Map<String, Object> contactData = planContactList.get(i);
                userids += (contactData.get("tel") + ",");
            }
            userids = userids.substring(0, userids.length() - 1);
        }

        JSONObject jsonMarkdown = new JSONObject();
        jsonMarkdown.put("msgtype", "markdown");

        JSONObject markdown = new JSONObject();
        markdown.put("title", "销售数据通知");
        markdown.put("text", notifyMessage.toString());

        jsonMarkdown.put("markdown", markdown);

        if("DingTalk".equals(msgType))
        {
            //获取token
            OapiGettokenResponse oapiGettokenResponse = null;
            try {
                oapiGettokenResponse = dingTalkApi.getAccessToken();
                String accessToken = oapiGettokenResponse.getAccessToken();
                if(!"Robot".equals(notifyType))
                {
                    DingTalkUtil.sendCardCorpConversation(accessToken, agentId, userids, title, jsonMarkdown.toJSONString(), "查看详情", url);
                }
                else
                {
                    String notifyURL = notifyRobot;
                            // "https://oapi.dingtalk.com/robot/send?access_token=48df943458770124cbeaf8a983032d3fca5a7d88bcf10dfeef2049857c254f20";
                    Long timestamp = System.currentTimeMillis();
                    // String secret = "SECf5213c029551084197995dc59bd0328ea177fcf7a4b0c33d7057a8487e5d4ff5";

                    String stringToSign = timestamp + "\n" + secret;
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                    byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                    String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");

                    if(!"".equals(secret))
                    {
                        notifyURL += ("&timestamp=" + timestamp);
                        notifyURL += ("&sign=" + sign);
                    }

                    notifyMessage = new StringBuilder(notifyMessage.toString().replaceAll("&&t", "\\\n"));

                    DingTalkUtil.sendGroupRobotMessage(notifyURL, title, notifyMessage.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if("FeiShu".equals(msgType))
        {
            String resultMesaage = feishuApi.SendGroupMessage(notifyRobot, notifyMessage.toString());
            JSONObject messageJson = new JSONObject();
            try {
                messageJson = JSONObject.parseObject(resultMesaage);
            } catch (Exception err) {
                logger.error("解析返回值失败！{}", err.getMessage());
                insertJobParam.put("MSG", err.getMessage());
                planDataMapper.insertPlanJob(insertJobParam);
                return;
            }
        }
        insertJobParam.put("MSG", notifyMessage.toString());
        planDataMapper.insertPlanJob(insertJobParam);

        logger.info("DynamicNotifyTask execute times:{}");
    }

    public Map<String, Object> excuteDynamicSQL(String groupName, Map<String,String> keyMap)
    {
        String TABLE = keyMap.get("TABLE");
        if(TABLE == null || "".equals(TABLE))
        {
            return null;
        }

        String GROUP_COLUMN = keyMap.get("GROUP_COLUMN");
        String DATE_COLUMN = keyMap.get("DATE_COLUMN");
        String DATE_FORMAT = keyMap.get("DATE_FORMAT");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateTxt = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        dateTxt = sdf.format(calendar.getTime());

        String ATTRIBUTE_COLUMN = keyMap.get("ATTRIBUTE_COLUMN");
        String sql = "SELECT " + ATTRIBUTE_COLUMN + " FROM " + TABLE
                   + " WHERE " + DATE_COLUMN + " = '" + dateTxt
                   + "' AND " + GROUP_COLUMN + " = '" + groupName + "' " + "";

        Map<String,Object> parameters = new HashMap<>();

        //拼接sql
        // String s = sqlSourceUtil.sqlSource(sql, parameters);

        //执行sql，并缓存sql，直接返回执行结果

        Long cacheId = (new Random()).nextLong();//缓存id,唯一

        List<Map<String, Object>> queryResult = sqlSourceUtil.query(sql, cacheId, parameters);
        if(queryResult != null && !queryResult.isEmpty())
        {
            return queryResult.get(0);
        }
        else
        {
            return null;
        }
    }
}
