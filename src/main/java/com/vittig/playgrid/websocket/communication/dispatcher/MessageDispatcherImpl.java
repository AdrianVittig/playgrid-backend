package com.vittig.playgrid.websocket.communication.dispatcher;

import com.vittig.playgrid.dto.websocket.WebSocketMessage;
import com.vittig.playgrid.enums.MessageType;
import com.vittig.playgrid.websocket.communication.processor.MessageProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MessageDispatcherImpl implements MessageDispatcher {
    private final List<MessageProcessor> messageProcessors;


    @Override
    public WebSocketMessage<?> dispatch(String sessionId, MessageType type, JsonNode payload) {
        MessageProcessor processor = messageProcessors.stream()
                .filter(p -> p.messageType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported type!"));

        return processor.process(sessionId, payload);
    }

    @PostConstruct
    private void validate(){
        Set<MessageType> types = new HashSet<>();
        messageProcessors.forEach(mp -> {
            boolean added = types.add(mp.messageType());
            if(!added){
                throw new IllegalStateException(mp.messageType() + " already exists!");
            }
        });
    }
}
