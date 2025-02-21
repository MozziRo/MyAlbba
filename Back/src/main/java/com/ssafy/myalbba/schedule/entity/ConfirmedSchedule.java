package com.ssafy.myalbba.schedule.entity;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.common.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "confirmed_schedule")
public class ConfirmedSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirmed_schedule_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private Store store;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "date")
    private LocalDate date;

    private ConfirmedSchedule(LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public static ConfirmedSchedule createConfirmedSchedule(LocalTime startTime, LocalTime endTime, LocalDate date) {
        return new ConfirmedSchedule(startTime, endTime, date);
    }

    public void setWorker(Worker worker) {this.worker = worker;}
    public void setStore(Store store) {this.store = store;}

}

