package dz.kyrios.payment.consumer;

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

    public KafkaConsumer(PaymentService paymentService, Producer producer) {
        this.paymentService = paymentService;
        this.producer = producer;
    }

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void handleOrderEvent(OrderEvent orderEvent) {
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
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        if ("ROLLBACK_PAYMENT".equals(paymentEvent.getStatus())) {
            // Perform payment rollback logic
            paymentService.rollbackPayment(paymentEvent.getOrderId());
        }
    }
}
