package com.ssafy.myalbba.notice.service;

import com.ssafy.myalbba.notice.dto.NoticeRequestDto;
import com.ssafy.myalbba.notice.dto.NoticeResponseDto;
import com.ssafy.myalbba.notice.entity.Notice;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeService {
    List<NoticeResponseDto> getNotice(String loginUserEmail);

    NoticeResponseDto getNoticeById(Long id);

    NoticeResponseDto registerNotice(String email, NoticeRequestDto noticeRequestDto);

    void updateNotice(NoticeRequestDto noticeRequestDto);

    void deleteNotice(Long id);
}
