package com.example.msbalance.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

  // JSON serializer/deserializer
  @Bean
  public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  // Producer template
  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter converter) {
    RabbitTemplate template = new RabbitTemplate(cf);
    template.setMessageConverter(converter);

    // publish confirm/return istesem:
    template.setMandatory(true);
    // template.setConfirmCallback((correlationData, ack, cause) -> {});
    // template.setReturnsCallback(returned -> {});

    return template;
  }

  // Listener factory (consumer)
  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory cf, MessageConverter converter
  ) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(cf);
    factory.setMessageConverter(converter);

    // sadə & stabil başlanğıc:
    factory.setDefaultRequeueRejected(false); // exception olarsa requeue etməsin (DLQ işləsin)
    // factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

    return factory;
  }

  // ---- Topology (Exchange/Queue/DLQ) ----

  @Bean
  public DirectExchange appExchange() {
    return new DirectExchange(RabbitTopologyProps.NUMBER_BALANCE_EXCHANGE, true, false);
  }

  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange(RabbitTopologyProps.NUMBER_BALANCE_DLX, true, false);
  }

  @Bean
  public Queue appQueue() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-dead-letter-exchange", RabbitTopologyProps.NUMBER_BALANCE_DLX);
    args.put("x-dead-letter-routing-key", RabbitTopologyProps.NUMBER_BALANCE_DLQ);
    return new Queue(RabbitTopologyProps.NUMBER_BALANCE_QUEUE, true, false, false, args);
  }

  @Bean
  public Queue deadLetterQueue() {
    return new Queue(RabbitTopologyProps.NUMBER_BALANCE_DLQ, true);
  }

  @Bean
  public Binding appBinding(Queue appQueue, DirectExchange appExchange) {
    return BindingBuilder.bind(appQueue).to(appExchange).with(RabbitTopologyProps.NUMBER_BALANCE_ROUTING_KEY);
  }

  @Bean
  public Binding dlqBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
    return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(RabbitTopologyProps.NUMBER_BALANCE_DLQ);
  }
}
