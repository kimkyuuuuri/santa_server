package com.smileflower.santa.src.profile.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetFlagRes {
    private Long flagIdx;
    private int userIdx;
    private Long mountainIdx;
    private String createdAt;
    private String pictureUrl;
    private int flagCount;
    private String name;

    public GetFlagRes(Long flagIdx, int userIdx, Long mountainIdx,  String createdAt, String pictureUrl,int flagCount,String name) {
        this.flagIdx = flagIdx;
        this.userIdx = userIdx;
        this.mountainIdx = mountainIdx;
        this.createdAt = createdAt;
        this.pictureUrl = pictureUrl;
        this.flagCount = flagCount;
        this.name = name;
    }
}
