package com.ssafy.myalbba.review.repository;

import com.ssafy.myalbba.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findTop20ByStoreIdOrderByCreatedAtDesc(Long storeId);
}
