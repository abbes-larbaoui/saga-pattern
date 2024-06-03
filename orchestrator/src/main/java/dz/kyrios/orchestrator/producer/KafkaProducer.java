package dz.kyrios.orchestrator.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer implements Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @Override
    public void sendEvent(String topic, Object notificationEvent){
        try {
            kafkaTemplate.send(topic, mapper.writeValueAsString(notificationEvent));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
