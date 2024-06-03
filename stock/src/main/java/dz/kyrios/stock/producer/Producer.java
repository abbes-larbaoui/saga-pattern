package dz.kyrios.stock.producer;

import dz.kyrios.stock.event.StockEvent;

public interface Producer {
    void sendStockEvent(StockEvent stockEvent);
}
