package com.example.msbilling.configs;

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
    public DirectExchange balanceBillingExchange() {
        return new DirectExchange(RabbitTopologyProps.BALANCE_BILLING_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange balanceBillingDlx() {
        return new DirectExchange(RabbitTopologyProps.BALANCE_BILLING_DLX, true, false);
    }

    @Bean
    public Queue balanceBillingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RabbitTopologyProps.BALANCE_BILLING_DLX);
        args.put("x-dead-letter-routing-key", RabbitTopologyProps.BALANCE_BILLING_DLQ);
        return new Queue(RabbitTopologyProps.BALANCE_BILLING_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue balanceBillingDlq() {
        return new Queue(RabbitTopologyProps.BALANCE_BILLING_DLQ, true);
    }

    @Bean
    public Binding balanceBillingBinding(Queue balanceBillingQueue, DirectExchange balanceBillingExchange) {
        return BindingBuilder.bind(balanceBillingQueue).to(balanceBillingExchange).with(RabbitTopologyProps.BALANCE_BILLING_ROUTING_KEY);
    }

    @Bean
    public Binding balanceBillingDlqBinding(Queue balanceBillingDlq, DirectExchange balanceBillingDlx) {
        return BindingBuilder.bind(balanceBillingDlq).to(balanceBillingDlx).with(RabbitTopologyProps.BALANCE_BILLING_DLQ);
    }
}
