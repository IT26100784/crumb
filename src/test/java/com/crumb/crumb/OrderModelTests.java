package com.crumb.crumb;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import com.crumb.crumb.dto.CreateOrderRequest;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderModelTests {
    private OrderModel order() {
        return new OrderModel("C001", LocalDate.now(), new BigDecimal("1500.00"), "");
    }

    @Test
    void followsTheFullOrderLifecycle() {
        OrderModel order = order();
        order.confirm();
        order.startBaking();
        assertFalse(order.isCancellable());
        order.markReady();
        order.complete();
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void cannotSkipStepsOrResumeCancelledOrders() {
        OrderModel order = order();
        assertThrows(IllegalStateException.class, order::complete);
        assertThrows(IllegalStateException.class, order::startBaking);
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertThrows(IllegalStateException.class, order::confirm);
    }

    @Test
    void confirmedOrdersCanBeCancelled() {
        OrderModel order = order();
        order.confirm();
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void pastDatesAreRejectedOnCreationButDoNotBlockExistingOrders() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var request = new CreateOrderRequest();
            request.setCustomerId("C001");
            request.setRequiredDate(LocalDate.now().minusDays(1));
            request.setTotalAmount(new BigDecimal("1500"));
            assertTrue(validator.validate(request).stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("requiredDate")));
            OrderModel existing = order();
            existing.setRequiredDate(LocalDate.now().minusDays(1));
            existing.confirm();
            assertTrue(validator.validate(existing).isEmpty());
        }
    }
}
