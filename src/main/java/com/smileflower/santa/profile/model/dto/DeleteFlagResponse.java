package com.smileflower.santa.profile.model.dto;

public class DeleteFlagResponse {
    private boolean isSuccess;

    public DeleteFlagResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
