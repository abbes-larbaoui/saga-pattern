package dz.kyrios.payment.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz.kyrios.payment.event.OrderEvent;
import dz.kyrios.payment.event.PaymentEvent;
import dz.kyrios.payment.producer.Producer;
import dz.kyrios.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final PaymentService paymentService;

    private final Producer producer;

    private final ObjectMapper mapper;

    public KafkaConsumer(PaymentService paymentService, Producer producer, ObjectMapper mapper) {
        this.paymentService = paymentService;
        this.producer = producer;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void handleOrderEvent(String event) {
        OrderEvent orderEvent = null;
        try {
            orderEvent = mapper.readValue(event, OrderEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if ("ORDER_CREATED".equals(orderEvent.getStatus())) {
            // Process payment
            PaymentEvent paymentEvent;
            try {
                paymentService.processPayment(orderEvent.getOrderId());
                paymentEvent = new PaymentEvent(orderEvent.getOrderId(), "PAYMENT_COMPLETED");
            } catch (Exception e) {
                paymentEvent = new PaymentEvent(orderEvent.getOrderId(), "PAYMENT_FAILED");
            }
            producer.sendPaymentEvent(paymentEvent);
        }
    }

    @KafkaListener(topics = "payment-events", groupId = "payment-group")
    public void handlePaymentEvent(String event) {
        PaymentEvent paymentEvent = null;
        try {
            paymentEvent = mapper.readValue(event, PaymentEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if ("ROLLBACK_PAYMENT".equals(paymentEvent.getStatus())) {
            // Perform payment rollback logic
            paymentService.rollbackPayment(paymentEvent.getOrderId());
        }
    }
}
