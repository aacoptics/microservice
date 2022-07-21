package com.aacoptics.notification.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : Yan Shangqi
 * @CreateTime : 2021/5/15
 * @Description :
 **/

@Configuration
public class BusConfig {
    //绑定键
    public final static String EMAIL_ROUTING = "notification.email";
    public final static String DINGTALK_ROUTING = "notification.dingTalk";
    public final static String EXCHANGE_NAME = "microservice-functional";

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_ROUTING);
    }

    @Bean
    public Queue dingTalkQueue() {
        return new Queue(DINGTALK_ROUTING);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding bindingEmailExchangeMessage() {
        return BindingBuilder.bind(emailQueue()).to(exchange()).with(EMAIL_ROUTING);
    }

    @Bean
    Binding bindingDingTalkExchangeMessage() {
        return BindingBuilder.bind(dingTalkQueue()).to(exchange()).with(DINGTALK_ROUTING);
    }
}