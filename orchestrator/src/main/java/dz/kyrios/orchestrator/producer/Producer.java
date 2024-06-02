package dz.kyrios.notification.producer;

import dz.kyrios.notification.event.NotificationEvent;

public interface Producer {
    void sendNotificationEvent(NotificationEvent notificationEvent);
}
