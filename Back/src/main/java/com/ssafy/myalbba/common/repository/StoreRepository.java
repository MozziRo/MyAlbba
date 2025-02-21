package com.ssafy.myalbba.common.repository;

import com.ssafy.myalbba.common.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByPayday(int payday);

    Optional<Store> findByOwnerId(Long ownerId);

    List<Store> findByReviewWidgetTrue();
}
