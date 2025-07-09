package com.saga.orderservice.producer;

import com.saga.orderservice.event.OrderCreatedEvent;
import com.saga.orderservice.event.PaymentRequestedEvent;
import com.saga.orderservice.property.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void sendPaymentRequestedEvent(PaymentRequestedEvent event) {
        kafkaTemplate.send(kafkaProperties.getTopic().getPaymentRequested(), event);
        log.info("payment-requested event gönderildi: {}", event);
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaTemplate.send(kafkaProperties.getTopic().getOrderCreated(), event);
        log.info("order-created event gönderildi: {}", event);
    }
}