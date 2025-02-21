package com.ssafy.myalbba.webrtc.interviewMemo.service;

import com.ssafy.myalbba.webrtc.interviewMemo.dto.KeywordsResponseDto;
import com.ssafy.myalbba.webrtc.interviewMemo.dto.InterviewMemoRequestDto;
import com.ssafy.myalbba.webrtc.interviewMemo.dto.SentimentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class InterviewMemoService {

    private final WebClient webClient;

    public SentimentResponseDto analyzeSentiment(InterviewMemoRequestDto requestDto) {
        System.out.println("analyzeSentiment : 호출");
        return webClient.post()
                .uri("/analyze_sentiment/")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(SentimentResponseDto.class)
                .block();
    }

    public KeywordsResponseDto extractKeywords(InterviewMemoRequestDto requestDto) {
        return webClient.post()
                .uri("/extract_keywords/")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(KeywordsResponseDto.class)
                .block();
    }
}
