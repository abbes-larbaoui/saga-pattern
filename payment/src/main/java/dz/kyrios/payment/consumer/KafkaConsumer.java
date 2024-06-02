package dz.kyrios.order.consumer;

import dz.kyrios.order.event.OrderEvent;
import dz.kyrios.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final OrderService orderService;

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void handleOrderEvent(OrderEvent orderEvent) {
        if ("ROLLBACK_ORDER".equals(orderEvent.getStatus())) {
            orderService.rollbackOrderCreation(orderEvent.getOrderId());
        }
    }
}
