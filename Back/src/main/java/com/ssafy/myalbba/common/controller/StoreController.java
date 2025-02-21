package com.ssafy.myalbba.common.controller;

import com.ssafy.myalbba.common.dto.StoreDTO;
import com.ssafy.myalbba.common.service.StoreService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<StoreDTO> getStore(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return ResponseEntity.ok(storeService.getStore(loginUser.getUserEmail()));
    }

    @PutMapping
    public ResponseEntity<?> updateStore(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody StoreDTO storeDTO) {
        storeService.updateStore(loginUser.getUserEmail(), storeDTO);
        return ResponseEntity.ok().build();
    }

}
