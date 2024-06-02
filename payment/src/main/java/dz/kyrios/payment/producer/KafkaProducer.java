package dz.kyrios.order.producer;

import dz.kyrios.order.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements Producer{

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendOrderEvent(OrderEvent orderEvent) {
        kafkaTemplate.send("order-events", orderEvent);
    }
}
