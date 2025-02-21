import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import { useLocalVideoStore } from './localVideoStore'
import { webSocketInstance } from '@/common/apis/ws/webSocket'
import { webRTCManager } from '@/common/webrtc/webRTCManager'

export const useWebRTCStore = defineStore('webRTCStore', () => {
    const peerConnection = ref(null)
    //
    // {
    //   id: trackId
    //   stream: stream
    // }
    //
    const remoteStream = ref(null)
    const isConnected = ref(false)
    const connectionState = ref('')
    const localVideoStore = useLocalVideoStore()
    let stateCheckInterval = null

    const createPeerConnection = () => {
        peerConnection.value = webRTCManager.createPeerConnection(localVideoStore.localStream)
    }

    // WebRTC 초기화
    const initializeWebRTC = async () => {
        try {
            // 원격 스트림 처리
            peerConnection.value.ontrack = (event) => {
                // console.log('Remote track received:', event.streams[0])
                remoteStream.value = event.streams[0]
            }

            // ICE candidate 교환
            peerConnection.value.onicecandidate = (event) => {
              if (event.candidate) {
                  // console.log('로컬 ICE candidate 전송:', event.candidate)
                  webSocketInstance.send('ice', { candidate: event.candidate })
              }
            }

            // ICE 상태 변경 처리
            peerConnection.value.oniceconnectionstatechange = () => {
                // console.log('ICE connection state:', peerConnection.value.iceConnectionState)
                isConnected.value = peerConnection.value.iceConnectionState === 'connected'
            }

            return peerConnection.value
        } catch (error) {
            console.error('WebRTC 초기화 실패:', error)
            throw error
        }
    }

    // 연결 상태 모니터링 시작
    const startStateMonitoring = () => {
        // 이전 인터벌이 있다면 제거
        if (stateCheckInterval) {
            clearInterval(stateCheckInterval)
        }

        // 1초마다 연결 상태 로깅
        stateCheckInterval = setInterval(() => {
            if (peerConnection.value) {
                console.log(`[WebRTC] Connection State: ${peerConnection.value.connectionState}`)
                console.log(`[WebRTC] ICE Connection State: ${peerConnection.value.iceConnectionState}`)
                console.log(`[WebRTC] Signaling State: ${peerConnection.value.signalingState}`)
                console.log('------------------------')
            }
        }, 1000)
    }

    // 시그널링 핸들러 설정
    const initializeSignaling = () => {
        // Offer 수신 처리
        webSocketInstance.on('offer', async (data) => {
            // console.log('Offer 수신:', data)
            try {
                if (!peerConnection.value) {
                    await initializeWebRTC()
                }
                const answer = await webRTCManager.createAnswer(peerConnection.value, {data})
                webSocketInstance.send('answer', { sdp: answer.sdp })

            } catch (error) {
                console.error('Offer 처리 실패:', error)
            }
        })

        // Answer 수신 처리
        webSocketInstance.on('answer', async (data) => {
            // console.log('Answer 수신:', data)
            try {
                if (!peerConnection.value) {
                    console.error('PeerConnection이 초기화되지 않았습니다')
                    return
                }
                await webRTCManager.handleAnswer(peerConnection.value, { data })

            } catch (error) {
                console.error('Answer 처리 실패:', error)
            }
        })

        // ICE candidate 수신 처리
        webSocketInstance.on('ice', async (data) => {
            // console.log('ICE candidate 수신:', data)
            try {
                if (!peerConnection.value) {
                    console.error('PeerConnection이 초기화되지 않았습니다')
                    return
                }
                await webRTCManager.addIceCandidate(peerConnection.value, { data })
            } catch (error) {
                console.error('ICE candidate 처리 실패:', error)
            }
        })
    }

    // Offer 생성 및 전송
    const createAndSendOffer = async () => {
        try {
            if (!peerConnection.value) {
                await initializeWebRTC()
            }
            const offer = await webRTCManager.createOffer(peerConnection.value)
            webSocketInstance.send('offer', { sdp: offer.sdp })
            // ICE 교환은 Answer를 받은 후에 시작됨
        } catch (error) {
            console.error('Offer 생성 및 전송 실패:', error)
            throw error
        }
    }

    // 연결 종료
    const closePeerConnection = () => {
        if (peerConnection.value) {
            peerConnection.value.close()
            peerConnection.value = null
        }
        remoteStream.value = null
        isConnected.value = false
    }

    return {
        remoteStream,
        isConnected,
        connectionState,
        createPeerConnection,
        initializeWebRTC,
        createAndSendOffer,
        closePeerConnection,
        initializeSignaling
    }
})
