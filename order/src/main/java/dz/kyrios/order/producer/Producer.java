package dz.kyrios.order.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import dz.kyrios.order.event.OrderEvent;

public interface Producer {
    void sendOrderEvent(Object orderEvent);
}
