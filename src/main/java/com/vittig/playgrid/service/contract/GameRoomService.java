package com.vittig.playgrid.service.contract;

import com.vittig.playgrid.model.GameRoom;

public interface GameRoomService {
    GameRoom createRoom(String hostId, String name);
    void removeRoomByHost(String hostId);
}
