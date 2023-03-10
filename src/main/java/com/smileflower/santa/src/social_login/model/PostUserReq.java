package com.smileflower.santa.src.social_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String accessToken;
    private String pushToken;
    private String tokenType;
    public PostUserReq(){}
}