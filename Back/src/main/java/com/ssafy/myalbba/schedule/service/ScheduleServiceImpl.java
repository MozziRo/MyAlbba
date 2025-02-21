package com.ssafy.myalbba.schedule.service;

import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.Member.repository.WorkerRepository;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.common.repository.StoreRepository;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleResponseDTO;
import com.ssafy.myalbba.schedule.dto.SubstituteScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.SubstituteScheduleResponseDTO;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import com.ssafy.myalbba.schedule.entity.SubstituteSchedule;
import com.ssafy.myalbba.schedule.repository.ConfirmedScheduleRepository;
import com.ssafy.myalbba.schedule.repository.SubstituteScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ConfirmedScheduleRepository confirmedScheduleRepository;
    private final SubstituteScheduleRepository substituteScheduleRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final StoreRepository storeRepository;

    @Override
    public List<ConfirmedScheduleResponseDTO> createConfirmedSchedule(String loginUserEmail, List<ConfirmedScheduleRequestDTO> confirmedScheduleList) {
        UserEntity loginUser = userRepository.findByEmail(loginUserEmail);
        Long storeId = loginUser.getStore().getId();
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        List<ConfirmedSchedule> schedules = confirmedScheduleList.stream()
                .map(dto -> {
                    Worker worker = workerRepository.findById(dto.getWorkerId())
                            .orElseThrow(() -> new RuntimeException("근무 알바생을 찾을 수 없습니다."));

                    ConfirmedSchedule confirmedSchedule = ConfirmedSchedule.createConfirmedSchedule(dto.getStartTime(), dto.getEndTime(), dto.getDate());
                    confirmedSchedule.setWorker(worker);
                    confirmedSchedule.setStore(store);

                    return confirmedSchedule;
                })
                .collect(Collectors.toList());

        List<ConfirmedSchedule> savedSchedule = confirmedScheduleRepository.saveAll(schedules);

        return savedSchedule.stream()
                .map(ConfirmedScheduleResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public ConfirmedScheduleResponseDTO addNewConfirmedSchedule(String loginUserEmail, ConfirmedScheduleRequestDTO confirmedScheduleDTO) {
        UserEntity loginUser = userRepository.findByEmail(loginUserEmail);
        Long storeId = loginUser.getStore().getId();
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        Worker worker = workerRepository.findById(confirmedScheduleDTO.getWorkerId())
                .orElseThrow(() -> new RuntimeException("근무 알바생을 찾을 수 없습니다."));

        ConfirmedSchedule confirmedSchedule = ConfirmedSchedule.createConfirmedSchedule(confirmedScheduleDTO.getStartTime(), confirmedScheduleDTO.getEndTime(), confirmedScheduleDTO.getDate());
        confirmedSchedule.setWorker(worker);
        confirmedSchedule.setStore(store);

        return ConfirmedScheduleResponseDTO.from(confirmedScheduleRepository.save(confirmedSchedule));
    }

    @Override
    public List<ConfirmedScheduleResponseDTO> getAllConfirmedSchedule(String loginUserEmail) {
        UserEntity loginUser = userRepository.findByEmail(loginUserEmail);
        Long storeId = loginUser.getStore().getId();
        List<ConfirmedSchedule> confirmedScheduleList = confirmedScheduleRepository.findByStoreId(storeId);

        List<ConfirmedScheduleResponseDTO> responseList = confirmedScheduleList.stream()
                .map(schedule -> {
                    return ConfirmedScheduleResponseDTO.from(schedule);
                })
                .collect(Collectors.toList());

        return responseList;
    }

    @Override
    public List<ConfirmedScheduleResponseDTO> getMyConfirmedSchedule(String loginUserEmail) {
        Long workerId = userRepository.findByEmail(loginUserEmail).getId();
        List<ConfirmedSchedule> confirmedScheduleList = confirmedScheduleRepository.findByWorkerId(workerId);

        List<ConfirmedScheduleResponseDTO> responseList = confirmedScheduleList.stream()
                .map(schedule -> {
                    return ConfirmedScheduleResponseDTO.from(schedule);
                })
                .collect(Collectors.toList());

        return responseList;
    }

    @Override
    public void deleteConfirmedSchedule(Long confirmedScheduleId) {
        confirmedScheduleRepository.deleteById(confirmedScheduleId);
    }

    @Override
    public void createSubstituteSchedule(SubstituteScheduleRequestDTO substituteScheduleDTO) {
        ConfirmedSchedule confirmedSchedule = confirmedScheduleRepository.findById(substituteScheduleDTO.getConfirmedScheduleId())
                .orElseThrow(() -> new RuntimeException("확정 스케줄을 찾을 수 없습니다."));

        Store store = storeRepository.getReferenceById(confirmedSchedule.getStore().getId());
        Worker requester = workerRepository.getReferenceById(confirmedSchedule.getWorker().getId());

        SubstituteSchedule substituteSchedule = SubstituteSchedule.createSubstituteSchedule();
        substituteSchedule.setConfirmedSchedule(confirmedSchedule);
        substituteSchedule.setStore(store);
        substituteSchedule.setRequester(requester);

        substituteScheduleRepository.save(substituteSchedule);
    }

    @Override
    public List<SubstituteScheduleResponseDTO> getAllSubstituteSchedule(String loginUserEmail) {
        UserEntity loginUser = userRepository.findByEmail(loginUserEmail);
        Long storeId = loginUser.getStore().getId();

        List<SubstituteSchedule> substituteScheduleList = substituteScheduleRepository.findByStoreId(storeId);

        List<SubstituteScheduleResponseDTO> responseList = substituteScheduleList.stream()
                .map(schedule -> {
                    return SubstituteScheduleResponseDTO.from(schedule);
                })
                .collect(Collectors.toList());

        return responseList;
    }

    @Override
    public ConfirmedScheduleResponseDTO applySubstituteSchedule(String loginUserEmail, SubstituteScheduleRequestDTO substituteScheduleDTO) {
        try {
            // 확정 스케줄에 대타 스케줄 지원자 반영하기
            SubstituteSchedule substituteSchedule = substituteScheduleRepository.findById(substituteScheduleDTO.getSubstituteScheduleId())
                    .orElseThrow(() -> new RuntimeException("신청된 대타 스케줄을 찾을 수 없습니다."));
            
            ConfirmedSchedule confirmedSchedule = confirmedScheduleRepository.findById(substituteSchedule.getConfirmedSchedule().getId())
                    .orElseThrow(() -> new RuntimeException("확정 스케줄을 찾을 수 없습니다."));

            // 지원자를 구한 대타 스케줄 삭제하기
            substituteScheduleRepository.deleteById(substituteScheduleDTO.getSubstituteScheduleId());

            Long substituteId = userRepository.findByEmail(loginUserEmail).getId();
            Worker substitute = workerRepository.findById(substituteId)
                    .orElseThrow(() -> new RuntimeException("대타 지원자를 찾을 수 없습니다."));

            confirmedSchedule.setWorker(substitute);
            return ConfirmedScheduleResponseDTO.from(confirmedScheduleRepository.save(confirmedSchedule));
        } catch (Exception e) {
            throw new RuntimeException("대타 스케줄 처리 중 오류 발생: " + e.getMessage());
        }
    }



}
