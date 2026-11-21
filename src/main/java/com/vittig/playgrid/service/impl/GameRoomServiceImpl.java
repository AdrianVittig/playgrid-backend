package com.vittig.playgrid.service.impl;

import com.vittig.playgrid.exception.HostAlreadyOwnsARoomException;
import com.vittig.playgrid.exception.InvalidRoomNameException;
import com.vittig.playgrid.model.GameRoom;
import com.vittig.playgrid.service.contract.GameRoomService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRoomServiceImpl implements GameRoomService {
    private final Map<String, GameRoom> rooms =
            new ConcurrentHashMap<>();
    @Override
    public synchronized GameRoom createRoom(String hostId, String name) {

        if(hostId == null || hostId.isBlank()){
            throw new IllegalArgumentException("Invalid host ID!");
        }

        if(name == null || name.isBlank()){
            throw new InvalidRoomNameException("Invalid room name!");
        }

        if(rooms.containsKey(hostId)){
            throw new HostAlreadyOwnsARoomException("The host already owns a room!");
        }

        boolean isSessionInRoom = getRooms().stream().anyMatch(gr ->
                gr.members().contains(hostId));

        if(isSessionInRoom){
            throw new IllegalArgumentException("You already belong to a room!");
        }

        UUID id = UUID.randomUUID();
        GameRoom gameRoom = new GameRoom(id, name, hostId, Set.of(hostId));

        rooms.putIfAbsent(hostId, gameRoom);

        return gameRoom;
    }

    @Override
    public List<GameRoom> getRooms() {
        return List.copyOf(rooms.values());
    }

    @Override
    public synchronized void removeRoomByHost(String hostId) {
        rooms.remove(hostId);
    }

    @Override
    public synchronized GameRoom joinRoom(String sessionId, UUID roomId) {
        if(sessionId == null || sessionId.isBlank()){
            throw new IllegalArgumentException("Session not valid!");
        }

        if(roomId == null){
            throw new IllegalArgumentException("Room not valid!");
        }

        GameRoom gameRoom = getRooms().stream().filter(r -> r.id().equals(roomId))
                .findAny()
                .orElseThrow(
                () -> new IllegalArgumentException("No room!")
        );

        boolean isSessionInRoom = getRooms().stream().anyMatch(gr ->
            gr.members().contains(sessionId));

        if(isSessionInRoom){
            throw new IllegalArgumentException("You already belong to a room!");
        }

        if(gameRoom.members().size() >= 2){
            throw new IllegalArgumentException("Game Room contains 2 members!");
        }

        Set<String> newMembers = new HashSet<>(gameRoom.members());
        newMembers.add(sessionId);
        gameRoom = new GameRoom(
                roomId,
                gameRoom.name(),
                gameRoom.hostId(),
                newMembers
        );

        rooms.put(gameRoom.hostId(), gameRoom);

        return gameRoom;
    }
}
