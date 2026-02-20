package com.example.msnumber.queue;

import com.example.msnumber.entity.OutboxEvent;
import com.example.msnumber.model.BaseEvent;
import com.example.msnumber.repositories.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private static final String STATUS_NEW = "NEW";
    private static final String STATUS_SENT = "SENT";
    private static final String STATUS_FAILED = "FAILED";

    private final OutboxEventRepository outboxEventRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void enqueue(Long aggregateId, String eventType, Integer eventVersion, String payload) {
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setAggregateId(aggregateId);
        outboxEvent.setEventType(eventType);
        outboxEvent.setEventVersion(eventVersion);
        outboxEvent.setPayload(payload);
        outboxEvent.setStatus(STATUS_NEW);
        outboxEventRepository.save(outboxEvent);
    }

    @Scheduled(fixedDelayString = "${app.outbox.dispatch-delay-ms:3000}")
    @Transactional
    public void dispatchPending() {
        List<OutboxEvent> events = outboxEventRepository
                .findTop100ByStatusInOrderByCreatedAtAsc(List.of(STATUS_NEW, STATUS_FAILED));
        for (OutboxEvent event : events) {
            try {
                publish(event);
                event.setStatus(STATUS_SENT);
                event.setSentAt(LocalDateTime.now());
                event.setLastError(null);
            } catch (RuntimeException ex) {
                event.setStatus(STATUS_FAILED);
                event.setLastError(ex.getMessage());
                log.error("Outbox dispatch failed for eventId={}", event.getId(), ex);
            }
            outboxEventRepository.save(event);
        }
    }

    private void publish(OutboxEvent event) {
        Integer payload = Integer.valueOf(event.getPayload());
        BaseEvent<Integer> baseEvent = BaseEvent.of(event.getEventType(), event.getEventVersion(), payload);
        if ("init_new.number".equals(event.getEventType())) {
            eventPublisher.publishToNumberBalance(baseEvent);
            return;
        }
        if ("default.package".equals(event.getEventType())) {
            eventPublisher.publishToNumberPackage(baseEvent);
            return;
        }
        throw new IllegalStateException("Unknown outbox event type: " + event.getEventType());
    }
}
