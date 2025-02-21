package com.ssafy.myalbba.salary.entity;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.memo.entity.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private Store store;

    private LocalDate salaryDate;
    private Double totalHours;
    private int monthlySalary;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Salary(LocalDate salaryDate, Double totalHours, int monthlySalary) {
        this.salaryDate = salaryDate;
        this.totalHours = totalHours;
        this.monthlySalary = monthlySalary;
        this.status = Status.NOT_DONE;
    }

    public static Salary createSalary(LocalDate salaryDate, Double totalHours, int monthlySalary) {
        return new Salary(salaryDate, totalHours, monthlySalary);
    }

    public void setWorker(Worker worker) { this.worker = worker; }
    public void setStore(Store store) { this.store = store; }

    public void updateMonthlySalary(int updateMonthlySalary, Double totalHours) {
        this.monthlySalary = updateMonthlySalary;
        this.totalHours = totalHours;
    }

    public void updateSalaryStatus() { this.status = Status.DONE; }
}
