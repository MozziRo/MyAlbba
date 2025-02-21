package com.ssafy.myalbba.webrtc;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManager {

    private final Set<String> validRooms = ConcurrentHashMap.newKeySet();

    public void addRoom(String roomId) {
        validRooms.add(roomId);
    }

    public boolean isValidRoom(String roomId) {
        return validRooms.contains(roomId);
    }

    public void removeRoom(String roomId) {
        validRooms.remove(roomId);
    }
}
