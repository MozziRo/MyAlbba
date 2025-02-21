package com.ssafy.myalbba.memo.controller;

import com.ssafy.myalbba.memo.dto.MemoRequestDto;
import com.ssafy.myalbba.memo.dto.MemoResponseDto;
import com.ssafy.myalbba.memo.dto.NextWorkerDto;
import com.ssafy.myalbba.memo.service.MemoService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/memo")
@Tag(name = "Memo", description = "메모 API")
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    @Operation(summary = "메모 등록", description = "메모를 적어서 등록하는 API")
    public ResponseEntity<?> registerMemo(@RequestBody MemoRequestDto memoRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println(memoRequestDto.getWorkers());
        memoService.registerMemo(memoRequestDto, customUserDetails.getUserEmail());
        
        // 클라이언트에서 성공적으로 메모 등록이 되었는지 확인하기 위해 응답 본문 생성
        return new ResponseEntity<String>("메모 등록 성공", HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @Operation(summary = "나의 메모 조회", description = "나에게 온 메모들을 조회하는 API")
    public ResponseEntity<List<MemoResponseDto>> getMemos(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(memoService.getMyMemo(customUserDetails.getUserEmail()));
    }

    @GetMapping("/my/next")
    @Operation(summary = "내 다음 근무자 조회", description = "내 다음 근무자를 조회하는 API")
    public ResponseEntity<List<NextWorkerDto>> getNextWorkers(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(memoService.getNextWorkers(customUserDetails.getUserEmail()));
    }

    @GetMapping("/my/send")
    @Operation(summary = "내가 보낸 메모 조회", description = "내가 보낸 메모들을 조회하는 API")
    public ResponseEntity<List<MemoResponseDto>> getSendMemos(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(memoService.getSendMemos(customUserDetails.getUserEmail()));
    }

    @GetMapping
    @Operation(summary = "사장 메모 조회", description = "사장이 모든 알림장을 조회하는 API")
    public ResponseEntity<List<MemoResponseDto>> getAllMemos(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(memoService.getAllMemos(customUserDetails.getUserEmail()));
    }

    @PutMapping("/my")
    @Operation(summary = "메모 완료 체크", description = "메모에 있는 작업들을 완료/미완료 체크하는 API")
    public ResponseEntity<?> checkMemo(@RequestParam("id") Long memoId) {
        memoService.checkMemo(memoId);

        return ResponseEntity.ok().build();
    }
}
