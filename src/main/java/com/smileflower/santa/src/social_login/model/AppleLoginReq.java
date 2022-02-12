package com.smileflower.santa.src.social_login.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleLoginReq {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
