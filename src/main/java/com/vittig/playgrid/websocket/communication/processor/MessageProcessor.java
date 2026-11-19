package com.vittig.playgrid.websocket.communication.processor;

import com.vittig.playgrid.dto.websocket.WebSocketMessage;
import com.vittig.playgrid.enums.MessageType;
import tools.jackson.databind.JsonNode;

public interface MessageProcessor {
    WebSocketMessage<?> process(String sessionId, JsonNode payload);

    MessageType messageType();
}
