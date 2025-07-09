package com.saga.orderservice.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestedEvent {
    private Long orderId;
    private Long userId;
    private String productCode;
    private Integer quantity;
}