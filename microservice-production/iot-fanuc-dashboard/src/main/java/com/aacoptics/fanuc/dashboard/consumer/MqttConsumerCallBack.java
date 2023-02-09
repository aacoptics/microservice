package com.aacoptics.fanuc.dashboard.consumer;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.fanuc.dashboard.entity.vo.MarkdownMessage;
import com.aacoptics.fanuc.dashboard.provider.FeishuApi;
import com.aacoptics.fanuc.dashboard.service.FeishuService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j

public class MqttConsumerCallBack implements MqttCallbackExtended {

    private MqttClient client;

    private MqttConnectOptions options;

    private String subTopics;

    private FeishuApi feishuApi;

    private FeishuService feishuService;

    public MqttConsumerCallBack(MqttClient client,
                                MqttConnectOptions options,
                                String subTopics,
                                FeishuApi feishuApi,
                                FeishuService feishuService) {
        this.client = client;
        this.options = options;
        this.subTopics = subTopics;
        this.feishuApi = feishuApi;
        this.feishuService = feishuService;
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
    public void messageArrived(String topic, MqttMessage message) {
        JSONObject msgJson = JSONObject.parseObject(new String(message.getPayload()));
        if (StrUtil.isBlank(msgJson.getString("Message")))
            return;
        log.info("message:", message);

        //当前时间转换String
        JSONObject dataJson = msgJson.getJSONObject("data");
        String machineName = dataJson.getString("machineName"); //机台

        String dateTime = dataJson.getString("dateTime");//时间

        MarkdownMessage markdownGroupMessage = new MarkdownMessage();
        String title = null;
        switch (msgJson.getString("Message")) {
            case "bufferException":
                title = "注塑机最小缓冲异常";
                markdownGroupMessage.setTitle(title);
                markdownGroupMessage.addContent("设备：" + machineName);
                markdownGroupMessage.addContent("检查时间：" + dateTime);
                markdownGroupMessage.addContent("最小缓冲值等于0");
                break;
            case "temperatureAnomaly":
                title = "注塑机检测温度异常";
                markdownGroupMessage.setTitle(title);

                String abnormalParam = dataJson.getString("abnormalParam"); //检查项
                String currentValue = dataJson.getString("currentValue"); //当前值
                String setValue = dataJson.getString("setValue");//设定值

                markdownGroupMessage.addContent("设备：" + machineName);
                markdownGroupMessage.addContent("检查项：" + abnormalParam);
                markdownGroupMessage.addContent("检查时间：" + dateTime);
                markdownGroupMessage.addContent("设定值：" + setValue);
                markdownGroupMessage.addContent("当前值：" + currentValue);
                break;
        }

        if(markdownGroupMessage == null || StrUtil.isBlank(markdownGroupMessage.toString()))
            return;
        String chatName = "零件注塑机每日点监IOT系统";
        try {
            if (StrUtil.isBlank(title)) {
                log.error("title为空");
                return;
            }
            String chatId = feishuService.fetchChatIdByRobot(chatName);
            cn.hutool.json.JSONObject cardJson = feishuApi.getMarkdownMessage(markdownGroupMessage.toString(), null);
            feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                    chatId,
                    FeishuService.MSG_TYPE_INTERACTIVE,
                    cardJson);
        } catch (Exception err) {
            log.error("发送" + title + "失败！", err);
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
        if (client.isConnected()) {
            log.info("mqtt连接成功");
            //订阅主题
            try {
                String[] subTopicArray = subTopics.split(",");
                for (String subTopic : subTopicArray) {
                    client.subscribe(subTopic, 2);
                }
                log.info("mqtt订阅成功");
            } catch (MqttException e) {
                log.error("mqtt订阅失败", e);
            }
        } else {
            log.error("mqtt连接失败");
        }
    }

}
