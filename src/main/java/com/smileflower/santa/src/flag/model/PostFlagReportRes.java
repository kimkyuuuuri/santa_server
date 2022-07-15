package com.smileflower.santa.src.flag.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostFlagReportRes {
    private Long flagIdx;
    private int reportCnt;

    public PostFlagReportRes(Long flagIdx, int reportCnt) {
        this.flagIdx = flagIdx;
        this.reportCnt = reportCnt;
    }


}
