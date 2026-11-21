package com.vittig.playgrid.model;

import java.util.Set;
import java.util.UUID;

public record GameRoom(UUID id, String name, String hostId, Set<String> members) {
    public GameRoom {
        members = Set.copyOf(members);
    }
}
