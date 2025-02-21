package com.ssafy.myalbba.webrtc.interviewMemo.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class KeywordsResponseDto {
    private String text;
    private List<String> keywords;
}
