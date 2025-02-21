package com.ssafy.myalbba.Member.entity;

import com.ssafy.myalbba.Member.dto.OwnerDTO;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Owner extends UserEntity{

    @PrimaryKeyJoinColumn // 부모의 id를 자식 테이블의 owner_id로 매핑
    private Long id;

    private String bank;

    @Transient
    private boolean isVerified;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> notices = new ArrayList<>();

    public void addNotice(Notice notice){
        this.notices.add(notice);
        notice.setOwner(this);
    }

    @Builder
    public Owner(String email, String password, String name, String role, String phoneNumber) {
        super(email, password, name, role, phoneNumber);
    }

    public static Owner createOwner(String email, String password, String name, String role, String phoneNumber, String bank) {
        Owner owner = new Owner(email, password, name, role, phoneNumber);
        owner.setBank(bank);
        return owner;
    }

    public void setStore(Store store){
        super.setStore(store);
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void updateOwner(OwnerDTO ownerDTO) {
        this.updateUser(ownerDTO);
    }
}
