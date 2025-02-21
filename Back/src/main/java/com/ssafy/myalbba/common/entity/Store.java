package com.ssafy.myalbba.common.entity;

import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.common.dto.StoreDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Owner owner;

    private String name;
    private String address;
    private String phoneNumber;
    private int payday;

    private Boolean reviewWidget;

    @PrePersist
    private void prePersist(){
        if(this.reviewWidget == null){ this.reviewWidget = false; }
    }

    public void setOwner(Owner owner) { this.owner = owner; }

    private Store (String name, String address, String phoneNumber, int payday) {
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.payday = payday;
    }

    public static Store createStore(String name, String address, String phoneNumber, int payday) {
        return new Store(name, address, phoneNumber, payday);
    }

    public Store(long id, String name, String address, String phoneNumber, int payday) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.payday = payday;
    }

    public void updateStore(StoreDTO storeDTO) {
        this.name = storeDTO.getName();
        this.address = storeDTO.getAddress();
        this.phoneNumber = storeDTO.getPhoneNumber();
        this.payday = storeDTO.getPayday();
    }

    public void setReviewWidget(boolean reviewWidget) { this.reviewWidget = reviewWidget; }
}
