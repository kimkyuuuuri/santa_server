package com.smileflower.santa.apple.model.dto;

public class AppleSigninResponse {
    private String refresh_token;
    private String emailId;
    private String name;

    public AppleSigninResponse(String refresh_token, String emailId, String name) {
        this.refresh_token = refresh_token;
        this.emailId = emailId;
        this.name = name;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
