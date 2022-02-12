package com.smileflower.santa.src.social_login.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplePostUserRes {
    private String refresh_token;
    private String emailId;
    private String name;


}
