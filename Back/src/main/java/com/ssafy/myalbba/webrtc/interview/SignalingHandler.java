package com.ssafy.myalbba.webrtc.interview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SignalingHandler extends TextWebSocketHandler {

    // 세션 ID를 키로 하여 방 ID를 저장하는 맵
    private final Map<String, String> sessionToRoomMap = new ConcurrentHashMap<>();
    
    // 방에 들어가 있는 유저 저장
    private final Map<String, List<String>> roomInfo = new HashMap<>();

    // 세션 정보를 roomId로 관리
    private static final ConcurrentHashMap<String, Map<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New WebRTC connection: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {

    	// Json으로 메시지 받음
        JsonNode jsonMessage = objectMapper.readTree(textMessage.getPayload());
        
        // 필수 정보
        String type = jsonMessage.get("type").asText();
        JsonNode data = jsonMessage.get("data");
        
        log.info("받은 메시지: {}", textMessage.getPayload());  // 전체 메시지 로그
        
        if (type.equals("ice")) {
            log.info("ICE candidate 데이터: {}", data);  // ICE 데이터 로그
        }
        
        String uuid = session.getId();
        
        // 세션의 현재 방 ID 조회 (join이 아닌 경우)
        String roomId = type.equals("join") ? data.get("roomId").asText() : sessionToRoomMap.get(uuid);
        
        log.info("메시지 타입 {}, 보낸 사람 {}, 방 번호 {}", type, uuid, roomId);
        
        switch(type) {
            case "offer":
            case "answer":
            case "ice":
            case "leave-interview":
            case "emoticon":
            case "chat":
                // 해당 방의 다른 참가자들에게 원본 데이터 전달
                if (roomSessions.containsKey(roomId)) {
                    Map<String, WebSocketSession> sessions = roomSessions.get(roomId);
                    sessions.values().forEach(s -> {
                        // 자신을 제외한 같은 방의 다른 참가자들에게 전달
                        if (!s.getId().equals(uuid)) {
                            try {
                                // 원본 메시지 그대로 전달
                                s.sendMessage(textMessage);
                            } catch (Exception e) {
                                log.error("메시지 전송 오류: {}", e.getMessage());
                            }
                        }
                    });
                } else {
                    log.error("존재하지 않는 방입니다. 방 ID: {}", roomId);
                }
                break;

            case "join":
                log.info("{} 가 #{}번 방에 들어감", uuid, roomId);
                
                // 세션 ID와 방 ID 매핑 저장
                sessionToRoomMap.put(uuid, roomId);

                // 방이 이미 존재하는 경우
                if (roomSessions.containsKey(roomId)) {
                    Map<String, WebSocketSession> sessions = roomSessions.get(roomId);
                    sessions.put(uuid, session);
                    
                    // 새로 들어온 참가자를 제외한 모든 참가자에게 join 메시지 전달
                    sessions.values().forEach(s -> {
                        if (!s.getId().equals(uuid)) {
                            try {
                                // 원본 data에 sender 정보만 추가하여 전달
                                ObjectNode modifiedData = objectMapper.createObjectNode();
                                modifiedData.setAll((ObjectNode) data);
                                modifiedData.put("sender", uuid);

                                ObjectNode joinMessage = objectMapper.createObjectNode();
                                joinMessage.put("type", "join");
                                joinMessage.set("data", modifiedData);
                                
                                s.sendMessage(new TextMessage(joinMessage.toString()));
                            } catch (Exception e) {
                                log.error("Join 메시지 전송 오류", e);
                            }
                        }
                    });
                } else {
                    // 새로운 방 생성
                    Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
                    sessions.put(uuid, session);
                    roomSessions.put(roomId, sessions);
                }

                // roomInfo 업데이트
                if (roomInfo.containsKey(roomId)) {
                    roomInfo.get(roomId).add(uuid);
                } else {
                    // 방이 존재하지 않는다면 생성하고 추가
                    List<String> newRoom = new ArrayList<>();
                    newRoom.add(uuid);
                    roomInfo.put(roomId, newRoom);
                }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uuid = session.getId();
        String roomId = sessionToRoomMap.get(uuid);
        log.info("클라이언트 접속 해제 : 세션 - {}, 방 - {}, 상태 - {}", session, roomId, status);
        
        // 세션-방 매핑 제거
        sessionToRoomMap.remove(uuid);
        
        // 방에서 세션 제거
        if (roomId != null && roomSessions.containsKey(roomId)) {
            Map<String, WebSocketSession> sessions = roomSessions.get(roomId);
            
            // 다른 참가자들에게 퇴장 알림
            sessions.values().forEach(s -> {
                if (!s.getId().equals(uuid)) {
                    try {
                        ObjectNode leaveData = objectMapper.createObjectNode();
                        leaveData.put("sender", uuid);
                        
                        ObjectNode leaveMessage = objectMapper.createObjectNode();
                        leaveMessage.put("type", "leave");
                        leaveMessage.set("data", leaveData);
                        
                        s.sendMessage(new TextMessage(leaveMessage.toString()));
                    } catch (Exception e) {
                        log.error("Leave 메시지 전송 오류", e);
                    }
                }
            });
            
            // 세션 정리
            sessions.remove(uuid);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
        
        // 세션-방 매핑 제거
        sessionToRoomMap.remove(uuid);
        
        // roomInfo에서도 제거
        if (roomId != null && roomInfo.containsKey(roomId)) {
            List<String> users = roomInfo.get(roomId);
            users.remove(uuid);
            if (users.isEmpty()) {
                roomInfo.remove(roomId);
            }
        }
    }
}
