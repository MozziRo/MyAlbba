import SockJS from 'sockjs-client'
import { WS_URLS } from '../utils/wsConfig'

// 싱글톤 패턴

class WebSocketInstance {
  constructor() {
    this.sock = null
    this.messageHandlers = new Map()
    // console.debug("[WebSocketInstance] 인스턴스 생성됨")
  }

  // WebSocket 연결 설정
  connect() {
    return new Promise((resolve, reject) => {
      // console.debug("[WebSocketInstance] connect() 호출됨, WS_URLS.RTC =", WS_URLS.RTC)

      this.sock = new SockJS(WS_URLS.RTC)

      this.sock.onopen = () => {
        // console.log("[WebSocketInstance] SockJS 연결 성공")
        resolve()
      }

      this.sock.onmessage = (event) => {
        // console.debug("[WebSocketInstance] 메시지 수신:", event.data)
        const message = JSON.parse(event.data)
        this.handleMessage(message)
      }

      this.sock.onerror = (error) => {
        // console.error("[WebSocketInstance] SockJS 에러:", error)
        reject(error)
      }

      this.sock.onclose = () => {
        // console.log("[WebSocketInstance] SockJS 연결 종료")
      }
    })
  }

  // 메시지 핸들러 등록
  on(type, handler) {
    this.messageHandlers.set(type, handler)
    // console.log("[WebSocketInstance] 핸들러 등록됨: type =", type)
  }

  // 메시지 핸들러 제거
  off(type) {
    this.messageHandlers.delete(type)
    // console.log("[WebSocketInstance] 핸들러 제거됨: type =", type)
  }

  // 모든 핸들러 제거
  clearHandlers() {
    this.messageHandlers.clear()
    // console.log("[WebSocketInstance] 모든 핸들러 제거됨")
  }

  // 메시지 처리
  handleMessage(message) {
    // console.log("[WebSocketInstance] handleMessage() 호출됨, message:", message)
    const handler = this.messageHandlers.get(message.type)
    if (handler) {
      handler(message.data)
    } else {
      // console.log("[WebSocketInstance] 등록되지 않은 메시지 타입:", message.type)
    }
  }

  // 시그널링 메시지 전송
  send(type, data) {
    if (this.sock && this.sock.readyState === SockJS.OPEN) {
      const message = JSON.stringify({
        type,
        data
      })
      // console.log("[WebSocketInstance] send() 호출됨, message:", message)
      this.sock.send(message)
    } else {
      console.error("[WebSocketInstance] SockJS가 연결되어 있지 않습니다. 현재 상태:",
        this.sock ? this.sock.readyState : 'sock is null')
    }
  }

  // 연결 종료
  disconnect() {
    if (this.sock) {
      // console.log("[WebSocketInstance] disconnect() 호출됨")
      this.sock.close()
      this.sock = null
    }
  }
}

// 싱글톤 인스턴스 생성 및 내보내기
export const webSocketInstance = new WebSocketInstance()
