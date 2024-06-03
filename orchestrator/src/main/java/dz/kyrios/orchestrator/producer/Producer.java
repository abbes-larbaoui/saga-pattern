package dz.kyrios.orchestrator.producer;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Producer {
    void sendEvent(String topic, Object event);
}
