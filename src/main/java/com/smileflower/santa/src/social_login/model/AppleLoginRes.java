package com.smileflower.santa.src.social_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppleLoginRes {
    private String emailId;
    private String refresh_token;
    private String jwt;



}

