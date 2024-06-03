package dz.kyrios.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    public void processPayment(String orderId) {
        log.info("Processing payment for order ID: {}", orderId);
    }

    public void rollbackPayment(String orderId) {
        log.info("Rolling back payment for order ID: {}", orderId);
    }
}
