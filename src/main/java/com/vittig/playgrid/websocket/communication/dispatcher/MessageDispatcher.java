package com.vittig.playgrid.websocket.communication.dispatcher;

import com.vittig.playgrid.dto.websocket.WebSocketMessage;
import com.vittig.playgrid.enums.MessageType;
import tools.jackson.databind.JsonNode;

public interface MessageDispatcher {
    WebSocketMessage<?> dispatch(String sessionId, MessageType type, JsonNode payload);
}
