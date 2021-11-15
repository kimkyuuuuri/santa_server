package com.smileflower.santa.apple.model.dto;

import com.smileflower.santa.apple.model.domain.Email;

public class AppleSigninRequest {
    private String name;
    private Email userEmail;
    private String authorizationCode;
    private String identifyToken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Email getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(Email userEmail) {
        this.userEmail = userEmail;
    }

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
