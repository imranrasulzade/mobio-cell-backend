package com.example.msnotification.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, MessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public DirectExchange balanceNotificationExchange() {
        return new DirectExchange(RabbitTopologyProps.BALANCE_NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange balanceNotificationDlx() {
        return new DirectExchange(RabbitTopologyProps.BALANCE_NOTIFICATION_DLX, true, false);
    }

    @Bean
    public Queue balanceNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RabbitTopologyProps.BALANCE_NOTIFICATION_DLX);
        args.put("x-dead-letter-routing-key", RabbitTopologyProps.BALANCE_NOTIFICATION_DLQ);
        return new Queue(RabbitTopologyProps.BALANCE_NOTIFICATION_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue balanceNotificationDlq() {
        return new Queue(RabbitTopologyProps.BALANCE_NOTIFICATION_DLQ, true);
    }

    @Bean
    public Binding balanceNotificationBinding(Queue balanceNotificationQueue, DirectExchange balanceNotificationExchange) {
        return BindingBuilder.bind(balanceNotificationQueue).to(balanceNotificationExchange).with(RabbitTopologyProps.BALANCE_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding balanceNotificationDlqBinding(Queue balanceNotificationDlq, DirectExchange balanceNotificationDlx) {
        return BindingBuilder.bind(balanceNotificationDlq).to(balanceNotificationDlx).with(RabbitTopologyProps.BALANCE_NOTIFICATION_DLQ);
    }
}
