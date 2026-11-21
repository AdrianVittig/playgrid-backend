package com.vittig.playgrid.websocket.communication.processor;

import com.vittig.playgrid.dto.request.JoinRoomRequest;
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
public class JoinRoomMessageProcessor implements MessageProcessor{
    private final JsonMapper jsonMapper;
    private final GameRoomService gameRoomService;

    @Override
    public WebSocketMessage<?> process(String sessionId, JsonNode payload) {
        if(payload == null || !payload.isObject()){
            throw new IllegalArgumentException("Invalid payload!");
        }

        JoinRoomRequest joinRequest =
                jsonMapper.convertValue(
                        payload, JoinRoomRequest.class
                );

        GameRoom gameRoom =  gameRoomService.joinRoom(sessionId, joinRequest.roomId());

        GameRoomResponse gameRoomResponse = new GameRoomResponse(gameRoom.id(), gameRoom.name());

        return new WebSocketMessage<>(MessageType.ROOM_JOINED, gameRoomResponse);
    }

    @Override
    public MessageType messageType() {
        return MessageType.JOIN_ROOM;
    }
}
