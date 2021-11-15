package com.smileflower.santa.profile.model.domain;

import java.time.LocalDateTime;

public class Picture {
    //Member Field
    private final Long pictureIdx;
    private int userIdx;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    //Constructor
    public Picture(Long pictureIdx, int userIdx, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.pictureIdx = pictureIdx;
        this.userIdx = userIdx;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public Picture(int userIdx, String imageUrl) {
        this(null,userIdx,imageUrl,null,null,"T");
    }

    //Getter
    public Long getPictureIdx() {
        return pictureIdx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public String getImageUrl() {
        return imageUrl;
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
