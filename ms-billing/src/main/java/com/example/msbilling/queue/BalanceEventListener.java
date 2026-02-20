package com.example.msbilling.queue;

import com.example.msbilling.configs.RabbitTopologyProps;
import com.example.msbilling.model.BalanceChangedPayload;
import com.example.msbilling.model.BaseEvent;
import com.example.msbilling.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceEventListener {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitTopologyProps.BALANCE_BILLING_QUEUE)
    public void onBalanceChanged(BaseEvent<?> event) {
        if (event == null || event.getPayload() == null) {
            log.warn("Received empty balance event");
            return;
        }
        if (!"balance.changed".equals(event.getType())) {
            log.warn("Unsupported event type={}", event.getType());
            return;
        }
        BalanceChangedPayload payload = objectMapper.convertValue(event.getPayload(), BalanceChangedPayload.class);
        transactionService.createFromBalanceEvent(payload);
        log.info("Transaction saved for numberId={}, operation={}",
                payload.getNumberId(), payload.getOperationType());
    }
}
