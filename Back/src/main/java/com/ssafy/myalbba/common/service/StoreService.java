package com.ssafy.myalbba.common.service;

import com.ssafy.myalbba.common.dto.StoreDTO;

public interface StoreService {
    Long createStore(StoreDTO storeDTO);

    void updateStore(String loginUserEmail, StoreDTO storeDTO);

    StoreDTO getStore(String loginUserEmail);

}
