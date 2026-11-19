package com.vittig.playgrid.dto.response;

import java.util.UUID;

public record GameRoomResponse(UUID roomId, String name) {
}
