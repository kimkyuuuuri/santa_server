package com.smileflower.santa.src.social_login.model;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleUser {
    //Member Field
    private Email emailId;
    private String passwd;
    private int isKakao;
    private int isApple;
    private String userImageUrl;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;
    private String status;
    private String name;

    //Constructor
    public AppleUser(Email emailId, String passwd, String userImageUrl, LocalDateTime updateAt, LocalDateTime createAt, String status, String name) {
        this.emailId = emailId;
        this.passwd = passwd;
        this.isKakao = 0;
        this.isApple = 1;
        this.userImageUrl = userImageUrl;
        this.updateAt = null;
        this.createAt = null;
        this.status = status;
        this.name = name;
    }

    public AppleUser(Email emailId, String name){
        this(emailId,null,null,null,null,null,name);
    }



}
