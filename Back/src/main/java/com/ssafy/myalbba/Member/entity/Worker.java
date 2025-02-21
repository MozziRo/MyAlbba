package com.ssafy.myalbba.Member.entity;

import com.ssafy.myalbba.Member.dto.WorkerDTO;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.memo.entity.Memo;
import com.ssafy.myalbba.memo.entity.WorkerMemo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Worker extends UserEntity{

    @PrimaryKeyJoinColumn // 부모의 id를 자식 테이블의 owner_id로 매핑
    private Long id;

    private Integer hourlySalary;
    private String bank;
    private String account;
    private Double tax;
    private Double insurance;
    private Boolean holidayPay;

    @Transient
    private String ownerEmail;

    @Transient
    private boolean isVerified = false;

    @PrePersist
    private void prePersist(){
        if(this.hourlySalary == null) this.hourlySalary = 10030;
        if(this.tax == null) this.tax = 0.0;
        if(this.insurance == null) this.insurance = 0.0;
        if(this.holidayPay == null) this.holidayPay = false;
    }

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkerMemo> workerMemos = new ArrayList<>();

    public void addWorkersMemo(WorkerMemo workerMemo) {
        this.workerMemos.add(workerMemo);
        workerMemo.setWorker(this);
    }

    public Worker(String email, String password, String name, String role, String phoneNumber, String bank, String account) {
        super(email, password, name, role, phoneNumber);
        this.bank = bank;
        this.account = account;
    }

    public static Worker createWorker(String email, String password, String name, String role, String phoneNumber, String bank, String account) {
        return new Worker(email, password, name, role, phoneNumber, bank, account);
    }

    public void updateWorker(WorkerDTO workerDTO) {
        this.updateUser(workerDTO);
        if (workerDTO.getHourlySalary() != null) this.hourlySalary = workerDTO.getHourlySalary();
        if (workerDTO.getBank() != null) this.bank = workerDTO.getBank();
        if (workerDTO.getAccount() != null) this.account = workerDTO.getAccount();
        if (workerDTO.getTax() != null) this.tax = workerDTO.getTax();
        if (workerDTO.getInsurance() != null) this.insurance = workerDTO.getInsurance();
        if (workerDTO.getHolidayPay()) this.holidayPay = workerDTO.getHolidayPay();
    }

    public void setStore(Store store) {
        super.setStore(store);
    }
}
