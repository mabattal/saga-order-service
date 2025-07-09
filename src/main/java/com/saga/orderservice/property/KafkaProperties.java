package com.saga.orderservice.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private String address;
    private Topic topic;
    private Consumer consumer;
    private String groupId;
    private Listener listener;

    @Getter
    @Setter
    public static class Consumer {
        private boolean enableAutoCommit;
        private String autoOffsetReset;
        private Map<String, Object> properties = new HashMap<>();
    }

    @Getter
    @Setter
    public static class Listener {
        private String ackMode;
    }

    @Getter
    @Setter
    public static class Topic {
        private String orderCreated;
        private String paymentRequested;
        private String paymentFailed;
        private String paymentCompleted;
        private String inventoryReserved;
        private String inventoryFailed;
    }
}
