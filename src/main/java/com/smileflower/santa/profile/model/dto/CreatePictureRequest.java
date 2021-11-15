package com.smileflower.santa.profile.model.dto;

public class CreatePictureRequest {
    private String imageUrl;

    public CreatePictureRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
