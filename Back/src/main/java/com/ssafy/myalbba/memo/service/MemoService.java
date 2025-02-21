package com.ssafy.myalbba.memo.service;

import com.ssafy.myalbba.memo.dto.MemoRequestDto;
import com.ssafy.myalbba.memo.dto.MemoResponseDto;
import com.ssafy.myalbba.memo.dto.NextWorkerDto;

import java.util.List;

public interface MemoService {
    void registerMemo(MemoRequestDto memoRequestDto, String email);

    List<MemoResponseDto> getMyMemo(String email);

    void checkMemo(Long memoId);

    List<MemoResponseDto> getSendMemos(String email);

    List<MemoResponseDto> getAllMemos(String userEmail);

    List<NextWorkerDto> getNextWorkers(String userEmail);
}
