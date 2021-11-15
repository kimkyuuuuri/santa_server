package com.smileflower.santa.profile.model.dto;

public class DeletePictureResponse {
    private boolean isSuccess;

    public DeletePictureResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
