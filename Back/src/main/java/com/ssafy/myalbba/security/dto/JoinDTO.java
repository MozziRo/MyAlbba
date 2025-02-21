package com.ssafy.myalbba.security.dto;

import lombok.Getter;
import lombok.Setter;

// 회원가입 dto
@Setter
@Getter
public class JoinDTO {

    private String email;
    private String password;
}