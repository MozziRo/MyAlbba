package com.ssafy.myalbba.schedule.entity;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.common.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SubstituteSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "substitute_schedule_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "confirmed_schedule_id")
    private ConfirmedSchedule confirmedSchedule;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Worker requester;

    @ManyToOne
    @JoinColumn(name = "substitute_id")
    private Worker substitute;

    public static SubstituteSchedule createSubstituteSchedule() {
        return new SubstituteSchedule();
    }

    public void setConfirmedSchedule(ConfirmedSchedule confirmedSchedule) { this.confirmedSchedule = confirmedSchedule; }
    public void setStore(Store store) { this.store = store; }
    public void setRequester(Worker requester) { this.requester = requester; }
}
