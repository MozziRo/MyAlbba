package com.ssafy.myalbba.salary.repository;

import com.ssafy.myalbba.salary.entity.DailySalary;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DailySalaryRepository extends JpaRepository<DailySalary, Long> {

    List<DailySalary> findByWorkerId(Long workerId);

    List<DailySalary> findByStoreId(Long storeId);

    DailySalary findByConfirmedScheduleId(Long confirmedScheduleId);

    DailySalary deleteByConfirmedScheduleId(Long confirmedScheduleId);
}
