package com.ssafy.myalbba.common.dto;

import com.ssafy.myalbba.common.entity.SchedulePeriod;
import com.ssafy.myalbba.common.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {
    private Long storeId;
    private Long ownerId;
    private String name;
    private String address;
    private String phoneNumber;
    private int payday;

    public StoreDTO(Long ownerId, String name, String address, String phoneNumber, int payday) {
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.payday = payday;
    }

    public static StoreDTO from(Store store) {
        return new StoreDTO(store.getId(), store.getOwner().getId(), store.getName(), store.getAddress(), store.getPhoneNumber(), store.getPayday());
    }
}
