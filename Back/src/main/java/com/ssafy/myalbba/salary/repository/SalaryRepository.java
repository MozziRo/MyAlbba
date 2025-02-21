package com.ssafy.myalbba.salary.repository;

import com.ssafy.myalbba.salary.entity.DailySalary;
import com.ssafy.myalbba.salary.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Optional<Salary> findByWorkerId(long workerId);

    void deleteByWorkerId(long workerId);

    List<Salary> findByStoreId(Long storeId);

    Salary findByWorkerIdAndSalaryDate(Long workerId, LocalDate salaryDate);
}
