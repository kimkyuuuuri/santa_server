package com.smileflower.santa.profile.model.dto;

import java.time.LocalDateTime;

public class FlagResponse {
    private Long flagIdx;
    private int userIdx;
    private Long mountainIdx;
    private String createdAt;
    private String pictureUrl;
    private int flagCount;
    private String name;

    public FlagResponse(Long flagIdx, int userIdx, Long mountainIdx,  String createdAt, String pictureUrl,int flagCount,String name) {
        this.flagIdx = flagIdx;
        this.userIdx = userIdx;
        this.mountainIdx = mountainIdx;
        this.createdAt = createdAt;
        this.pictureUrl = pictureUrl;
        this.flagCount = flagCount;
        this.name = name;
    }

    public Long getFlagIdx() {
        return flagIdx;
    }

    public void setFlagIdx(Long flagIdx) {
        this.flagIdx = flagIdx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public Long getMountainIdx() {
        return mountainIdx;
    }

    public void setMountainIdx(Long mountainIdx) {
        this.mountainIdx = mountainIdx;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
