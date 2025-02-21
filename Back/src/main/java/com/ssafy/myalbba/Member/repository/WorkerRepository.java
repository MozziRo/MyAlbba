package com.ssafy.myalbba.Member.repository;

import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    // 이메일 중복 체크
    Boolean existsByEmail(String email);

    // 같은 가게 알바생들 조회
    List<Worker> findByStoreIdAndEmailNot(Long storeId, String email);

    Optional<Worker> findByEmail(String email);
    UserEntity findByName(String name);
    UserEntity findByEmailAndPassword(String email, String password);
}
