package com.ssafy.myalbba.schedule.dto;

import com.ssafy.myalbba.notice.dto.NoticeResponseDto;
import com.ssafy.myalbba.notice.entity.Notice;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import com.ssafy.myalbba.schedule.enums.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedScheduleResponseDTO {
    private Long confirmedScheduleId;
    private Long workerId;
    private String workerName;  // 알바생 이름
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    public static ConfirmedScheduleResponseDTO from(ConfirmedSchedule confirmedSchedule) {
        return new ConfirmedScheduleResponseDTO(confirmedSchedule.getId(), confirmedSchedule.getWorker().getId(), confirmedSchedule.getWorker().getName(), confirmedSchedule.getStartTime(), confirmedSchedule.getEndTime(), confirmedSchedule.getDate());
    }

}

