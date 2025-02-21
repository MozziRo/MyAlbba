package com.ssafy.myalbba.schedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubstituteScheduleRequestDTO {
    private Long substituteScheduleId;
    private Long confirmedScheduleId;
}
