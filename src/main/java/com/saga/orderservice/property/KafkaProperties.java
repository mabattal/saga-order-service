package com.saga.orderservice.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String address;
    private String groupId;
    private Topic topic;

    @Getter
    @Setter
    public static class Topic {
        private String paymentRequested;
        private String paymentCompleted;
        private String paymentFailed;
        private String orderCreated;
        private String inventoryReserved;
        private String inventoryFailed;
    }
}
