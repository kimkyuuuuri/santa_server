package com.smileflower.santa.profile.model.dto;

public class UploadImageResponse {
    private String fileUrl;

    public UploadImageResponse(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
