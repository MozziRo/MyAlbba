package com.ssafy.myalbba.salary.controller;

import com.ssafy.myalbba.Member.dto.WorkerDTO;
import com.ssafy.myalbba.Member.service.SignUpService;
import com.ssafy.myalbba.salary.dto.DailySalaryDTO;
import com.ssafy.myalbba.salary.dto.SalaryBankDTO;
import com.ssafy.myalbba.salary.dto.SalaryDTO;
import com.ssafy.myalbba.salary.service.SalaryService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.scheduler.Scheduler;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salary")
@Tag(name = "Salary", description = "월급 API")
public class SalaryController {

    private final SalaryService salaryService;

    @GetMapping("/daily")
    @Operation(summary = "가게 알바생 전체 일급 조회", description = "가게 알바생들의 모든 일급을 조회하는 API")
    public ResponseEntity<List<DailySalaryDTO>> getAllDailySalary(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(salaryService.getAllDailySalary(customUserDetails.getUserEmail()));
    }

    @GetMapping("/monthly")
    @Operation(summary = "가게 알바생 전체 월급 조회", description = "가게 알바생들의 모든 월급을 조회하는 API")
    public ResponseEntity<List<SalaryDTO>> getAllMonthlySalary(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(salaryService.getAllMonthlySalary(customUserDetails.getUserEmail()));
    }

    @GetMapping("/daily/my")
    @Operation(summary = "내 전체 일급 조회", description = "내 전체 일급을 조회하는 API")
    public ResponseEntity<List<DailySalaryDTO>> getMyDailySalary(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(salaryService.getMyDailySalary(customUserDetails.getUserEmail()));
    }

    @GetMapping("/monthly/my")
    @Operation(summary = "내 월급 조회", description = "내 월급을 조회하는 API")
    public ResponseEntity<List<SalaryDTO>> getMyMonthlySalary(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(salaryService.getMyMonthlySalary(customUserDetails.getUserEmail()));
    }

    @PutMapping("/monthly/complete/{salaryId}")
    @Operation(summary = "월급 정산 상태 변경", description = "월급 정산 상태를 변경하는 API")
    public ResponseEntity<?> updateSalaryStatusAsCompleted(@PathVariable Long salaryId) {
        salaryService.updateSalaryStatusAsCompleted(salaryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bank/my")
    @Operation(summary = "사장님 은행 조회", description = "송금 연결을 위한 은행 API")
    public ResponseEntity<SalaryBankDTO> getMyBankName(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(salaryService.getMyBankName(customUserDetails.getUserEmail()));
    }

}
