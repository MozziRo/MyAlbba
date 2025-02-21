package com.ssafy.myalbba.memo.dto;

import com.ssafy.myalbba.memo.entity.Memo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoResponseDto {
    private Long id;
    private String content;
    private String status;
    private String creator;
    private LocalDateTime createdAt;

    public static MemoResponseDto from(Memo memo) {
        return MemoResponseDto.builder()
                .id(memo.getId())
                .content(memo.getContent())
                .status(memo.getStatus().toString())
                .creator(memo.getCreator().getName())
                .createdAt(memo.getCreatedAt())
                .build();
    }
}
