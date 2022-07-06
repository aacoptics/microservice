package com.aacoptics.notification.config;

import javax.annotation.PostConstruct;

import com.aacoptics.notification.event.MqttConsumerCallBack;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.amqp.core.Base64UrlNamingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MqttConsumerConfig {
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

    @Value("${mqtt.subTopic}")
    private String subTopics;

    /**
     * 客户端对象
     */
    private MqttClient client;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init(){
        connect();
    }

    /**
     * 客户端连接服务端
     */
    public void connect(){
        try {
            String clientIdUnique = new Base64UrlNamingStrategy(clientId + "_").generateName();
            //创建MQTT客户端对象
            client = new MqttClient(hostUrl,clientIdUnique,new MemoryPersistence());
            //连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置为false表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接到服务端都是以新的身份
            options.setCleanSession(true);
            //设置连接用户名
            options.setUserName(username);
            //设置连接密码
            options.setPassword(password.toCharArray());

            options.setAutomaticReconnect(true);
            //设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            //设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic",(clientIdUnique + "与服务器断开连接").getBytes(),0,false);
            //设置回调
            client.setCallback(new MqttConsumerCallBack(client, options, subTopics));
            client.connect(options);
        } catch (MqttException e) {
            log.error("MQTT连接失败！",e);
        }
    }

    /**
     * 断开连接
     */
    public void disConnect(){
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 订阅主题
     */
    public void subscribe(String topic,int qos){
        try {
            client.subscribe(topic,qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
