package com.ssafy.myalbba.salary.entity;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class DailySalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_salary_id")
    private Long id;

    private Long confirmedScheduleId;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private Store store;

    private LocalDate date;
    private Double hours;
    private int dailyAmount;

    private DailySalary(Long confirmedScheduleId, LocalDate date, Double hours, int dailyAmount) {
        this.confirmedScheduleId = confirmedScheduleId;
        this.date = date;
        this.hours = hours;
        this.dailyAmount = dailyAmount;
    }

    public static DailySalary createDailySalary(Long confirmedScheduleId, LocalDate date, Double hours, int dailyAmount) {
        return new DailySalary(confirmedScheduleId, date, hours, dailyAmount);
    }

    public void setWorker(Worker worker) { this.worker = worker; }
    public void setStore(Store store) { this.store = store; }

}
