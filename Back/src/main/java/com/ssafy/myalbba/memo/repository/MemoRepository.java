package com.ssafy.myalbba.memo.repository;

import com.ssafy.myalbba.memo.dto.MemoResponseDto;
import com.ssafy.myalbba.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByCreatorId(Long id);
    List<Memo> findByStoreId(Long storeId);
}
