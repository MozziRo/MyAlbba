package com.ssafy.myalbba.webrtc.interview;

import com.ssafy.myalbba.webrtc.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webrtc")
public class WebRtcController {

    private final RoomManager roomManager;

    @GetMapping("/create")
    public ResponseEntity<Map<String, String>> createRoom() {
        String roomId = UUID.randomUUID().toString();
        roomManager.addRoom(roomId);

        return ResponseEntity.ok(Map.of(
                "roomId", roomId,
                "url", "http://i12a304.p.ssafy.io/video-interview/" + roomId
        ));
    }

    @GetMapping("/{roomId}/exists")
    public ResponseEntity<Boolean> checkRoomExists(@PathVariable("roomId") String roomId) {
        boolean exists = roomManager.isValidRoom(roomId);
        return ResponseEntity.ok(exists);
    }
}