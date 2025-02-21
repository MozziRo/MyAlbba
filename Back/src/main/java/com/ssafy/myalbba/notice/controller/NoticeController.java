package com.ssafy.myalbba.notice.controller;

import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.notice.dto.NoticeRequestDto;
import com.ssafy.myalbba.notice.dto.NoticeResponseDto;
import com.ssafy.myalbba.notice.service.NoticeService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getNotice(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return ResponseEntity.ok(noticeService.getNotice(loginUser.getUserEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNoticeById(@PathVariable Long id){
        return new ResponseEntity<NoticeResponseDto>(noticeService.getNoticeById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoticeResponseDto> registerNotice(@RequestBody NoticeRequestDto noticeRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.registerNotice(customUserDetails.getUserEmail(), noticeRequestDto));
    }

    @PutMapping
    public ResponseEntity<?> updateNotice(@RequestBody NoticeRequestDto noticeRequestDto){
        noticeService.updateNotice(noticeRequestDto);

        return new ResponseEntity<String>("공지사항 수정 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);

        return new ResponseEntity<String>("공지사항 삭제 성공", HttpStatus.OK);
    }

}
