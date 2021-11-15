package com.smileflower.santa.profile.model.domain;

import java.time.LocalDateTime;

public class Mountain {
    //MemberField
    private final Long mountainIdx;
    private String name;
    private int high;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private double latitude;
    private double longitude;
    private String imageUrl;

    //Constructor

    public Mountain(Long mountainIdx, String name, int high, String address, LocalDateTime createdAt, LocalDateTime updatedAt, String status, double latitude, double longitude, String imageUrl) {
        this.mountainIdx = mountainIdx;
        this.name = name;
        this.high = high;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }

    public Mountain(String name, int high, String address, double latitude, double longitude, String imageUrl) {
        this(null,name,high,address,null,null,"T",latitude,longitude,imageUrl);
    }

    //GETTER

    public Long getMountainIdx() {
        return mountainIdx;
    }

    public String getName() {
        return name;
    }

    public int getHigh() {
        return high;
    }

    public String getAddress() {
        return address;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
