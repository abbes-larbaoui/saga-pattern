package dz.kyrios.payment.producer;

import dz.kyrios.payment.event.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements Producer{

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        kafkaTemplate.send("payment-events", paymentEvent);
    }
}
