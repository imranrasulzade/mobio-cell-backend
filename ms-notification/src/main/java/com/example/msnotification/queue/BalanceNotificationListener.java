package com.example.msnotification.queue;

import com.example.msnotification.configs.RabbitTopologyProps;
import com.example.msnotification.entity.Notification;
import com.example.msnotification.model.BalanceChangedPayload;
import com.example.msnotification.model.BaseEvent;
import com.example.msnotification.repositories.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceNotificationListener {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitTopologyProps.BALANCE_NOTIFICATION_QUEUE)
    public void onBalanceChanged(BaseEvent<?> event) {
        if (event == null || event.getPayload() == null) {
            log.warn("Received empty balance notification event");
            return;
        }
        if (!"balance.changed".equals(event.getType())) {
            log.warn("Unsupported notification event type={}", event.getType());
            return;
        }
        BalanceChangedPayload payload = objectMapper.convertValue(event.getPayload(), BalanceChangedPayload.class);
        String message = buildMessage(payload);
        Notification notification = Notification.builder()
                .numberId(payload.getNumberId())
                .type(payload.getOperationType())
                .message(message)
                .build();
        notificationRepository.save(notification);
        log.info("Notification saved for numberId={} message={}", payload.getNumberId(), message);
    }

    private String buildMessage(BalanceChangedPayload payload) {
        return "Operation=" + payload.getOperationType() +
                ", oldAmount=" + payload.getOldAmount() +
                ", change=" + payload.getChangedAmount() +
                ", newAmount=" + payload.getNewAmount();
    }
}
