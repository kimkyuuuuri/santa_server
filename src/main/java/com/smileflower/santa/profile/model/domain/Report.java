package com.smileflower.santa.profile.model.domain;

import java.time.LocalDateTime;

public class Report {
    //Member Field
    private final Long reportIdx;
    private Long userIdx;
    private Long flagIdx;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    //Constructor

    public Report(Long reportIdx, Long userIdx, Long flagIdx, LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.reportIdx = reportIdx;
        this.userIdx = userIdx;
        this.flagIdx = flagIdx;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public Report(){
        this(null,null,null,null,null,"T");
    }

    //GETTER


    public Long getReportIdx() {
        return reportIdx;
    }

    public Long getUserIdx() {
        return userIdx;
    }

    public Long getFlagIdx() {
        return flagIdx;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getStatus() {
        return status;
    }
}
