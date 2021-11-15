package com.smileflower.santa.profile.model.dto;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ProfilePostsResponse implements Comparable<ProfilePostsResponse> {
    private boolean isFlag;
    private Long flagIdx;
    private Long pictureIdx;
    private int userIdx;
    private int flagCount;
    private Long mountainIdx;
    private String name;
    private String createdAt;
    private String pictureUrl;

    public ProfilePostsResponse(boolean isFlag, Long flagIdx, Long pictureIdx, int userIdx, int flagCount, Long mountainIdx, String name, String createdAt, String pictureUrl) {
        this.isFlag = isFlag;
        this.flagIdx = flagIdx;
        this.pictureIdx = pictureIdx;
        this.userIdx = userIdx;
        this.flagCount = flagCount;
        this.mountainIdx = mountainIdx;
        this.name = name;
        this.createdAt = createdAt;
        this.pictureUrl = pictureUrl;
    }

    public boolean getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(boolean flag) {
        isFlag = flag;
    }

    public Long getFlagIdx() {
        return flagIdx;
    }

    public void setFlagIdx(Long flagIdx) {
        this.flagIdx = flagIdx;
    }

    public Long getPictureIdx() {
        return pictureIdx;
    }

    public void setPictureIdx(Long pictureIdx) {
        this.pictureIdx = pictureIdx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public Long getMountainIdx() {
        return mountainIdx;
    }

    public void setMountainIdx(Long mountainIdx) {
        this.mountainIdx = mountainIdx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    @Override
    public int compareTo(@NotNull ProfilePostsResponse o) {
        LocalDateTime l1 = LocalDateTime.parse(o.getCreatedAt(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime l2 = LocalDateTime.parse(this.getCreatedAt(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if(l1.isAfter(l2)){
            return 1;
        }
        else if(l1.isEqual(l2)){
            return 0;
        }
        else{
            return -1;
        }
    }
}
