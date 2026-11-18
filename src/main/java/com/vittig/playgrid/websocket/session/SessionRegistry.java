package com.vittig.playgrid.websocket.session;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {
    private final Map<String, WebSocketSession> sessions =
            new ConcurrentHashMap<>();

    public void register(WebSocketSession session){
        WebSocketSession decoratedSession =
                new ConcurrentWebSocketSessionDecorator(
                        session,
                        10000,
                        64 * 1024
                );

        sessions.putIfAbsent(session.getId(), decoratedSession);
    }

    public WebSocketSession get(String sessionId){
        return sessions.get(sessionId);
    }

    public void remove(String sessionId){
        sessions.remove(sessionId);
    }


}
