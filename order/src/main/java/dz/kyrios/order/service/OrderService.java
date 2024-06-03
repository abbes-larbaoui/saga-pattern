package dz.kyrios.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dz.kyrios.order.dto.OrderDto;
import dz.kyrios.order.event.OrderEvent;
import dz.kyrios.order.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private final Producer producer;

    public OrderService(Producer producer) {
        this.producer = producer;
    }

    public OrderEvent createOrder(OrderDto order) {
        OrderEvent orderEvent = new OrderEvent(order.getId(), "ORDER_CREATED");
        log.info("Order created with ID: " + orderEvent.getOrderId());
        producer.sendOrderEvent(orderEvent);
        return orderEvent;
    }

    public void rollbackOrderCreation(String orderId) {
        log.info("Rolling back order with ID: " + orderId);
    }
}
