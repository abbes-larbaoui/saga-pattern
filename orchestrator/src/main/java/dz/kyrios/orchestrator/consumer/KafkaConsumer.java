package dz.kyrios.orchestrator.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz.kyrios.orchestrator.event.PaymentEvent;
import dz.kyrios.orchestrator.event.StockEvent;
import dz.kyrios.orchestrator.service.OrchestratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final OrchestratorService orchestratorService;

    private final ObjectMapper mapper;


    public KafkaConsumer(OrchestratorService orchestratorService, ObjectMapper mapper) {
        this.orchestratorService = orchestratorService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "payment-events", groupId = "orchestrator-group")
    public void handlePaymentEvent(String event) throws JsonProcessingException {
        PaymentEvent paymentEvent = mapper.readValue(event, PaymentEvent.class);
        orchestratorService.handlePaymentEvent(paymentEvent);
    }

    @KafkaListener(topics = "stock-events", groupId = "orchestrator-group")
    public void handleStockEvent(String event) throws JsonProcessingException {
        StockEvent stockEvent = mapper.readValue(event, StockEvent.class);
        orchestratorService.handleStockEvent(stockEvent);
    }
}
