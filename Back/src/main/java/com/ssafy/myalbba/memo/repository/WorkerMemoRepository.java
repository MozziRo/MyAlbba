package com.ssafy.myalbba.memo.repository;

import com.ssafy.myalbba.memo.entity.WorkerMemo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerMemoRepository extends JpaRepository<WorkerMemo, Long> {
}
