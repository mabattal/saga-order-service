package com.saga.orderservice.consumer;

import com.saga.orderservice.event.*;
import com.saga.orderservice.model.OrderStatus;
import com.saga.orderservice.producer.KafkaProducerService;
import com.saga.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(
            topics = "${spring.kafka.topic.paymentCompleted}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "paymentCompletedFactory")
    public void handlePaymentCompleted(PaymentCompletedEvent event, Acknowledgment ack) {
        try {
            log.info("payment-completed alındı: {}", event);
            orderRepository.findById(event.getOrderId()).ifPresent(order -> {
                order.setStatus(OrderStatus.IN_PROGRESS);
                order.setStatus(OrderStatus.IN_PROGRESS);
                orderRepository.save(order);

                OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .productCode(order.getProductCode())
                        .quantity(order.getQuantity())
                        .build();

                kafkaProducerService.sendOrderCreatedEvent(orderCreatedEvent);
                ack.acknowledge();
            });
        } catch (Exception ex) {
            log.error("payment-completed event işlenirken hata oluştu: {}", ex.getMessage(), ex);
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.paymentFailed}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "paymentFailedFactory")
    public void handlePaymentFailed(PaymentFailedEvent event, Acknowledgment ack) {
        try {
            log.info("payment-failed alındı: {}", event);
            orderRepository.findById(event.getOrderId()).ifPresent(order -> {
                order.setStatus(OrderStatus.FAILED_PAYMENT);
                orderRepository.save(order);
            });

            ack.acknowledge();
        } catch (Exception ex) {
            log.error("payment-failed event işlenirken hata oluştu: {}", ex.getMessage(), ex);
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.inventoryReserved}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "inventoryReservedFactory")
    public void handleInventoryReserved(InventoryReservedEvent event, Acknowledgment ack) {
        try {
            log.info("inventory-reserved alındı: {}", event);
            orderRepository.findById(event.getOrderId()).ifPresent(order -> {
                order.setStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);
            });

            ack.acknowledge();
        } catch (Exception ex) {
            log.error("inventory-reserved event işlenirken hata oluştu: {}", ex.getMessage(), ex);
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.inventoryFailed}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "inventoryFailedFactory")
    public void handleInventoryFailed(InventoryFailedEvent event, Acknowledgment ack) {
        try {
            log.info("inventory-failed alındı: {}", event);
            orderRepository.findById(event.getOrderId()).ifPresent(order -> {
                order.setStatus(OrderStatus.FAILED_INVENTORY);
                orderRepository.save(order);
            });

            ack.acknowledge();
        } catch (Exception ex) {
            log.error("inventory-failed event işlenirken hata oluştu: {}", ex.getMessage(), ex);
        }
    }
}

