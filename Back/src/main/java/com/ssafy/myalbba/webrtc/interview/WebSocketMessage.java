package com.ssafy.myalbba.webrtc.interview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
//    private String sender; // 보내는 유저 UUID
////    private String receiver;  // 받는사람
//    private String type; // 메시지 타입
//    private String roomId; // roomId
//    private Object ice; // 상태
//    private Object sdp; // sdp 정보
    
    private Object data; // 모든 데이터
}
