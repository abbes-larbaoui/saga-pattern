package dz.kyrios.stock.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz.kyrios.stock.event.PaymentEvent;
import dz.kyrios.stock.event.StockEvent;
import dz.kyrios.stock.producer.Producer;
import dz.kyrios.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final StockService stockService;

    private final Producer producer;

    private final ObjectMapper mapper;

    public KafkaConsumer(StockService stockService, Producer producer, ObjectMapper mapper) {
        this.stockService = stockService;
        this.producer = producer;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "payment-events", groupId = "stock-group")
    public void handlePaymentEvent(String event) {
        PaymentEvent paymentEvent = null;
        try {
            paymentEvent = mapper.readValue(event, PaymentEvent.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        if ("PAYMENT_COMPLETED".equals(paymentEvent.getStatus())) {
            // Process payment
            StockEvent stockEvent;
            try {
                stockService.reserveStock(paymentEvent.getOrderId());
                stockEvent = new StockEvent(paymentEvent.getOrderId(), "STOCK_RESERVED");
            } catch (Exception e) {
                stockEvent = new StockEvent(paymentEvent.getOrderId(), "STOCK_RESERVATION_FAILED");
            }

            producer.sendStockEvent(stockEvent);
        }
    }

    @KafkaListener(topics = "stock-events", groupId = "stock-group")
    public void handleStockEvent(String event) {
        StockEvent stockEvent = null;
        try {
            stockEvent = mapper.readValue(event, StockEvent.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        if ("ROLLBACK_STOCK".equals(stockEvent.getStatus())) {
            // Perform stock rollback logic
            stockService.rollbackStockReservation(stockEvent.getOrderId());
        }
    }
}
