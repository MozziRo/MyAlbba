package com.ssafy.myalbba.common.service;

import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.repository.OwnerRepository;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.common.dto.StoreDTO;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.common.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public Long createStore(StoreDTO storeDTO) {
        Store store = Store.createStore(storeDTO.getName(), storeDTO.getAddress(), storeDTO.getPhoneNumber(), storeDTO.getPayday());

        Owner owner = ownerRepository.findById(storeDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("사장님을 찾을 수 없습니다."));

        store.setOwner(owner);
        return storeRepository.save(store).getId();
    }

    @Override
    public StoreDTO getStore(String loginUserEmail) {
        UserEntity userEntity = userRepository.findByEmail(loginUserEmail);

        Store store = storeRepository.findById(userEntity.getStore().getId())
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
        return StoreDTO.from(store);
    }

    @Override
    @Transactional
    public void updateStore(String loginUserEmail, StoreDTO storeDTO) {
        UserEntity userEntity = userRepository.findByEmail(loginUserEmail);

        Store store = storeRepository.findByOwnerId(userEntity.getId())
                .orElseThrow(() -> new RuntimeException("해당 사장님을 찾을 수 없습니다."));

        store.updateStore(storeDTO);
    }
}
