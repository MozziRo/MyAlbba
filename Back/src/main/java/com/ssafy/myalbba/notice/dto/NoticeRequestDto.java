package com.ssafy.myalbba.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDto {
    private Long id;
    private Long ownerId;
    private String title;
    private String content;

    public NoticeRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
