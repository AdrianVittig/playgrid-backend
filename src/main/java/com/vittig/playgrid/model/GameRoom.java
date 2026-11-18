package com.vittig.playgrid.model;

import java.util.UUID;

public record GameRoom(UUID id, String name, String hostId) {
}
