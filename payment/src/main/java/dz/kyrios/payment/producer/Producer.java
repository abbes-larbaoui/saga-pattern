package dz.kyrios.order.producer;

import dz.kyrios.order.event.OrderEvent;

public interface Producer {
    void sendOrderEvent(OrderEvent orderEvent);
}
