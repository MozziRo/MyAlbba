package com.ssafy.myalbba.Member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final StringRedisTemplate redisTemplate;

    // 인증 코드 저장
    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES); // 5분 동안 저장
    }

    // 저장된 인증 코드 가져오기
    public String getCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        String storedCode = getCode(email);
        return code.equals(storedCode);
    }
}
