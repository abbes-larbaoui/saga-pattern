package dz.kyrios.stock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockService {

    public void reserveStock(String orderId) {
        log.info("stock reservation for the order ID: {}", orderId);
    }

    public void rollbackStockReservation(String orderId) {
        log.info("Rolling back stock for order ID: {}", orderId);
    }
}
