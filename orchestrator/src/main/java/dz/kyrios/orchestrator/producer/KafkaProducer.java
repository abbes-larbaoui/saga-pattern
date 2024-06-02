package dz.kyrios.notification.producer;

import dz.kyrios.notification.event.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements Producer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendNotificationEvent(NotificationEvent notificationEvent) {
        kafkaTemplate.send("notification-events", notificationEvent);
    }
}
