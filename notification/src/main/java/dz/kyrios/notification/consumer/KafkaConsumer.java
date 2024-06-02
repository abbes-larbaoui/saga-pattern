package dz.kyrios.stock.consumer;

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

    public KafkaConsumer(StockService stockService, Producer producer) {
        this.stockService = stockService;
        this.producer = producer;
    }

    @KafkaListener(topics = "payment-events", groupId = "stock-group")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
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
    public void handleStockEvent(StockEvent stockEvent) {
        if ("ROLLBACK_STOCK".equals(stockEvent.getStatus())) {
            // Perform stock rollback logic
            stockService.rollbackStockReservation(stockEvent.getOrderId());
        }
    }
}
