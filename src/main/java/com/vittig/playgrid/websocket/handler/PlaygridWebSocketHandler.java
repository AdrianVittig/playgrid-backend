package com.vittig.playgrid.websocket.handler;

import com.vittig.playgrid.dto.request.CreateRoomRequest;
import com.vittig.playgrid.dto.websocket.WebSocketMessage;
import com.vittig.playgrid.enums.MessageType;
import com.vittig.playgrid.model.GameRoom;
import com.vittig.playgrid.service.contract.GameRoomService;
import com.vittig.playgrid.websocket.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.type.TypeReference;
import tools.jackson.core.JacksonException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PlaygridWebSocketHandler extends TextWebSocketHandler {
    private final SessionRegistry sessionRegistry;
    private final JsonMapper jsonMapper;
    private final GameRoomService gameRoomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessionRegistry.register(session);
        WebSocketMessage<Void> message = new WebSocketMessage<Void>(MessageType.CONNECTED, null);
        sendMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws IOException{
        GameRoom gameRoom;
        try{
            WebSocketMessage<CreateRoomRequest> incomingMessage =
                    jsonMapper.readValue(
                            message.getPayload(),
                            new TypeReference<WebSocketMessage<CreateRoomRequest>>(){}
                    );

            if (incomingMessage == null) {
                throw new IllegalArgumentException("Invalid message");
            }

            if (incomingMessage.type() != MessageType.CREATE_ROOM) {
                throw new IllegalArgumentException("Unsupported message type");
            }

            if (incomingMessage.payload() == null) {
                throw new IllegalArgumentException("Missing payload");
            }

            String roomName = incomingMessage.payload().name();

            gameRoom = gameRoomService.createRoom(session.getId(), roomName);
        }catch (JacksonException e) {
            sendMessage(session,
                    new WebSocketMessage<>(MessageType.ERROR, "Invalid JSON message"));
            return;
        } catch (IllegalArgumentException | IllegalStateException e) {
            sendMessage(session,
                    new WebSocketMessage<>(MessageType.ERROR, e.getMessage()));
            return;
        }

        WebSocketMessage<GameRoom> response =
                new WebSocketMessage<>(MessageType.ROOM_CREATED, gameRoom);

        sendMessage(session, response);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws IOException {
        cleanUpSession(session.getId());
    }

    @Override
    public void handleTransportError(
            WebSocketSession session,
            Throwable exception
    ) throws IOException{
        cleanUpSession(session.getId());
        if(session.isOpen()){
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    private void sendMessage(WebSocketSession session,
                             WebSocketMessage<?> message) throws IOException{
        WebSocketSession decorated = sessionRegistry.get(session.getId());
        if(decorated == null || !decorated.isOpen()){
            return;
        }

        decorated.sendMessage(new TextMessage(jsonMapper.writeValueAsString(message)));
    }

    private void cleanUpSession(String sessionId){
        sessionRegistry.remove(sessionId);
        gameRoomService.removeRoomByHost(sessionId);
    }
}
