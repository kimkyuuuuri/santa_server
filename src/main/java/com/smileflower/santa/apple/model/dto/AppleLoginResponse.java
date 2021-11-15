package com.smileflower.santa.apple.model.dto;

public class AppleLoginResponse {
    private String emailId;
    private String refresh_token;

    public AppleLoginResponse(String emailId, String refresh_token) {
        this.emailId = emailId;
        this.refresh_token = refresh_token;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}

