package dz.kyrios.payment.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import dz.kyrios.payment.event.PaymentEvent;

public interface Producer {
    void sendPaymentEvent(PaymentEvent paymentEvent);
}
