package com.smileflower.santa.apple.model.dto;

public class CheckUserRequest {
    private String authorizationCode;
    private String identifyToken;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getIdentifyToken() {
        return identifyToken;
    }

    public void setIdentifyToken(String identifyToken) {
        this.identifyToken = identifyToken;
    }
}
