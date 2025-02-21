package com.ssafy.myalbba.webrtc.interviewMemo.controller;

import com.ssafy.myalbba.webrtc.interviewMemo.dto.KeywordsResponseDto;
import com.ssafy.myalbba.webrtc.interviewMemo.dto.InterviewMemoRequestDto;
import com.ssafy.myalbba.webrtc.interviewMemo.dto.SentimentResponseDto;
import com.ssafy.myalbba.webrtc.interviewMemo.service.InterviewMemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memo")
@RequiredArgsConstructor
public class InterviewMemoController {

    private final InterviewMemoService interviewMemoService;

    @PostMapping("/analyze_sentiment")
    public ResponseEntity<SentimentResponseDto> analyzeSentiment(@RequestBody InterviewMemoRequestDto requestDto) {
        SentimentResponseDto response = interviewMemoService.analyzeSentiment(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/extract_keywords")
    public ResponseEntity<KeywordsResponseDto> extractKeywords(@RequestBody InterviewMemoRequestDto requestDto) {
        KeywordsResponseDto response = interviewMemoService.extractKeywords(requestDto);
        return ResponseEntity.ok(response);
    }
}
