package com.example.msbalance.queue;

import com.example.msbalance.configs.RabbitTopologyProps;
import com.example.msbalance.model.BaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public <T> void publishToBilling(BaseEvent<T> event) {
        rabbitTemplate.convertAndSend(
                RabbitTopologyProps.BALANCE_BILLING_EXCHANGE,
                RabbitTopologyProps.BALANCE_BILLING_ROUTING_KEY,
                event
        );
    }

    public <T> void publishToNotification(BaseEvent<T> event) {
        rabbitTemplate.convertAndSend(
                RabbitTopologyProps.BALANCE_NOTIFICATION_EXCHANGE,
                RabbitTopologyProps.BALANCE_NOTIFICATION_ROUTING_KEY,
                event
        );
    }
}
