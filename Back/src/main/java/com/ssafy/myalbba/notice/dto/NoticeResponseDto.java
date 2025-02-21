package com.ssafy.myalbba.notice.dto;

import com.ssafy.myalbba.common.entity.SchedulePeriod;
import com.ssafy.myalbba.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private Long ownerId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public NoticeResponseDto(Long id, String name, String address, String phoneNumber, LocalDate scheduleRequestStart, LocalDate scheduleRequestEnd, LocalDateTime payday, SchedulePeriod schedulePeriod) {
    }

    public static NoticeResponseDto from(Notice notice) {
        return new NoticeResponseDto(notice.getId(), notice.getOwner().getId(), notice.getTitle(), notice.getContent(), notice.getCreatedAt());
    }
}
