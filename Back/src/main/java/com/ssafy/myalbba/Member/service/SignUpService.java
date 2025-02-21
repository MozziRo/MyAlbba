package com.ssafy.myalbba.Member.service;

import com.ssafy.myalbba.Member.dto.*;

import java.util.List;

public interface SignUpService {
    // 이메일 중복 확인
    boolean checkDuplication(EmailCheckDTO emailCheckDTO);

    // 사장님 회원 가입
    Long signUpOwner(OwnerDTO ownerDTO);
    
    // 알바생 회원 가입
    void signUpWorker(WorkerDTO workerDTO);

    void updateWorker(String loginUserEmail, WorkerDTO workerDTO);

    void updateWorkerSalaryInfo(Long workerId, WorkerDTO workerDTO);

    void updateOwner(String loginUserEmail, OwnerDTO ownerDTO);

    List<WorkerDTO> getAllWorkers(String loginUserEmail);

    void setStore(Long ownerId, Long storeId);

    WorkerDTO getWorkerInfo(Long workerId);

    WorkerDTO getMyWorkerInfo(String loginUserEmail);

    OwnerDTO getMyOwnerInfo(String loginUserEmail);

    UserInfoDTO getMyInfo(String userEmail);
    Boolean checkPassword(String loginUserEmail, PasswordCheckRequestDTO passwordCheckRequestDTO);
}
