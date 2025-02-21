package com.ssafy.myalbba.review.controller;


import com.ssafy.myalbba.review.dto.ReviewResponseDTO;
import com.ssafy.myalbba.review.dto.ReviewsAndSentimentResponseDTO;
import com.ssafy.myalbba.review.service.ReviewService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import com.ssafy.myalbba.webrtc.interviewMemo.dto.InterviewMemoRequestDto;
import com.ssafy.myalbba.webrtc.interviewMemo.dto.SentimentResponseDto;
import com.ssafy.myalbba.webrtc.interviewMemo.service.InterviewMemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Tag(name = "Review", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;
    private final InterviewMemoService interviewMemoService;

    @PostMapping("/crawl_and_save")
    @Operation(summary = "리뷰 크롤링 요청 및 저장", description = "가게 리뷰 크롤링을 요청하고 저장하는 API")
    public ResponseEntity<?> fetchAndSaveReviews(@AuthenticationPrincipal CustomUserDetails loginUser) {
        reviewService.fetchAndSaveReviews(loginUser.getUserEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "리뷰 조회 및 감성 분석", description = "가게 리뷰를 조회하고 감성 분석을 돌리는 API")
    public ResponseEntity<ReviewsAndSentimentResponseDTO> getReviews(@AuthenticationPrincipal CustomUserDetails loginUser) {
        List<ReviewResponseDTO> reviews = reviewService.getReviews(loginUser.getUserEmail());
        String combinedText = reviews.stream()
                .map(ReviewResponseDTO::getReviewText)
                .collect(Collectors.joining("\n"));

        InterviewMemoRequestDto interviewMemoRequestDto = new InterviewMemoRequestDto();
        interviewMemoRequestDto.setText(combinedText);

        SentimentResponseDto sentimentResult = interviewMemoService.analyzeSentiment(interviewMemoRequestDto);
//        Map<String, Double> sentimentScores = new HashMap<>();
//        sentimentScores.put("neg", 0.111);
//        sentimentScores.put("neu", 0.777);
//        sentimentScores.put("pos", 0.111);
//
//        SentimentResponseDto sentimentResult = new SentimentResponseDto();
//        sentimentResult.setText(sentimentScores.toString());
//        sentimentResult.setSentiment_scores(sentimentScores);
        ReviewsAndSentimentResponseDTO reviewsAndSentimentResponseDTO = new ReviewsAndSentimentResponseDTO(reviews, sentimentResult);

        System.out.println(reviewsAndSentimentResponseDTO);
        return ResponseEntity.ok(reviewsAndSentimentResponseDTO);
    }
}
