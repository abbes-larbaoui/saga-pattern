package dz.kyrios.order.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz.kyrios.order.event.OrderEvent;
import dz.kyrios.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final OrderService orderService;

    private final ObjectMapper mapper;

    public KafkaConsumer(OrderService orderService, ObjectMapper mapper) {
        this.orderService = orderService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void handleOrderEvent(String event) {
        OrderEvent orderEvent = null;
        try {
            orderEvent = mapper.readValue(event, OrderEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if ("ROLLBACK_ORDER".equals(orderEvent.getStatus())) {
            orderService.rollbackOrderCreation(orderEvent.getOrderId());
        }
    }
}
