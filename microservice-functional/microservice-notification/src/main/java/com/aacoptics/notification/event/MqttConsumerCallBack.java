package com.aacoptics.notification.event;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.notification.entity.form.SpeakerVoiceFileInfo;
import com.aacoptics.notification.entity.vo.MarkdownMessage;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.provider.JacobProvider;
import com.aacoptics.notification.service.FeishuService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.scheduling.annotation.Async;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j

public class MqttConsumerCallBack implements MqttCallbackExtended {

    private MqttClient client;

    private MqttConnectOptions options;

    private String subTopics;

    private JacobProvider jacobProvider;

    private FeishuApi feishuApi;

    private FeishuService feishuService;

    public MqttConsumerCallBack(MqttClient client,
                                MqttConnectOptions options,
                                String subTopics,
                                JacobProvider jacobProvider,
                                FeishuApi feishuApi,
                                FeishuService feishuService) {
        this.client = client;
        this.options = options;
        this.subTopics = subTopics;
        this.jacobProvider = jacobProvider;
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
//        JSONObject msgJson = JSONObject.parseObject(new String(message.getPayload()));
//        if (StrUtil.isBlank(msgJson.getString("Message")))
//            return;
//
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        //当前时间转换String
//        LocalDateTime time = LocalDateTime.now();
//        String localTimeStr = df.format(time);
//        JSONObject dataJson = msgJson.getJSONObject("Data");
//        String machineName = dataJson.getString("machineName");
//        String projectName = dataJson.getString("projectName");
//        String modelName = dataJson.getString("modelName");
//        MarkdownMessage markdownGroupMessage = new MarkdownMessage();
//        String title = null;
//        switch (msgJson.getString("Message")) {
//            case "DoMonitorTempAlarm":
//                title = "加热棒状态报警";
//                markdownGroupMessage.setTitle(title);
//                String param = dataJson.getString("param");
//                JSONArray abnormalIdxJson = dataJson.getJSONArray("abnormalIdx");
//                int[] abnormalIdx = JSONArray.toJavaObject(abnormalIdxJson, int[].class);
//                String abnormalStr = Ints.join(",", abnormalIdx);
//                markdownGroupMessage.addBlobContent(machineName + " " + projectName + " " + modelName);
//                markdownGroupMessage.addBlobContent(localTimeStr);
//                if (param.equals("lower")) {
//                    markdownGroupMessage.addContent("下加热床 " + abnormalStr + "号加热棒温度低于平均值5℃。生产人员及时通知设备人员检查加热棒状态，通知工艺人员确定产品性能。");
//                } else {
//                    markdownGroupMessage.addContent("上加热床 " + abnormalStr + "号加热棒温度低于平均值5℃。生产人员及时通知设备人员检查加热棒状态，通知工艺人员确定产品性能。");
//                }
//                break;
////            case "FeedAlarm":
////                title = "模造换料提醒";
////                markdownGroupMessage.setTitle(title);
////                markdownGroupMessage.addBlobContent(localTimeStr);
////                markdownGroupMessage.addBlobContent(machineName + " " + projectName + " " + modelName);
////                markdownGroupMessage.addContent("机台需要换料，请相关人员进行处理！");
////                sendToAllSpeaker(machineName);
////                break;
//            case "moldTempMonitor":
//                title = "模造温度曲线报警";
//                String moldParam = dataJson.getString("param");
//                String avgValue = dataJson.getString("averageValue");
//                String currentValue = dataJson.getString("currentValue");
//                String recipePhase = dataJson.getString("recipePhase");
//                markdownGroupMessage.setTitle(title);
//                markdownGroupMessage.addBlobContent(machineName + " " + projectName + " " + modelName);
//                markdownGroupMessage.addBlobContent("阶段：" + recipePhase);
//                markdownGroupMessage.addBlobContent(localTimeStr);
//                if (moldParam.indexOf("upper") == 0)
//                    markdownGroupMessage.addContent("机台上模具温度异常，平均值为" + avgValue + "，当前值为" + currentValue + "，请相关人员检查!");
//                else if (moldParam.indexOf("lower") == 0)
//                    markdownGroupMessage.addContent("机台下模具温度异常，平均值为" + avgValue + "，当前值为" + currentValue + "，请相关人员检查!");
//                else
//                    markdownGroupMessage.addContent("机台模具温度异常，请相关人员检查!");
//                break;
//        }
//
//        String chatName = "模造车间异常&换料自动提醒群";
//        try {
//            if (StrUtil.isBlank(title)) {
//                log.error("title为空");
//                return;
//            }
//            String chatId = feishuService.fetchChatIdByRobot(chatName);
//            cn.hutool.json.JSONObject cardJson = feishuApi.getMarkdownMessage(markdownGroupMessage.toString(), null);
//            feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
//                    chatId,
//                    FeishuService.MSG_TYPE_INTERACTIVE,
//                    cardJson);
//        } catch (Exception err) {
//            log.error("发送" + title + "失败！", err);
//        }
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


    static final List<SpeakerVoiceFileInfo> speakerInfos = Arrays.asList(
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.125").setSpeakerSn("ls20://02008669A7B1").setSpeakerPort(8888),
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.126").setSpeakerSn("ls20://02026DFB7B6D").setSpeakerPort(8888),
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.127").setSpeakerSn("ls20://020FF3BC43E6").setSpeakerPort(8888),
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.128").setSpeakerSn("ls20://0202C68F8D6B").setSpeakerPort(8888)
    );


    @Async
    public void sendToAllSpeaker(String machineName) {
        for (SpeakerVoiceFileInfo speakerInfo : speakerInfos) {
            sendVoiceToSpeaker(machineName, speakerInfo.getSpeakerSn(), speakerInfo.getSpeakerIp(), speakerInfo.getSpeakerPort());
        }
    }

    @Async
    public void sendVoiceToSpeaker(String machineName, String speakerSn, String speakerIp, Integer speakerPort) {
        SpeakerVoiceFileInfo speakerVoiceFileInfo = new SpeakerVoiceFileInfo();
        speakerVoiceFileInfo.setMessage(machineName + "机台需要换料，请注意\n"
                + machineName + "机台需要换料，请注意\n"
                + machineName + "机台需要换料，请注意\n");
        speakerVoiceFileInfo.setSpeakerIp(speakerIp).setSpeakerSn(speakerSn).setSpeakerPort(speakerPort);
        jacobProvider.sendToSpeaker(speakerVoiceFileInfo);
    }
}
