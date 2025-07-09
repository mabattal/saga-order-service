package com.saga.orderservice.model;


public enum OrderStatus {
    CREATED,
    COMPLETED,
    CANCELLED,
    IN_PROGRESS,
    FAILED_PAYMENT,
    FAILED_INVENTORY
}