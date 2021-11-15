package com.smileflower.santa.flag.model;

public class UploadImageResponse {
    private boolean isFlag;
    private boolean isDoubleVisited;
    private String fileUrl;

    public UploadImageResponse(boolean isFlag, boolean isDoubleVisited, String fileUrl) {
        this.isFlag = isFlag;
        this.isDoubleVisited = isDoubleVisited;
        this.fileUrl = fileUrl;
    }

    public UploadImageResponse(boolean isFlag, String fileUrl) {
        this.isFlag = isFlag;
        this.fileUrl = fileUrl;
    }

    public UploadImageResponse(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean getIsDoubleVisited() {
        return isDoubleVisited;
    }

    public void setIsDoubleVisited(boolean isDoubleVisited) {
        this.isDoubleVisited = isDoubleVisited;
    }

    public boolean getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
