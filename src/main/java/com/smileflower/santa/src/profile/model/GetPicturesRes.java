package com.smileflower.santa.src.profile.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter



public class GetPicturesRes {
    private  Long pictureIdx;
    private int userIdx;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    //Constructor
    public GetPicturesRes(Long pictureIdx, int userIdx, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.pictureIdx = pictureIdx;
        this.userIdx = userIdx;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}
