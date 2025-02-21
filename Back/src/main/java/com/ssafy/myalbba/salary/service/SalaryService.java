package com.ssafy.myalbba.salary.service;

import com.ssafy.myalbba.salary.dto.DailySalaryDTO;
import com.ssafy.myalbba.salary.dto.SalaryBankDTO;
import com.ssafy.myalbba.salary.dto.SalaryDTO;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleResponseDTO;

import java.util.List;

public interface SalaryService {

    void registerSalary(String loginUserEmail, List<ConfirmedScheduleResponseDTO> confirmedScheduleList);

    void addNewSalary(String loginUserEmail, ConfirmedScheduleResponseDTO confirmedSchedule);

    void deleteSalary(Long confirmedScheduleId);

    List<DailySalaryDTO> getAllDailySalary(String loginUserEmail);

    List<SalaryDTO> getAllMonthlySalary(String loginUserEmail);

    List<DailySalaryDTO> getMyDailySalary(String loginUserEmail);

    List<SalaryDTO> getMyMonthlySalary(String loginUserEmail);

    void updateSalaryStatusAsCompleted(Long salaryId);

    void sendPaydayAlarm();

    SalaryBankDTO getMyBankName(String userEmail);
}
