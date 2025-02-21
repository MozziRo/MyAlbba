package com.ssafy.myalbba.review.service;

import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.common.repository.StoreRepository;
import com.ssafy.myalbba.review.dto.ReviewResponseDTO;
import com.ssafy.myalbba.review.entity.Review;
import com.ssafy.myalbba.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final int MAX_REVIEW_LENGTH = 255;

    private final WebClient webClient;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    private String truncateText(String text) {
        if(text == null) return "";
        return text.length() > MAX_REVIEW_LENGTH ? text.substring(0, MAX_REVIEW_LENGTH) : text;
    }

    @Override
    @Transactional
    public void fetchAndSaveReviews(String loginUserEmail) {
        UserEntity loginUser = userRepository.findByEmail(loginUserEmail);
        Store store = loginUser.getStore();

        System.out.println("ReviewServiceImpl : fetchAndSaveReviews");

        List<ReviewResponseDTO> reviews = webClient.post()
                .uri("/crawl_and_save/")
                .bodyValue(Map.of("store_name", store.getName()))
                .retrieve()
                .bodyToFlux(ReviewResponseDTO.class)
                .collectList()
                .block();

        List<Review> reviewEntities = reviews.stream()
                .map(ReviewResponseDTO -> Review.createReview(store, truncateText(ReviewResponseDTO.getReviewText())))
                .collect(Collectors.toList());

        reviewRepository.saveAll(reviewEntities);

        store.setReviewWidget(true); // 리뷰 위젯 true로 업데이트
    }

    @Override
    @Scheduled(cron = "0 0 0 * * 1")
    public void fetchAndSaveAllReviews() {
        List<Store> stores = storeRepository.findByReviewWidgetTrue();

        stores.stream()
                .forEach(store -> {
                    try {
                        List<ReviewResponseDTO> reviews = webClient.post()
                                .uri("/crawl_and_save/")
                                .bodyValue(Map.of("store_name", store.getName())) // store 이름 전달
                                .retrieve()
                                .bodyToFlux(ReviewResponseDTO.class)
                                .collectList()
                                .block();

                        List<Review> reviewEntities = reviews.stream()
                                .map(dto -> Review.createReview(store, dto.getReviewText()))
                                .toList();

                        reviewRepository.saveAll(reviewEntities);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public List<ReviewResponseDTO> getReviews(String loginUserEmail) {
        UserEntity loginUser = userRepository.findByEmail(loginUserEmail);

        List<Review> reviews = reviewRepository.findTop20ByStoreIdOrderByCreatedAtDesc(loginUser.getStore().getId());

        return reviews.stream()
                .map(ReviewResponseDTO::from)
                .collect(Collectors.toList());
    }

}
