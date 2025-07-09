package com.saga.orderservice.service;

import com.saga.orderservice.event.PaymentRequestedEvent;
import com.saga.orderservice.model.Order;
import com.saga.orderservice.model.OrderStatus;
import com.saga.orderservice.producer.KafkaProducerService;
import com.saga.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.CREATED);
        Order saved = orderRepository.save(order);

        PaymentRequestedEvent event = PaymentRequestedEvent.builder()
                .orderId(saved.getId())
                .userId(saved.getUserId())
                .productCode(saved.getProductCode())
                .quantity(saved.getQuantity())
                .build();

        kafkaProducerService.sendPaymentRequestedEvent(event);
        return saved;
    }
}