package dz.kyrios.order.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer implements Producer{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper mapper;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @Override
    public void sendOrderEvent(Object orderEvent) {
        try {
            kafkaTemplate.send("order-events", mapper.writeValueAsString(orderEvent));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
