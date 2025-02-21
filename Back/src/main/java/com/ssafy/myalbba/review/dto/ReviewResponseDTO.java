package com.ssafy.myalbba.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.myalbba.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    @JsonProperty("review_text")
    private String reviewText;

    public static ReviewResponseDTO from(Review review) {
        return new ReviewResponseDTO(review.getReviewText());
    }

}
