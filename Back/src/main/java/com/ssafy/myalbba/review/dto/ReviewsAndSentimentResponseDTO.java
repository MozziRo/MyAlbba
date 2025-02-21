package com.ssafy.myalbba.review.dto;

import com.ssafy.myalbba.webrtc.interviewMemo.dto.SentimentResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsAndSentimentResponseDTO {
    private List<ReviewResponseDTO> reviews;
    private SentimentResponseDto sentiment;
}
