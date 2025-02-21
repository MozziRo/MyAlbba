package com.ssafy.myalbba.schedule.controller;

import com.ssafy.myalbba.salary.service.SalaryService;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleResponseDTO;
import com.ssafy.myalbba.schedule.dto.SubstituteScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.SubstituteScheduleResponseDTO;
import com.ssafy.myalbba.schedule.service.ScheduleService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@Tag(name = "Schedule", description = "확정 스케줄 및 대타 스케줄 API")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SalaryService salaryService;

    @PostMapping("/confirm")
    @Operation(summary = "한 달 전체 확정 스케줄 등록", description = "한 달 전체 확정 스케줄을 등록하는 API")
    public ResponseEntity<?> createConfirmedSchedule(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody List<ConfirmedScheduleRequestDTO> confirmedScheduleList) {
        List<ConfirmedScheduleResponseDTO> savedSchedule =  scheduleService.createConfirmedSchedule(loginUser.getUserEmail(), confirmedScheduleList);

        // 확정 스케줄 등록 시 일급 및 월급도 함께 등록
        salaryService.registerSalary(loginUser.getUserEmail(), savedSchedule);

        return ResponseEntity.ok("확정 스케줄이 성공적으로 등록되었습니다.");
    }

    @PostMapping("/confirm/new")
    @Operation(summary = "확정 스케줄 추가 등록", description = "새로운 스케줄을 추가하는 API")
    public ResponseEntity<?> addNewConfirmedSchedule(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody ConfirmedScheduleRequestDTO confirmedSchedule) {
        ConfirmedScheduleResponseDTO savedSchedule = scheduleService.addNewConfirmedSchedule(loginUser.getUserEmail(), confirmedSchedule);
        
        // 확정 스케줄 추가 시 일급 및 월급에도 변경사항 반영
        salaryService.addNewSalary(loginUser.getUserEmail(), savedSchedule);
        return ResponseEntity.ok("새로운 스케줄이 성공적으로 추가되었습니다.");
    }

    @GetMapping("/confirm")
    @Operation(summary = "확정 스케줄 전체 조회", description = "확정 스케줄을 전체 조회하는 API")
    public ResponseEntity<List<ConfirmedScheduleResponseDTO>> getAllConfirmedSchedule(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(scheduleService.getAllConfirmedSchedule(loginUser.getUserEmail()), HttpStatus.OK);
    }

    @GetMapping("/confirm/my")
    @Operation(summary = "나의 확정 스케줄 조회", description = "나의 확정 스케줄을 조회하는 API")
    public ResponseEntity<List<ConfirmedScheduleResponseDTO>> getMyConfirmedSchedule(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(scheduleService.getMyConfirmedSchedule(loginUser.getUserEmail()), HttpStatus.OK);
    }

    @DeleteMapping("/confirm/{confirmedScheduleId}")
    @Operation(summary = "확정 스케줄 삭제", description = "확정 스케줄을 삭제하는 API")
    public ResponseEntity<?> updateConfirmedSchedule(@PathVariable Long confirmedScheduleId) {
        // 스케줄 삭제 시 일급 및 월급에도 변경사항 반영
        salaryService.deleteSalary(confirmedScheduleId);

        scheduleService.deleteConfirmedSchedule(confirmedScheduleId);
        return ResponseEntity.ok("확정 스케줄이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/substitute")
    @Operation(summary = "대타 스케줄 등록", description = "대타 스케줄을 등록하는 API")
    public ResponseEntity<?> createSubstituteSchedule(@RequestBody SubstituteScheduleRequestDTO substituteScheduleDTO) {
        scheduleService.createSubstituteSchedule(substituteScheduleDTO);
        return ResponseEntity.ok("대타 스케줄이 성공적으로 등록되었습니다.");
    }
    
    @GetMapping("/substitute")
    @Operation(summary = "대타 스케줄 조회", description = "대타 스케줄을 조회하는 API")
    public ResponseEntity<List<SubstituteScheduleResponseDTO>> getAllSubstituteSchedule(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(scheduleService.getAllSubstituteSchedule(loginUser.getUserEmail()), HttpStatus.OK);
    }

    @PutMapping("/confirm")
    @Operation(summary = "확정 스케줄에 대타 스케줄 반영", description = "확정 스케줄 근무자를 대타 스케줄 지원자로 변경 및 신청된 대타 스케줄을 삭제하는 API")
    public ResponseEntity<?> applySubstituteSchedule(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody SubstituteScheduleRequestDTO substituteScheduleDTO) {
        ConfirmedScheduleResponseDTO confirmedSchedule = scheduleService.applySubstituteSchedule(loginUser.getUserEmail(), substituteScheduleDTO);
        
        // 대타 스케줄이 확정 스케줄로 반영 시 일급 및 월급에도 변경사항 반영
        salaryService.addNewSalary(loginUser.getUserEmail(), confirmedSchedule);
        return ResponseEntity.ok("대타 스케줄이 성공적으로 반영되었습니다.");
    }

}
