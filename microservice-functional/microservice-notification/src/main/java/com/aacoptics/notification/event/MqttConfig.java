//package com.aacoptics.notification.event;
//
//import com.aacoptics.notification.entity.vo.MarkdownGroupMessage;
//import com.aacoptics.notification.entity.vo.MarkdownMessage;
//import com.aacoptics.notification.provider.DingTalkApi;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.primitives.Ints;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.IntegrationComponentScan;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.core.MessageProducer;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
//import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
//import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.lang.reflect.Array;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//import java.util.TreeMap;
//
//@Slf4j
//@Configuration
//@IntegrationComponentScan
//@Getter
//@Setter
//public class MqttConfig {
//
//    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";
//
//    public static final String INPUT_CHANNEL = "mqttInputChannel";
//
//    @Value("${mqtt.subTopic}")
//    public String SUB_TOPICS;
//
//    @Value("${mqtt.username}")
//    private String username;
//
//    @Value("${mqtt.password}")
//    private String password;
//
//    @Value("${mqtt.serverURIs}")
//    private String hostUrl;
//
//    @Value("${mqtt.client.id}")
//    private String clientId;
//
//    @Value("${mqtt.topic}")
//    private String defaultTopic;
//
//    @Resource
//    DingTalkApi dingTalkApi;
//
//    @PostConstruct
//    public void init() {
//        log.debug("username:{} password:{} hostUrl:{} clientId :{} defaultTopic :{}",
//                this.username, this.password, this.hostUrl, this.clientId, this.defaultTopic);
//    }
//
//    @Bean
//    public MqttPahoClientFactory clientFactory() {
//
//        final MqttConnectOptions options = new MqttConnectOptions();
//        options.setServerURIs(new String[]{hostUrl});
//        options.setUserName(username);
//        options.setPassword(password.toCharArray());
//        options.setAutomaticReconnect(true);
//        options.setKeepAliveInterval(5000);
//        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
//        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        factory.setConnectionOptions(options);
//        return factory;
//    }
//
//    @Bean(value = OUTBOUND_CHANNEL)
//    public MessageChannel mqttOutboundChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
//    public MessageHandler mqttOutbound() {
//
//        final MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, clientFactory());
//        handler.setDefaultQos(1);
//        handler.setDefaultRetained(true);
//        handler.setDefaultTopic(defaultTopic);
//        handler.setAsync(false);
//        handler.setAsyncEvents(false);
//        return handler;
//    }
//
//    /**
//     * MQTT消息接收处理
//     */
//    //接收通道
//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }
//
//    //配置client,监听的topic
//    @Bean
//    public MessageProducer inbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter =
//                new MqttPahoMessageDrivenChannelAdapter(
//                        clientId + "_inbound", clientFactory(), SUB_TOPICS.split(","));
//        adapter.setCompletionTimeout(3000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//
//    //通过通道获取数据
//    @Bean
//    @ServiceActivator(inputChannel = INPUT_CHANNEL)
//    public MessageHandler handler() {
//        return message -> {
//            JSONObject msgJson = JSONObject.parseObject(message.getPayload().toString());
//            if (msgJson.getString("Message").equals("DoMonitorTempAlarm")) {
//                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                //当前时间转换String
//                LocalDateTime time = LocalDateTime.now();
//                String localTimeStr = df.format(time);
//                JSONObject dataJson = msgJson.getJSONObject("Data");
//                MarkdownMessage markdownGroupMessage = new MarkdownMessage();
//                markdownGroupMessage.setTitle("加热棒状态报警");
//                String projectName = dataJson.getString("projectName");
//                String modelName = dataJson.getString("modelName");
//                String param = dataJson.getString("param");
//                String machineName = dataJson.getString("machineName");
//                JSONArray abnormalIdxJson = dataJson.getJSONArray("abnormalIdx");
//                int[] abnormalIdx =  JSONArray.toJavaObject(abnormalIdxJson, int[].class);
//                String abnormalStr = Ints.join(",", abnormalIdx);
//                markdownGroupMessage.addBlobContent(machineName + " " + projectName + " " + modelName);
//                markdownGroupMessage.addBlobContent(localTimeStr);
//                if (param.equals("lower")) {
//                    markdownGroupMessage.addContent("下加热床 " + abnormalStr + "号加热棒温度低于平均值5℃。生产人员及时通知设备人员检查加热棒状态，通知工艺人员确定产品性能。");
//                } else {
//                    markdownGroupMessage.addContent("上加热床 " + abnormalStr + "号加热棒温度低于平均值5℃。生产人员及时通知设备人员检查加热棒状态，通知工艺人员确定产品性能。");
//                }
//                String robotUrl = "https://oapi.dingtalk.com/robot/send?access_token=bcf308c4ee97a16d9265365d27001de7f42794d9018702fd253c2d1b28bc442a";
//                try {
//                    Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage(robotUrl, "加热棒状态报警", markdownGroupMessage.toString());
//                    JSONObject resultMapJson = (JSONObject) JSONObject.toJSON(resultMap);
//                    log.debug(JSONObject.toJSONString(resultMapJson));
//                    String result = resultMap.get("result");
//                    String resMsg = resultMap.get("message");
//                    if (!StringUtils.isEmpty(resMsg) && resMsg.length() > 1024) {
//                        resMsg = resMsg.substring(1024);
//                    }
//                } catch (Exception err) {
//
//                }
//
//
//
//            }
//
//        };
//    }
//
//}