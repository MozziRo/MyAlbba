package com.ssafy.myalbba.Member.dto;

import com.ssafy.myalbba.Member.entity.Owner;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OwnerDTO {
    private Long ownerId;

    private String role;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String bank;

    private String storeName;
    private String storeAddress;
    private String storePhoneNumber;
    private int payday;

    public static OwnerDTO buildForOwnerInfo(Owner owner) {
        return OwnerDTO.builder()
                .ownerId(owner.getId())
                .email(owner.getEmail())
                .name(owner.getName())
                .phoneNumber(owner.getPhoneNumber())
                .bank(owner.getBank())
                .build();
    }
}

