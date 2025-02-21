package com.ssafy.myalbba.Member.entity;

import com.ssafy.myalbba.Member.dto.OwnerDTO;
import com.ssafy.myalbba.Member.dto.WorkerDTO;
import com.ssafy.myalbba.common.entity.Store;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "store_id", nullable = true)
    private Store store;

    private String email;
    private String password;
    private String name;
    private String role;
    private String phoneNumber;

    public UserEntity(String email, String password, String name, String role, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public void setStore(Store store) { this.store = store; }

    public void updateUser(OwnerDTO ownerDTO) {
        if(ownerDTO.getPassword() != null) this.password = ownerDTO.getPassword();
        this.phoneNumber = ownerDTO.getPhoneNumber();
    }

    public void updateUser(WorkerDTO workerDTO) {
        if(workerDTO.getPassword() != null) this.password = workerDTO.getPassword();
        if(workerDTO.getPhoneNumber() != null) this.phoneNumber = workerDTO.getPhoneNumber();
    }

}

