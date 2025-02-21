package com.ssafy.myalbba.memo.service;

import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.Member.repository.WorkerRepository;
import com.ssafy.myalbba.memo.dto.MemoRequestDto;
import com.ssafy.myalbba.memo.dto.MemoResponseDto;
import com.ssafy.myalbba.memo.dto.NextWorkerDto;
import com.ssafy.myalbba.memo.entity.Memo;
import com.ssafy.myalbba.memo.entity.Status;
import com.ssafy.myalbba.memo.entity.WorkerMemo;
import com.ssafy.myalbba.memo.repository.MemoRepository;
import com.ssafy.myalbba.memo.repository.WorkerMemoRepository;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import com.ssafy.myalbba.schedule.repository.ConfirmedScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final ConfirmedScheduleRepository confirmedScheduleRepository;

    @Override
    public void registerMemo(MemoRequestDto memoRequestDto, String email) {
        Worker creator = workerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        Memo memo = Memo.builder()
                .content(memoRequestDto.getContent())
                .status(Status.NOT_DONE)
                .creator(creator)
                .store(creator.getStore())
                .build();

        memoRepository.save(memo);

        memoRequestDto.getWorkers().forEach(
                workerId -> {
                    Worker worker = workerRepository.findById(workerId)
                            .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

                    WorkerMemo workerMemo = new WorkerMemo();
                    workerMemo.setMemo(memo);
                    worker.addWorkersMemo(workerMemo);

                    workerRepository.save(worker);
                });

        log.info("Memo registered successfully for {} workers", memoRequestDto.getWorkers().size());
    }

    @Override
    public List<MemoResponseDto> getMyMemo(String email) {

        Worker worker = workerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));



        // 상태가 NOT_DONE 인 메모들만 필터링
        return worker.getWorkerMemos().stream()
                .map(WorkerMemo::getMemo)
                .filter(m -> m.getStatus() == Status.NOT_DONE)
                .map(MemoResponseDto::from)
                .toList();
    }

    @Override
    public List<MemoResponseDto> getSendMemos(String email) {
        Worker worker = workerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        return memoRepository.findByCreatorId(worker.getId()).stream()
                .map(MemoResponseDto::from)
                .toList();
    }

    @Override
    public List<MemoResponseDto> getAllMemos(String loginUserEmail) {
        UserEntity userEntity = userRepository.findByEmail(loginUserEmail);

        List<Memo> memos = memoRepository.findByStoreId(userEntity.getStore().getId());

        return memos.stream()
                .filter(m -> m.getStatus() == Status.NOT_DONE)
                .map(MemoResponseDto::from)
                .toList();
    }

    @Override
    public List<NextWorkerDto> getNextWorkers(String userEmail) {
        Worker worker = workerRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 현재 사용자의 가장 최근 근무 스케줄 조회
        ConfirmedSchedule latestSchedule = confirmedScheduleRepository
                .findTopByWorkerAndDateAfterOrderByDateAscStartTimeAsc(worker, LocalDate.now())
                .orElse(null);

        // 만약 근무 스케줄이 없으면 빈 리스트 반환
        if (latestSchedule == null) {
            return List.of();
        }

        // 내 근무 종료 시간 이후의 가장 가까운 스케줄 조회
        ConfirmedSchedule nextSchedule = confirmedScheduleRepository
                .findTopByDateAndStartTimeAfterOrderByStartTimeAsc(
                        latestSchedule.getDate(), latestSchedule.getEndTime())
                .orElse(null);

        if (nextSchedule == null) {
            return List.of();
        }

        // 같은 스케줄을 가지는 다른 근무자 조회
        List<Worker> nextWorkers = confirmedScheduleRepository.findWorkersByConfirmedSchedule(nextSchedule);

        return nextWorkers.stream()
                .map(nextWorker -> new NextWorkerDto(nextWorker.getId(), nextWorker.getName()))
                .toList();
    }

    @Override
    @Transactional
    public void checkMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("Memo not found"));

        // 현재 상태가 DONE이면 NOT_DONE으로 아니면 DONE으로 변경
        if (memo.getStatus() == Status.DONE) {
            memo.setStatus(Status.NOT_DONE);
        } else if (memo.getStatus() == Status.NOT_DONE) {
            memo.setStatus(Status.DONE);
        }
    }
}
