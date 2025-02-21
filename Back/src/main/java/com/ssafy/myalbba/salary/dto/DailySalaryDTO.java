package com.ssafy.myalbba.salary.dto;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.salary.entity.DailySalary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailySalaryDTO {
    private Long workerId;
    private String workerName;
    private LocalDate date;
    private Double hours;
    private int dailyAmount;

    public static DailySalaryDTO from(DailySalary dailySalary) {
        Worker worker = dailySalary.getWorker();
        return new DailySalaryDTO(worker.getId(), worker.getName(), dailySalary.getDate(), dailySalary.getHours(), dailySalary.getDailyAmount());
    }
}
