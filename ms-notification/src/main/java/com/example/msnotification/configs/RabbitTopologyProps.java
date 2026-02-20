package com.example.msnotification.configs;

public interface RabbitTopologyProps {
    String BALANCE_NOTIFICATION_EXCHANGE = "app.balance.notification.exchange";
    String BALANCE_NOTIFICATION_QUEUE = "app.balance.notification.queue";
    String BALANCE_NOTIFICATION_ROUTING_KEY = "app.balance.notification.routingkey";
    String BALANCE_NOTIFICATION_DLX = "app.balance.notification.dlx";
    String BALANCE_NOTIFICATION_DLQ = "app.balance.notification.dlq";
}
