package dz.kyrios.payment.producer;

import dz.kyrios.payment.event.PaymentEvent;

public interface Producer {
    void sendPaymentEvent(PaymentEvent paymentEvent);
}
