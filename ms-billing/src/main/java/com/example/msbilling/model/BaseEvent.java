package com.example.msbilling.model;

import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent<T> {
    private UUID id;
    private String type;
    private int version;
    private Instant occurredAt;
    private Map<String, Object> meta;
    private T payload;
}
