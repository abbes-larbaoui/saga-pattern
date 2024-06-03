package dz.kyrios.payment.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz.kyrios.payment.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer implements Producer{

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @Override
    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        try {
            kafkaTemplate.send("payment-events", mapper.writeValueAsString(paymentEvent));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
