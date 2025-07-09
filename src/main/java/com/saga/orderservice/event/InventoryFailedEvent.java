package com.saga.orderservice.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryFailedEvent {
    private Long orderId;
    private String productCode;
    private String reason;
}