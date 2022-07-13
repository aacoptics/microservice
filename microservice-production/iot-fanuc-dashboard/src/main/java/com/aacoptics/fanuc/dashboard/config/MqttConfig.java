package com.aacoptics.fanuc.dashboard.config;

import com.aacoptics.fanuc.dashboard.entity.FanucDataEntity;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.amqp.core.Base64UrlNamingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Configuration
@IntegrationComponentScan
@Getter
@Setter
public class MqttConfig {

    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";

    public static final String INPUT_CHANNEL = "mqttInputChannel";

    @Value("${mqtt.subTopic}")
    public String SUB_TOPICS;

    public static Map<String, FanucDataEntity> FanucMachineDataMap = new TreeMap<>();

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.serverURIs}")
    private String hostUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String defaultTopic;

    @PostConstruct
    public void init() {
        log.debug("username:{} password:{} hostUrl:{} clientId :{} defaultTopic :{}",
                this.username, this.password, this.hostUrl, this.clientId, this.defaultTopic);
    }

    @Bean
    public MqttPahoClientFactory clientFactory() {

        final MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{hostUrl});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(5000);
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean(value = OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler mqttOutbound() {
        String uniqueClintId = new Base64UrlNamingStrategy(clientId + "_").generateName();
        final MqttPahoMessageHandler handler = new MqttPahoMessageHandler(uniqueClintId, clientFactory());
        handler.setDefaultQos(1);
        handler.setDefaultRetained(true);
        handler.setDefaultTopic(defaultTopic);
        handler.setAsync(false);
        handler.setAsyncEvents(false);
        return handler;
    }

    /**
     * MQTT消息接收处理
     */
    //接收通道
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听的topic
    @Bean
    public MessageProducer inbound() {
        String uniqueClintId = new Base64UrlNamingStrategy(clientId + "_").generateName();
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        uniqueClintId + "_inbound", clientFactory(), SUB_TOPICS.split(","));
        adapter.setCompletionTimeout(3000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = INPUT_CHANNEL)
    public MessageHandler handler() {
        return message -> {
            JSONObject msgJson = JSONObject.parseObject(message.getPayload().toString());
            FanucDataEntity fanucDataEntity = new FanucDataEntity();
            if (FanucMachineDataMap.containsKey(msgJson.getString("ClientId")))
                fanucDataEntity = FanucMachineDataMap.get(msgJson.getString("ClientId"));
            switch (msgJson.getString("Message")) {
                case "moldData":
                    fanucDataEntity.setMoldData(msgJson.getJSONObject("Data"));
                    FanucMachineDataMap.put(msgJson.getString("ClientId"), fanucDataEntity);
                    break;
                case "condData":
                    fanucDataEntity.setCondData(msgJson.getJSONObject("Data"));
                    FanucMachineDataMap.put(msgJson.getString("ClientId"), fanucDataEntity);
                    break;
                case "monitData":
                    fanucDataEntity.setMonitData(msgJson.getJSONObject("Data"));
                    FanucMachineDataMap.put(msgJson.getString("ClientId"), fanucDataEntity);
                    break;
            }
        };
    }

}