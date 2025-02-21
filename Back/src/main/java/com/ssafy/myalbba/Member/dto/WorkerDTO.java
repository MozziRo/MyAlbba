package com.ssafy.myalbba.Member.dto;

import com.ssafy.myalbba.Member.entity.Worker;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDTO {
    private long workerId;

    private String ownerEmail;
    private String code;

    private String role;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    private Integer hourlySalary;
    private String bank;
    private String account;

    private Double tax;
    private Double insurance;
    private Boolean holidayPay;

    private WorkerDTO(Long workerId, String name) {
        this.workerId = workerId;
        this.name = name;
    }

    public static WorkerDTO buildForAllWorkers(Worker worker) {
        return WorkerDTO.builder()
                .workerId(worker.getId())
                .name(worker.getName())
                .build();
    }

    public static WorkerDTO buildForWorkerInfo(Worker worker) {
        return WorkerDTO.builder()
                .workerId(worker.getId())
                .email(worker.getEmail())
                .name(worker.getName())
                .phoneNumber(worker.getPhoneNumber())
                .bank(worker.getBank())
                .account(worker.getAccount())
                .hourlySalary(worker.getHourlySalary())
                .tax(worker.getTax())
                .insurance(worker.getInsurance())
                .holidayPay(worker.getHolidayPay())
                .build();
    }

}
