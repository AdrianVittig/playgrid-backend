package com.vittig.playgrid.dto.websocket;

import com.vittig.playgrid.enums.MessageType;

public record WebSocketMessage<T>(MessageType type, T payload) {
}
