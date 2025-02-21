package com.ssafy.myalbba.schedule.repository;

import com.ssafy.myalbba.schedule.entity.SubstituteSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubstituteScheduleRepository extends JpaRepository<SubstituteSchedule, Long> {
    List<SubstituteSchedule> findByStoreId(Long storeId);
}
