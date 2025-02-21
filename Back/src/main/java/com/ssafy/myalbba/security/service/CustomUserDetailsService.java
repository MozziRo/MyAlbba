package com.ssafy.myalbba.security.service;

import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.repository.OwnerRepository;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.Member.repository.WorkerRepository;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //DB에서 조회
        UserEntity userData = userRepository.findByEmail(email);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}