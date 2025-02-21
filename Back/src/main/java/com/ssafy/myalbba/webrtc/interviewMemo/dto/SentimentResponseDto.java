package com.ssafy.myalbba.webrtc.interviewMemo.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class SentimentResponseDto {
    private String text;
    private Map<String, Double> sentiment_scores;
}
