package com.smileflower.santa.profile.model.dto;

public class CreatePictureResponse {
    private int pictureIdx;

    public CreatePictureResponse(int pictureIdx) {
        this.pictureIdx = pictureIdx;
    }

    public int getPictureIdx() {
        return pictureIdx;
    }

    public void setPictureIdx(int pictureIdx) {
        this.pictureIdx = pictureIdx;
    }
}
