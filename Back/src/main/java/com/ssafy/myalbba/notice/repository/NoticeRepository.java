package com.ssafy.myalbba.notice.repository;

import com.ssafy.myalbba.notice.entity.Notice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByOwnerId(Long ownerId);
}
