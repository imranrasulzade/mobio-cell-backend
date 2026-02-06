package com.example.mspackage.queue;

import com.example.mspackage.configs.RabbitTopologyProps;
import com.example.mspackage.model.BaseEvent;
import com.example.mspackage.service.NumbersPackageService;
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

    private final NumbersPackageService numbersPackageService;

    @RabbitListener(queues = RabbitTopologyProps.NUMBER_PACKAGE_QUEUE)
    public void onMessage(BaseEvent<?> event, @Headers Map<String, Object> headers) {
        log.info("Got event type={}, id={}, headers={}",
                event.getType(), event.getId(), headers);

        // routing by type:
        switch (event.getType()) {
            case "default.package" -> handleDefaultPackage(event);
            default -> log.warn("Unknown event type: {}", event.getType());
        }
    }

    private void handleDefaultPackage(BaseEvent<?> event) {
        log.info("handleDefaultPackage event: {}", event);
        Integer numberId = (Integer) event.getPayload();
        numbersPackageService.addDefaultPackageForNumber(numberId);
    }

}
