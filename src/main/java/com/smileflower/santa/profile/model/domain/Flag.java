package com.smileflower.santa.profile.model.domain;

import java.time.LocalDateTime;

public class Flag {
    //member field
    private final Long flagIdx;
    private Long userIdx;
    private Long mountainIdx;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private String pictureUrl;

    //CONSTRUCTOR
    public Flag(Long flagIdx, Long userIdx, Long mountainIdx, LocalDateTime createdAt, LocalDateTime updatedAt, String status, String pictureUrl) {
        this.flagIdx = flagIdx;
        this.userIdx = userIdx;
        this.mountainIdx = mountainIdx;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.pictureUrl = pictureUrl;
    }

    public Flag(Long userIdx, Long mountainIdx, String pictureUrl) {
        this(null,userIdx,mountainIdx,null,null,"T",pictureUrl);
    }

    //GETTER
    public Long getFlagIdx() {
        return flagIdx;
    }

    public Long getUserIdx() {
        return userIdx;
    }

    public Long getMountainIdx() {
        return mountainIdx;
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

    public String getPictureUrl() {
        return pictureUrl;
    }
}
