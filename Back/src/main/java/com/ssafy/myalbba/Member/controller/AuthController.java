package com.ssafy.myalbba.Member.controller;

import com.ssafy.myalbba.Member.dto.*;
import com.ssafy.myalbba.Member.service.EmailSenderService;
import com.ssafy.myalbba.Member.service.SignUpService;
import com.ssafy.myalbba.Member.service.VerificationCodeService;
import com.ssafy.myalbba.common.dto.StoreDTO;
import com.ssafy.myalbba.common.service.StoreService;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "회원 정보 관리 API")
public class AuthController {

    private final SignUpService signUpService;
    private final StoreService storeService;
    private final EmailSenderService emailSenderService;
    private final VerificationCodeService verificationCodeService;


    // 이메일 중복 확인
    @PostMapping("/check/duplicate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> checkDuplication(@RequestBody EmailCheckDTO emailCheckDTO) {
        if(!signUpService.checkDuplication(emailCheckDTO)) {
            return new ResponseEntity<String>("중복되지 않은 이메일입니다.", HttpStatus.OK);
        }

        return new ResponseEntity<String>("중복된 이메일 입니다.", HttpStatus.CONFLICT);
    }
    
    // owner 이메일로 인증 코드 전송
    @PostMapping("/sendcode")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> sendVerificationCode(@RequestBody WorkerDTO workerDTO) {
        System.out.println("send code");
        emailSenderService.sendEmail(workerDTO.getOwnerEmail());

        return new ResponseEntity<String>("인증 코드 보내기 성공", HttpStatus.OK);
    }
    
    // 인증 코드 검증
    @PostMapping("/verifycode")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> verifyCode(@RequestBody WorkerDTO workerDTO) {
        boolean isValid = verificationCodeService.verifyCode(workerDTO.getOwnerEmail(), workerDTO.getCode());
        
        if(isValid) 
            return new ResponseEntity<String>("인증 성공", HttpStatus.OK);
        
        return new ResponseEntity<String>("인증 실패", HttpStatus.BAD_REQUEST);
    }
    
    // 사장님 회원 가입
    @PostMapping("/signup/owner")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> signUpOwner(@RequestBody OwnerDTO ownerDTO) {
        // OwnerSignUpRequestDTO를 회원 정보와 가게 정보로 나누어 service 호출
        // 회원 정보 저장
        Long ownerId = signUpService.signUpOwner(ownerDTO);
        
        // 가게 정보 저장
        StoreDTO storeDTO = new StoreDTO(ownerId, ownerDTO.getStoreName(), ownerDTO.getStoreAddress(), ownerDTO.getStorePhoneNumber(), ownerDTO.getPayday());
        Long storeId = storeService.createStore(storeDTO);

        signUpService.setStore(ownerId, storeId);

        return new ResponseEntity<String>("회원가입 성공", HttpStatus.OK);
    }
    
    // 알바생 회원가입
    @PostMapping("/signup/worker")
    public ResponseEntity<?> signUpWorker(@RequestBody WorkerDTO workerDTO) {
        signUpService.signUpWorker(workerDTO);
        return new ResponseEntity<String>("회원가입 성공", HttpStatus.OK);
    }

    @PostMapping("/check/password")
    @Operation(summary = "비밀번호 확인", description = "비밀번호 확인으로 로그인 사용자가 본인임을 확인하는 API")
    public ResponseEntity<?> checkPassword(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody PasswordCheckRequestDTO passwordCheckRequestDTO) {
        boolean isPasswordCorrect = signUpService.checkPassword(loginUser.getUserEmail(), passwordCheckRequestDTO);

        if(isPasswordCorrect)
            return ResponseEntity.ok(Map.of("message", "비밀번호 검증이 성공하였습니다."));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "비밀번호 검증이 실패하였습니다.")); // 401 Unauthorized 에러
    }

    @PutMapping("/worker")
    @Operation(summary = "알바생 정보 수정", description = "알바생의 회원 정보를 수정하는 API")
    public ResponseEntity<?> updateWorker(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody WorkerDTO workerDTO) {
        signUpService.updateWorker(loginUser.getUserEmail(), workerDTO);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/worker/{workerId}")
    @Operation(summary = "알바생 정보 수정", description = "알바생의 회원 정보 및 월급 관련 정보를 수정하는 API")
    public ResponseEntity<?> updateWorkerSalaryInfo(@PathVariable Long workerId, @RequestBody WorkerDTO workerDTO) {
        signUpService.updateWorkerSalaryInfo(workerId, workerDTO);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/owner")
    @Operation(summary = "사장 정보 수정", description = "사장 회원 정보 수정하는 API")
    public ResponseEntity<?> updateOwner(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody OwnerDTO ownerDTO) {
        signUpService.updateOwner(loginUser.getUserEmail(), ownerDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "모든 알바생 조회", description = "모든 알바생을 조회해서 PK와 Name을 응답하는 API")
    public ResponseEntity<List<WorkerDTO>> getAllWorkers(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(signUpService.getAllWorkers(loginUser.getUserEmail()), HttpStatus.OK);
    }

    @GetMapping("/{workerId}")
    @Operation(summary = "해당 알바생 정보 조회", description = "해당 알바생의 모든 정보를 조회하는 API")
    public ResponseEntity<WorkerDTO> getWorkerInfo(@PathVariable Long workerId) {
        return new ResponseEntity<>(signUpService.getWorkerInfo(workerId), HttpStatus.OK);
    }

    @GetMapping("/worker/my")
    @Operation(summary = "알바생 본인 정보 조회", description = "알바생 본인의 모든 정보를 조회하는 API")
    public ResponseEntity<WorkerDTO> getMyWorkerInfo(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(signUpService.getMyWorkerInfo(loginUser.getUserEmail()), HttpStatus.OK);
    }

    @GetMapping("/owner/my")
    @Operation(summary = "사장님 본인 정보 조회", description = "사장님 본인의 모든 정보를 조회하는 API")
    public ResponseEntity<OwnerDTO> getMyOwnerInfo(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(signUpService.getMyOwnerInfo(loginUser.getUserEmail()), HttpStatus.OK);
    }

    @GetMapping("/my")
    @Operation(summary = "내 정보 조회", description = "메인 화면에 진입할때 사용자의 정보를 전달해주는 API")
    public ResponseEntity<UserInfoDTO> getMyInfo(@AuthenticationPrincipal CustomUserDetails loginUser) {
        return new ResponseEntity<>(signUpService.getMyInfo(loginUser.getUserEmail()), HttpStatus.OK);
    }

}
