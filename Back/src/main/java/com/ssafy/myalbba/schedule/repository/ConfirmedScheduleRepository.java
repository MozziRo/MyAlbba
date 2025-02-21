package com.ssafy.myalbba.schedule.repository;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmedScheduleRepository extends JpaRepository<ConfirmedSchedule, Long> {
    List<ConfirmedSchedule> findByStoreId(Long storeId);
    List<ConfirmedSchedule> findByWorkerId(Long workerId);
    List<ConfirmedSchedule> findByDate(LocalDate date);

    Optional<ConfirmedSchedule> findTopByWorkerAndDateAfterOrderByDateAscStartTimeAsc(Worker worker, LocalDate now);

    @Query("SELECT cs.worker FROM ConfirmedSchedule cs WHERE cs = :confirmedSchedule")
    List<Worker> findWorkersByConfirmedSchedule(@Param("confirmedSchedule") ConfirmedSchedule confirmedSchedule);

    Optional<ConfirmedSchedule> findTopByDateAndStartTimeAfterOrderByStartTimeAsc(LocalDate date, LocalTime endTime);
}

