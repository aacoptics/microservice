package com.aacoptics.notification.event;

import com.aacoptics.notification.entity.vo.MarkdownMessage;
import com.aacoptics.notification.provider.DingTalkApi;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class MqttConsumerCallBack implements MqttCallbackExtended{

    private MqttClient client;

    private MqttConnectOptions options;

    private String subTopics;

    @Resource
    DingTalkApi dingTalkApi;

    public MqttConsumerCallBack(MqttClient client, MqttConnectOptions options, String subTopics) {
        this.client = client;
        this.options = options;
        this.subTopics = subTopics;
    }

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("MQTT连接断开，发起重连......");
    }



    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        JSONObject msgJson = JSONObject.parseObject(new String(message.getPayload()));
        if (msgJson.getString("Message").equals("DoMonitorTempAlarm")) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            //当前时间转换String
            LocalDateTime time = LocalDateTime.now();
            String localTimeStr = df.format(time);
            JSONObject dataJson = msgJson.getJSONObject("Data");
            MarkdownMessage markdownGroupMessage = new MarkdownMessage();
            markdownGroupMessage.setTitle("加热棒状态报警");
            String projectName = dataJson.getString("projectName");
            String modelName = dataJson.getString("modelName");
            String param = dataJson.getString("param");
            String machineName = dataJson.getString("machineName");
            JSONArray abnormalIdxJson = dataJson.getJSONArray("abnormalIdx");
            int[] abnormalIdx =  JSONArray.toJavaObject(abnormalIdxJson, int[].class);
            String abnormalStr = Ints.join(",", abnormalIdx);
            markdownGroupMessage.addBlobContent(machineName + " " + projectName + " " + modelName);
            markdownGroupMessage.addBlobContent(localTimeStr);
            if (param.equals("lower")) {
                markdownGroupMessage.addContent("下加热床 " + abnormalStr + "号加热棒温度低于平均值5℃。生产人员及时通知设备人员检查加热棒状态，通知工艺人员确定产品性能。");
            } else {
                markdownGroupMessage.addContent("上加热床 " + abnormalStr + "号加热棒温度低于平均值5℃。生产人员及时通知设备人员检查加热棒状态，通知工艺人员确定产品性能。");
            }
            String robotUrl = "https://oapi.dingtalk.com/robot/send?access_token=bcf308c4ee97a16d9265365d27001de7f42794d9018702fd253c2d1b28bc442a";
            try {
                Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage(robotUrl, "加热棒状态报警", markdownGroupMessage.toString());
                JSONObject resultMapJson = (JSONObject) JSONObject.toJSON(resultMap);
                log.debug(JSONObject.toJSONString(resultMapJson));
                String result = resultMap.get("result");
                String resMsg = resultMap.get("message");
                if (!StringUtils.isEmpty(resMsg) && resMsg.length() > 1024) {
                    resMsg = resMsg.substring(1024);
                }
            } catch (Exception err) {

            }



        }
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void connectComplete(boolean b, String s) {
        if(client.isConnected()) {
            log.info("mqtt连接成功");
            //订阅主题
            try {
                client.subscribe(subTopics, 2);
                log.info("mqtt订阅成功");
            } catch (MqttException e) {
                log.error("mqtt订阅失败", e);
            }
        }else{
            log.error("mqtt连接失败");
        }
    }
}