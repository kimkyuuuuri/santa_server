package com.smileflower.santa.profile.model.dto;

public class ReportFlagResponse {
    private Long flagIdx;
    private int reportCnt;

    public ReportFlagResponse(Long flagIdx, int reportCnt) {
        this.flagIdx = flagIdx;
        this.reportCnt = reportCnt;
    }

    public Long getFlagIdx() {
        return flagIdx;
    }

    public void setFlagIdx(Long flagIdx) {
        this.flagIdx = flagIdx;
    }

    public int getReportCnt() {
        return reportCnt;
    }

    public void setReportCnt(int reportCnt) {
        this.reportCnt = reportCnt;
    }
}
