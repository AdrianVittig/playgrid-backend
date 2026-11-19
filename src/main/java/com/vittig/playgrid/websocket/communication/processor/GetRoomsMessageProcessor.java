package com.vittig.playgrid.websocket.communication.processor;

import com.vittig.playgrid.dto.response.GameRoomResponse;
import com.vittig.playgrid.dto.websocket.WebSocketMessage;
import com.vittig.playgrid.enums.MessageType;
import com.vittig.playgrid.model.GameRoom;
import com.vittig.playgrid.service.contract.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRoomsMessageProcessor implements MessageProcessor{
    private final GameRoomService gameRoomService;

    @Override
    public WebSocketMessage<?> process(String sessionId, JsonNode payload) {
        List<GameRoom> rooms;

        rooms = gameRoomService.getRooms();

        List<GameRoomResponse> roomResponses = new ArrayList<>();

        rooms.forEach(r -> {
            roomResponses.add(new GameRoomResponse(r.id(), r.name()));
        });

        return new WebSocketMessage<>(MessageType.ROOMS_LIST, roomResponses);
    }

    @Override
    public MessageType messageType() {
        return MessageType.GET_ROOMS;
    }
}
