package com.ssafy.myalbba.Member.repository;

import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.Member.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    // 이메일 중복 체크
    Boolean existsByEmail(String email);

    Optional<Owner> findByEmail(String email);
    Optional<Owner> findByPhoneNumber(String phoneNumber);
}
