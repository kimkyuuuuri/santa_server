package com.smileflower.santa.src.social_login.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ApplePostUserReq {
    private String name;

    private String userIdentifier;
    private String authorizationCode;
    private String identifyToken;
    private String pushToken="N";
    private String tokenType="N";
}
