package com.example.msbilling.configs;

public interface RabbitTopologyProps {
    String BALANCE_BILLING_EXCHANGE = "app.balance.billing.exchange";
    String BALANCE_BILLING_QUEUE = "app.balance.billing.queue";
    String BALANCE_BILLING_ROUTING_KEY = "app.balance.billing.routingkey";
    String BALANCE_BILLING_DLX = "app.balance.billing.dlx";
    String BALANCE_BILLING_DLQ = "app.balance.billing.dlq";
}
