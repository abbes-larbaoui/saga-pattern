package dz.kyrios.notification.service;

import dz.kyrios.notification.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    public void sendNotification(NotificationEvent notificationEvent) {
        log.info("Notification: order placed successfully");
    }
}
