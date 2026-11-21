package com.vittig.playgrid.service.contract;

import com.vittig.playgrid.model.GameRoom;

import java.util.List;
import java.util.UUID;

public interface GameRoomService {
    GameRoom createRoom(String hostId, String name);
    List<GameRoom> getRooms();
    void removeRoomByHost(String hostId);
    GameRoom joinRoom(String sessionId, UUID roomId);
}
