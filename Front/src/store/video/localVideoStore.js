import { defineStore } from 'pinia'
import { ref } from 'vue'

// 로컬 비디오 스토어
export const useLocalVideoStore = defineStore('localVideoStore', () => {
    // 상태
    const localStream = ref(null) // 로컬 비디오 스트림
    const isVideoOn = ref(false) // 비디오 활성화 여부
    const isAudioOn = ref(false) // 오디오 활성화 여부

    // 로컬 비디오 스트림 시작
     const startLocalStream = async () => {
        try {
            // 사용자의 미디어 스트림 가져오기
            localStream.value = await navigator.mediaDevices.getUserMedia({
                video: true,
                audio: true
            })
            // 최초 비디오 및 오디오 비활성화
            toggleVideo()
            toggleAudio()

          } catch (error) {
            console.error('미디어 스트림 얻기 실패:', error)
            return false
          }

          return true
    }

    // 비디오 토글
    const toggleVideo = () => {
        if (localStream.value) {
            const videoTrack = localStream.value.getVideoTracks()[0] // 비디오 트랙 가져오기
            videoTrack.enabled = !videoTrack.enabled // 비디오 트랙 활성화 여부 토글
            isVideoOn.value = videoTrack.enabled // 비디오 활성화 여부 업데이트

        }
        return isVideoOn.value
    }

    // 오디오 토글
    const toggleAudio = () => {
        if (localStream.value) {
            const audioTrack = localStream.value.getAudioTracks()[0] // 오디오 트랙 가져오기
            audioTrack.enabled = !audioTrack.enabled // 오디오 트랙 활성화 여부 토글
            isAudioOn.value = audioTrack.enabled // 오디오 활성화 여부 업데이트
        }
        return isAudioOn.value
    }

    // 로컬 스트림 정지
    const stopLocalStream = () => {
        if (localStream.value) {
            localStream.value.getTracks().forEach(track => track.stop()) // 모든 트랙 정지
            localStream.value = null // 로컬 스트림 초기화
            isVideoOn.value = false // 비디오 활성화 여부 초기화
            isAudioOn.value = false // 오디오 활성화 여부 초기화
        }
        return isVideoOn.value
    }

    return {
        // 상태
        localStream,
        isVideoOn,
        isAudioOn,
        // 액션
        startLocalStream,
        toggleVideo,
        toggleAudio,
        stopLocalStream
    }
})
