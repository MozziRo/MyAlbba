// WebRTC 관련 핵심 로직을 함수형으로 구현하는 모듈

// PeerConnection 생성 및 로컬 스트림 트랙 추가
const createPeerConnection = (localStream) => {
  const configuration = {
    iceServers: [{ urls: 'stun:stun.l.google.com:19302' }]
  }

  const peerConnection = new RTCPeerConnection(configuration)

  if (localStream) {
    localStream.getTracks().forEach(track => {
      peerConnection.addTrack(track, localStream)
    })
  }

  return peerConnection
}

// Offer 생성 및 LocalDescription 설정
const createOffer = async (peerConnection) => {
  try {
    const offer = await peerConnection.createOffer()
    await peerConnection.setLocalDescription(offer)
    return offer
  } catch (error) {
    console.error('Offer 생성 실패:', error)
    throw error
  }
}

// Answer 생성 및 LocalDescription 설정
const createAnswer = async (peerConnection, message) => {
  try {
    const { data } = message
    if (!data?.sdp) {
      throw new Error('오퍼 메시지에 sdp가 없습니다.')
    }

    await peerConnection.setRemoteDescription(
      new RTCSessionDescription({
        type: 'offer',
        sdp: data.sdp
      })
    )
    const answer = await peerConnection.createAnswer()
    await peerConnection.setLocalDescription(answer)
    return answer
  } catch (error) {
    console.error('Answer 생성 실패:', error)
    throw error
  }
}

// Answer 처리
const handleAnswer = async (peerConnection, message) => {
  try {
    const { data } = message
    if (!data?.sdp) {
      throw new Error('답변 메시지에 sdp가 없습니다.')
    }

    await peerConnection.setRemoteDescription(
      new RTCSessionDescription({
        type: 'answer',
        sdp: data.sdp
      })
    )
  } catch (error) {
    console.error('Answer 처리 실패:', error)
    throw error
  }
}

// ICE Candidate 추가
const addIceCandidate = async (peerConnection, message) => {
  try {
    const { data } = message
    if (!data?.candidate) {
      throw new Error('ICE candidate가 없습니다.')
    }

    await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate))
  } catch (error) {
    console.error('ICE candidate 추가 실패:', error)
    throw error
  }
}

export const webRTCManager = {
  createPeerConnection,
  createOffer,
  createAnswer,
  handleAnswer,
  addIceCandidate
}
