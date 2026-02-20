package com.example.mspackage.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.aopalliance.aop.Advice;

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
    factory.setAdviceChain(packageRetryAdvice());
    // factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

    return factory;
  }

  @Bean
  public Advice packageRetryAdvice() {
    return RetryInterceptorBuilder.stateless()
            .maxAttempts(5)
            .recoverer(new RejectAndDontRequeueRecoverer())
            .build();
  }

  // ---- Topology (Exchange/Queue/DLQ) ----

    @Bean
    public DirectExchange appNumberPackageExchange() {
        return new DirectExchange(RabbitTopologyProps.NUMBER_PACKAGE_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange deadLetterNumberPackageExchange() {
        return new DirectExchange(RabbitTopologyProps.NUMBER_PACKAGE_DLX, true, false);
    }

    @Bean
    public Queue appNumberPackageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RabbitTopologyProps.NUMBER_PACKAGE_DLX);
        args.put("x-dead-letter-routing-key", RabbitTopologyProps.NUMBER_PACKAGE_DLQ);
        return new Queue(RabbitTopologyProps.NUMBER_PACKAGE_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue deadLetterNumberPackageQueue() {
        return new Queue(RabbitTopologyProps.NUMBER_PACKAGE_DLQ, true);
    }

    @Bean
    public Binding appNumberPackageBinding(Queue appNumberPackageQueue, DirectExchange appNumberPackageExchange) {
        return BindingBuilder.bind(appNumberPackageQueue).to(appNumberPackageExchange).with(RabbitTopologyProps.NUMBER_PACKAGE_ROUTING_KEY);
    }

    @Bean
    public Binding dlqNumberPackageBinding(Queue deadLetterNumberPackageQueue, DirectExchange deadLetterNumberPackageExchange) {
        return BindingBuilder.bind(deadLetterNumberPackageQueue).to(deadLetterNumberPackageExchange).with(RabbitTopologyProps.NUMBER_PACKAGE_DLQ);
    }
}
