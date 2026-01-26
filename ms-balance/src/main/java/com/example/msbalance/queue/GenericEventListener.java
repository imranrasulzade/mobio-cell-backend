package com.example.msbalance.queue;

import com.example.msbalance.configs.RabbitTopologyProps;
import com.example.msbalance.model.BaseEvent;
import com.example.msbalance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericEventListener {

    private final BalanceService balanceService;

    @RabbitListener(queues = RabbitTopologyProps.NUMBER_BALANCE_QUEUE)
    public void onMessage(BaseEvent<?> event, @Headers Map<String, Object> headers) {
        log.info("Got event type={}, id={}, headers={}",
                event.getType(), event.getId(), headers);

        // routing by type:
        switch (event.getType()) {
            case "init_new.number" -> handleInitialBalance(event);
            default -> log.warn("Unknown event type: {}", event.getType());
        }
    }

    private void handleInitialBalance(BaseEvent<?> event) {
        log.info("handleInitialBalance event: {}", event);
        Integer numberId = (Integer) event.getPayload();
        balanceService.addInitialBalance(numberId);
    }

}
