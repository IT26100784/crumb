package com.crumb.crumb.model.order;

public enum OrderStatus {
    PENDING,        // customer placed the order
    CONFIRMED,      // bakery accepted the order
    IN_PROGRESS,    // cake is being baked / decorated
    READY,          // ready for pickup or delivery
    COMPLETED,      // customer received the cake
    CANCELLED       // order was canceled
}