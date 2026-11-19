package com.vittig.playgrid.websocket.communication.processor;

import com.vittig.playgrid.dto.request.CreateRoomRequest;
import com.vittig.playgrid.dto.response.GameRoomResponse;
import com.vittig.playgrid.dto.websocket.WebSocketMessage;
import com.vittig.playgrid.enums.MessageType;
import com.vittig.playgrid.model.GameRoom;
import com.vittig.playgrid.service.contract.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class CreateRoomMessageProcessor implements MessageProcessor {
    private final JsonMapper jsonMapper;
    private final GameRoomService gameRoomService;

    @Override
    public WebSocketMessage<?> process(String sessionId, JsonNode payload) {
        if (payload == null || !payload.isObject()) {
            throw new IllegalArgumentException("Payload must be a JSON object");
        }

        CreateRoomRequest request =
                jsonMapper.convertValue(payload,
                        CreateRoomRequest.class);

        GameRoom gameRoom =
                gameRoomService.createRoom(sessionId, request.name());

        GameRoomResponse gameRoomResponse =
                new GameRoomResponse(gameRoom.id(), gameRoom.name());

        return new WebSocketMessage<>(MessageType.ROOM_CREATED, gameRoomResponse);
    }

    @Override
    public MessageType messageType() {
        return MessageType.CREATE_ROOM;
    }
}
