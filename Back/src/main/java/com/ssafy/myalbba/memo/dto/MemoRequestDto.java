package com.ssafy.myalbba.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemoRequestDto {
    private String content;
    private List<Long> workers;  // 보내고자 하는 알바생의 id 값
}
