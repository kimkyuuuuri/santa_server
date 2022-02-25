package com.smileflower.santa.src.social_login.model;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppleUser {
    //Member Field
    private Email emailId;
    private String passwd;
    private String kakao;
    private String apple;
    private String userImageUrl;
    private String userIdentifier;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;
    private String status;
    private String name;

    //Constructor
    public AppleUser(Email emailId, String passwd, String userImageUrl, LocalDateTime updateAt, LocalDateTime createAt, String status, String name,String id) {
        this.emailId = emailId;
        this.passwd = passwd;
        this.kakao = "";
        this.apple = id;
        this.userImageUrl = userImageUrl;
        this.updateAt = null;
        this.createAt = null;
        this.status = status;
        this.name = name;
    }


    public AppleUser(Email userEmail, String name, String userIdentifier) {

        this.emailId=userEmail;
        this.name=name;
        this.userIdentifier=userIdentifier;
    }
}
