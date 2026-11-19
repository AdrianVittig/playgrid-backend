package com.vittig.playgrid.service.impl;

import com.vittig.playgrid.dto.response.GameRoomResponse;
import com.vittig.playgrid.model.GameRoom;
import com.vittig.playgrid.service.contract.GameRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRoomServiceImpl implements GameRoomService {
    private final Map<String, GameRoom> rooms =
            new ConcurrentHashMap<>();
    @Override
    public GameRoom createRoom(String hostId, String name) {

        if(hostId == null || hostId.isBlank()){
            throw new IllegalArgumentException("Invalid host ID!");
        }

        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Invalid room name!");
        }

        UUID id = UUID.randomUUID();
        GameRoom gameRoom = new GameRoom(id, name, hostId);

        GameRoom existingRoom = rooms.putIfAbsent(hostId, gameRoom);

        if(existingRoom != null){
            throw new IllegalStateException("The host already owns a room!");
        }

        return gameRoom;
    }

    @Override
    public List<GameRoom> getRooms() {
        return List.copyOf(rooms.values());
    }

    @Override
    public void removeRoomByHost(String hostId) {
        rooms.remove(hostId);
    }
}
