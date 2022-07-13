package com.aacoptics.fanuc.dashboard.config;//package com.aacoptics.fanuc.dashboard.config;
//
//import com.aacoptics.fanuc.dashboard.consumer.FanucConsumer;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
//import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Slf4j
//public class BusConfig {
//
//    private static final String EXCHANGE_NAME = "aac-optics-fanuc-data";
//    private static final String ROUTING_KEY = "fanuc-data";
//
//    @Bean
//    Queue queue() {
//        String queueName = new Base64UrlNamingStrategy(ROUTING_KEY + ".").generateName();
//        return new Queue(queueName, true, false, true);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(EXCHANGE_NAME);
//    }
//
//    @Bean
//    Binding bindingMonit(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
//    }
//
//    @Bean
//    SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter, Queue queue) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        container.setQueueNames(queue.getName());
//        container.setMessageListener(messageListenerAdapter);
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter messageListenerAdapter(FanucConsumer fanucConsumer, MessageConverter messageConverter) {
//        return new MessageListenerAdapter(fanucConsumer, messageConverter);
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
//    }
//}
