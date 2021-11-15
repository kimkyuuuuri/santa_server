package com.smileflower.santa.profile.model.dto;

public class FlagsForMapResponse {
    private int userIdx;
    private Long mountainIdx;
    private String mountainName;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private int flagCount;
    private String address;

    public FlagsForMapResponse(int userIdx, Long mountainIdx,String mountainName, String imageUrl, double latitude, double longitude, int flagCount,String address) {
        this.userIdx = userIdx;
        this.mountainIdx = mountainIdx;
        this.mountainName = mountainName;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.flagCount = flagCount;
        this.address = address;
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

    public String getMountainName() {
        return mountainName;
    }

    public void setMountainName(String mountainName) {
        this.mountainName = mountainName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
