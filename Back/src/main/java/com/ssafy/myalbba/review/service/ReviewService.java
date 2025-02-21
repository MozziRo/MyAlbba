package com.ssafy.myalbba.review.service;

import com.ssafy.myalbba.review.dto.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    void fetchAndSaveReviews(String loginUserEmail);

    void fetchAndSaveAllReviews();

    List<ReviewResponseDTO> getReviews(String loginUserEmail);

}
