package com.example.msnumber.queue;

import com.example.msnumber.configs.RabbitTopologyProps;
import com.example.msnumber.model.BaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public <T> void publishToNumberBalance(BaseEvent<T> event) {
    rabbitTemplate.convertAndSend(
            RabbitTopologyProps.NUMBER_BALANCE_EXCHANGE,
            RabbitTopologyProps.NUMBER_BALANCE_ROUTING_KEY,
        event,
        message -> {
          message.getMessageProperties().setHeader("eventType", event.getType());
          message.getMessageProperties().setHeader("eventVersion", event.getVersion());
          return message;
        }
    );
  }

    public <T> void publishToNumberPackage(BaseEvent<T> event) {
        rabbitTemplate.convertAndSend(
                RabbitTopologyProps.NUMBER_PACKAGE_EXCHANGE,
                RabbitTopologyProps.NUMBER_PACKAGE_ROUTING_KEY,
                event,
                message -> {
                    message.getMessageProperties().setHeader("eventType", event.getType());
                    message.getMessageProperties().setHeader("eventVersion", event.getVersion());
                    return message;
                }
        );
    }

}
