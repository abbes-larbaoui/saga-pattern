package dz.kyrios.notification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz.kyrios.notification.event.NotificationEvent;
import dz.kyrios.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final NotificationService notificationService;

    private final ObjectMapper mapper;

    public KafkaConsumer(NotificationService notificationService, ObjectMapper mapper) {
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void handleNotificationEvent(String event) throws JsonProcessingException {
        NotificationEvent notificationEvent = mapper.readValue(event, NotificationEvent.class);
        notificationService.sendNotification(notificationEvent);
    }

}
