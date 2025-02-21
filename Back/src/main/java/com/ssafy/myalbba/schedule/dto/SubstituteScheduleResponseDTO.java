package com.ssafy.myalbba.schedule.dto;

import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import com.ssafy.myalbba.schedule.entity.SubstituteSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class SubstituteScheduleResponseDTO {
    private Long substituteScheduleId;
    private Long confirmedScheduleId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long requesterId;
    private String requesterName;

    public static SubstituteScheduleResponseDTO from(SubstituteSchedule substituteSchedule) {
        ConfirmedSchedule confirmedSchedule = substituteSchedule.getConfirmedSchedule();
        Worker requester = substituteSchedule.getRequester();

        return new SubstituteScheduleResponseDTO(substituteSchedule.getId(), confirmedSchedule.getId(), confirmedSchedule.getDate(), confirmedSchedule.getStartTime(), confirmedSchedule.getEndTime(), requester.getId(), requester.getName());
    }
}
