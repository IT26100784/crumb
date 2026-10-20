package com.crumb.crumb;

import com.crumb.crumb.dto.CreateOrderRequest;
import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import com.crumb.crumb.repository.order.OrderRepository;
import com.crumb.crumb.service.order.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTests {
    private final OrderRepository repository = mock(OrderRepository.class);
    private final OrderService service = new OrderService(repository);

    private OrderModel existing(OrderStatus status) {
        OrderModel order = new OrderModel("C001", LocalDate.now(), new BigDecimal("1500"), "Old note");
        order.setId(7L);
        order.setStatus(status);
        order.setOrderDate(LocalDateTime.of(2026, 1, 1, 10, 0));
        when(repository.findById(7L)).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(order);
        return order;
    }

    private CreateOrderRequest changes() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(" C002 ");
        request.setRequiredDate(LocalDate.now().plusDays(3));
        request.setTotalAmount(new BigDecimal("2500.00"));
        request.setSpecialNote("New note");
        return request;
    }

    @Test
    void editsDetailsWhilePreservingIdentityCreationDateAndStatus() {
        for (OrderStatus status : new OrderStatus[]{OrderStatus.PENDING, OrderStatus.CONFIRMED}) {
            OrderModel order = existing(status);
            LocalDateTime created = order.getOrderDate();
            CreateOrderRequest request = changes();
            OrderModel updated = service.updateOrder(7L, request);
            assertSame(order, updated);
            assertEquals(7L, updated.getId());
            assertEquals(created, updated.getOrderDate());
            assertEquals(status, updated.getStatus());
            assertEquals("C002", updated.getCustomerId());
            assertEquals(request.getRequiredDate(), updated.getRequiredDate());
            assertEquals(request.getTotalAmount(), updated.getTotalAmount());
            assertEquals("New note", updated.getSpecialNote());
        }
    }

    @Test
    void rejectsEditsAfterBakingStartsOrCancellation() {
        for (OrderStatus status : new OrderStatus[]{
                OrderStatus.IN_PROGRESS, OrderStatus.READY, OrderStatus.COMPLETED, OrderStatus.CANCELLED}) {
            OrderModel order = existing(status);
            assertThrows(IllegalStateException.class, () -> service.updateOrder(7L, changes()));
            assertEquals("C001", order.getCustomerId());
            verify(repository, never()).save(order);
        }
    }

    @Test
    void deletesPendingAndCancelledOrders() {
        for (OrderStatus status : new OrderStatus[]{OrderStatus.PENDING, OrderStatus.CANCELLED}) {
            OrderModel order = existing(status);
            service.deleteOrder(7L);
            verify(repository).delete(order);
        }
    }

    @Test
    void rejectsDeletionOfAcceptedAndCompletedOrders() {
        for (OrderStatus status : new OrderStatus[]{
                OrderStatus.CONFIRMED, OrderStatus.IN_PROGRESS, OrderStatus.READY, OrderStatus.COMPLETED}) {
            existing(status);
            assertThrows(IllegalStateException.class, () -> service.deleteOrder(7L));
        }
        verify(repository, never()).delete(any(OrderModel.class));
    }

    @Test
    void missingOrdersCannotBeUpdatedOrDeleted() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateOrder(7L, changes()));
        assertThrows(IllegalArgumentException.class, () -> service.deleteOrder(7L));
        verify(repository, never()).save(any(OrderModel.class));
        verify(repository, never()).delete(any(OrderModel.class));
    }
}
