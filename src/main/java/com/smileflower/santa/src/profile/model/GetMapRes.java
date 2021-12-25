package com.smileflower.santa.src.profile.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetMapRes {
    private int userIdx;
    private Long mountainIdx;
    private String mountainName;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private int flagCount;
    private String address;

    public GetMapRes(int userIdx, Long mountainIdx,String mountainName, String imageUrl, double latitude, double longitude, int flagCount,String address) {
        this.userIdx = userIdx;
        this.mountainIdx = mountainIdx;
        this.mountainName = mountainName;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.flagCount = flagCount;
        this.address = address;
    }
}
