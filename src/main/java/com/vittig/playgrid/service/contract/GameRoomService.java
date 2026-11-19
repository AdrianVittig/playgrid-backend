package com.vittig.playgrid.service.contract;

import com.vittig.playgrid.dto.response.GameRoomResponse;
import com.vittig.playgrid.model.GameRoom;

import java.util.List;

public interface GameRoomService {
    GameRoom createRoom(String hostId, String name);
    List<GameRoom> getRooms();
    void removeRoomByHost(String hostId);
}
