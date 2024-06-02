package dz.kyrios.notification.consumer;

import dz.kyrios.notification.event.NotificationEvent;
import dz.kyrios.notification.event.StockEvent;
import dz.kyrios.notification.producer.Producer;
import dz.kyrios.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final NotificationService notificationService;

    private final Producer producer;

    public KafkaConsumer(NotificationService notificationService, Producer producer) {
        this.notificationService = notificationService;
        this.producer = producer;
    }

    @KafkaListener(topics = "stock-events", groupId = "notification-group")
    public void handlePaymentEvent(StockEvent stockEvent) {
        if ("STOCK_RESERVED".equals(stockEvent.getStatus())) {
            NotificationEvent notificationEvent = new NotificationEvent(stockEvent.getOrderId(), "NOTIFICATION_SENT");
            producer.sendNotificationEvent(notificationEvent);
        }
    }


}
