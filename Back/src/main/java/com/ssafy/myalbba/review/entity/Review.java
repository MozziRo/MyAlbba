package com.ssafy.myalbba.review.entity;

import com.ssafy.myalbba.common.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "review_text")
    private String reviewText;

    @CreatedDate
    private LocalDateTime createdAt;

    private Review(Store store, String reviewText) {
        this.store = store;
        this.reviewText = reviewText;
    }

    public static Review createReview(Store store, String reviewText) {
        return new Review(store, reviewText);
    }

}
