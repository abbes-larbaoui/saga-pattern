package dz.kyrios.orchestrator.service;

import dz.kyrios.orchestrator.event.NotificationEvent;
import dz.kyrios.orchestrator.event.OrderEvent;
import dz.kyrios.orchestrator.event.PaymentEvent;
import dz.kyrios.orchestrator.event.StockEvent;
import dz.kyrios.orchestrator.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrchestratorService {

    private final Producer producer;

    public OrchestratorService(Producer producer) {
        this.producer = producer;
    }

    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        if ("PAYMENT_FAILED".equals(paymentEvent.getStatus())) {
            // Handle payment failure by triggering a rollback
            triggerRollback(paymentEvent.getOrderId());
        }
    }

    public void handleStockEvent(StockEvent stockEvent) {
        if ("STOCK_RESERVED".equals(stockEvent.getStatus())) {
            // Process stock reservation
            NotificationEvent notificationEvent = new NotificationEvent(stockEvent.getOrderId(), "ORDER_COMPLETED");
            producer.sendEvent("notification-events", notificationEvent);
        } else if ("STOCK_RESERVATION_FAILED".equals(stockEvent.getStatus())) {
            // Handle stock reservation failure by triggering a rollback
            triggerRollback(stockEvent.getOrderId());
        }
    }

    private void triggerRollback(String orderId) {
        OrderEvent rollbackOrderEvent = new OrderEvent(orderId, "ROLLBACK_ORDER");
        producer.sendEvent("order-events", rollbackOrderEvent);

        PaymentEvent rollbackPaymentEvent = new PaymentEvent(orderId, "ROLLBACK_PAYMENT");
        producer.sendEvent("payment-events", rollbackPaymentEvent);

        StockEvent rollbackStockEvent = new StockEvent(orderId, "ROLLBACK_STOCK");
        producer.sendEvent("stock-events", rollbackStockEvent);
    }
}
