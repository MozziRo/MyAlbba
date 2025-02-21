package com.ssafy.myalbba.salary.dto;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.memo.entity.Status;
import com.ssafy.myalbba.salary.entity.Salary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class SalaryDTO {
    private Long salaryId;
    private Long workerId;
    private String workerName;
    private String workerAccount;
    private LocalDate salaryDate;
    private Double totalHours;
    private int monthlySalary;
    private String status;
    private String bank;

    public static SalaryDTO from(Salary salary) {
        Worker worker = salary.getWorker();

        return SalaryDTO.builder()
                .salaryId(salary.getId())
                .workerId(worker.getId())
                .workerName(worker.getName())
                .workerAccount(worker.getAccount())
                .salaryDate(salary.getSalaryDate())
                .totalHours(salary.getTotalHours())
                .monthlySalary(salary.getMonthlySalary())
                .status(salary.getStatus().toString())
                .bank(worker.getBank())
                .build();
    }

}
