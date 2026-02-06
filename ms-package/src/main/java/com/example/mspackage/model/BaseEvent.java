package com.example.mspackage.model;

import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent<T> {

  /** unique message id */
  private UUID id;

  /** event type: "user.created", "order.paid" və s. */
  private String type;

  /** schema / contract version */
  private int version;

  /** event creates time */
  private Instant occurredAt;

  /** free-form metadata */
  private Map<String, Object> meta;

  /** actual payload */
  private T payload;

  public static <T> BaseEvent<T> of(String type, int version, T payload) {
    return BaseEvent.<T>builder()
        .id(UUID.randomUUID())
        .type(type)
        .version(version)
        .occurredAt(Instant.now())
        .payload(payload)
        .build();
  }
}
