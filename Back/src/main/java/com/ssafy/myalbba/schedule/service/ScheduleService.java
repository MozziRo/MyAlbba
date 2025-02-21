package com.ssafy.myalbba.schedule.service;

import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleResponseDTO;
import com.ssafy.myalbba.schedule.dto.SubstituteScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.SubstituteScheduleResponseDTO;

import java.util.List;

public interface ScheduleService {
    List<ConfirmedScheduleResponseDTO> createConfirmedSchedule(String loginUserEmail, List<ConfirmedScheduleRequestDTO> confirmedSchedule);

    ConfirmedScheduleResponseDTO addNewConfirmedSchedule(String loginUserEmail, ConfirmedScheduleRequestDTO confirmedSchedule);

    List<ConfirmedScheduleResponseDTO> getAllConfirmedSchedule(String loginUserEmail);

    List<ConfirmedScheduleResponseDTO> getMyConfirmedSchedule(String loginUserEmail);

    void deleteConfirmedSchedule(Long confirmedScheduleId);

    void createSubstituteSchedule(SubstituteScheduleRequestDTO substituteScheduleDTO);

    List<SubstituteScheduleResponseDTO> getAllSubstituteSchedule(String loginUserEmail);

    ConfirmedScheduleResponseDTO applySubstituteSchedule(String loginUserEmail, SubstituteScheduleRequestDTO substituteScheduleDTO);

}
