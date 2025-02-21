package com.ssafy.myalbba.schedule.dto;

import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
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
public class ConfirmedScheduleRequestDTO {
    private Long workerId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    public static ConfirmedScheduleRequestDTO from(ConfirmedSchedule confirmedSchedule) {
        return new ConfirmedScheduleRequestDTO(confirmedSchedule.getWorker().getId(), confirmedSchedule.getStartTime(), confirmedSchedule.getEndTime(), confirmedSchedule.getDate());
    }

    // Getters and Setters
}
